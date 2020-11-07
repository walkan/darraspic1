package com.darras.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.*;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.view.View;




public class PageAjouterChantier extends AppCompatActivity
{
    EditText text;
    @Override
   protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_ajouter_chantier);
        text = findViewById(R.id.text);
        text.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }


   public void addNewChantier(View v)
   {
       try
       {
           final String addressChantier = text.getText().toString();
           ConfigFileChantiers.addAddress(addressChantier);
           ListeChantiers.addChantier(addressChantier);
           InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
           imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
           text.clearFocus();
           Toast.makeText(this,"Chantier ajouté", Toast.LENGTH_LONG).show();
           finish();
        }
       catch (Exception e)
       {
           Log.i("WALID", e.getMessage());
           Toast.makeText(this,"Chantier pas ajouté :'(",Toast.LENGTH_LONG).show();
       }
    }

}