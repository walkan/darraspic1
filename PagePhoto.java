package com.darras.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class PagePhoto extends AppCompatActivity {
    File photoFile = null;
    ImageView imageView;
    Button button;
    TextView textview;
    static final int CAPTURE_IMAGE_REQUEST = 1;
    static private String chantierAddress = "";

    private void captureImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }
        else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                try {
                    photoFile = createImageFile();
                    Log.i("WALID", "PHOTO :" + photoFile.getAbsolutePath());

                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(this,
                                "com.darras.myapplication",
                                photoFile);
                        Log.i("WALID", photoFile.getAbsolutePath());
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST);
                    }
                } catch (Exception ex) {
                    // Error occurred while creating the File
                    Log.e("WALID", ex.getMessage());
                }


            }
            else {
                Log.e("WALID","Null : takePictureIntent.resolveActivity");
            }
        }
    }

    static private String getAddress() {
        return chantierAddress;
    }

    public static void setAddress(final String address) {
        chantierAddress = address;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        /////////////////
        File dossier = new File(ApplicationStorageDirectory.PATH + '/' + getAddress());
        if (!dossier.exists())
        {
            dossier.setWritable(true);
            boolean success = dossier.mkdirs();
            if (success)
            {
                displayMessage(getBaseContext(),"Dossier du nouveau chantier créé avec succès");
            }
            else
            {
                displayMessage(getBaseContext(),"Erreur lors de la création du dossier");
            }
        }

        File image = File.createTempFile( imageFileName, ".jpg", dossier );
        return image;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_photo);
        textview = findViewById(R.id.nom_chantier);
        textview.setText(chantierAddress);
        imageView =  findViewById(R.id.imageView);
        button = findViewById(R.id.btnCaptureImage);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Bundle extras = data.getExtras();
        // Bitmap imageBitmap = (Bitmap) extras.get("data");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            imageView.setImageBitmap(myBitmap);
            displayMessage(getBaseContext(), "Photo enregistrée \uD83D\uDC4D");
        } else {
            displayMessage(getBaseContext(), "Photo non enregistrée");
            photoFile.delete();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                captureImage();
            }
            else
            {displayMessage(getBaseContext(),"Cette application ne marchera pas sans les permissions nécessaires");}
        }

    }

    private void displayMessage(Context context, String message) {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

}