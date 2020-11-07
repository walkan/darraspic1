package com.darras.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


public class PagePrincipale extends AppCompatActivity implements Observer

{
    private ExampleAdapter adapter;
    private List<ExampleItem> exampleList;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ApplicationStorageDirectory.createDirectoryIfNeeded(this);
        ListeChantiers.initializeListOfChantiers(this);

        setContentView(R.layout.page_principale);

        createButtonAjouterUnChantier();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        exampleList = new ArrayList<>();

        Log.i("WALID", "ListeChantiers.list.size = " + ListeChantiers.list.size());



        for (final String address : ListeChantiers.list)
        {
            Log.i("WALID", "createButtonForChantier " + address);
            createButtonForChantier(address);
        }
        setUpRecyclerView();

        setOnClick();

        ListeChantiers.addObserver(this);
    }

    private void setOnClick(){

        adapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String ladd = exampleList.get(position).getText1();
                Intent i = new Intent(
                        PagePrincipale.this,
                        PagePhoto.class);
                PagePhoto.setAddress(ladd);
                startActivity(i);
            }

            @Override
            public void onDeleteClick(int position) {

                String adresseasupprimer = exampleList.get(position).getText1();

                new AlertDialog.Builder(PagePrincipale.this).setIcon(android.R.drawable.ic_delete)
                        .setTitle("Voulez-vous supprimer ce chantier ?")
                        .setMessage("Les photos de ce chantier ne seront pas supprimés")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onClick(DialogInterface dialogInterface, int wich) {
                                try {
//                                    exampleList.remove(position);
//                                    adapter.notifyItemRemoved(position);
//                                    exampleList.remove(position);
//                                    adapter.notifyItemRemoved(position);
                                    ConfigFileChantiers.deleteAddress(adresseasupprimer);
                                    exampleList.clear();
//                                    adapter.notifyDataSetChanged();
                                    for (final String address : ListeChantiers.list)
                                    {
                                        createButtonForChantier(address);
                                    }
                                    setUpRecyclerView();
                                    setOnClick();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        })
                        .setNegativeButton("No", null)
                        .show();


            }

        });

    }

    private void setUpRecyclerView() {
        adapter = new ExampleAdapter(exampleList);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void createButtonForChantier(final String address)
    {

        /*exampleList.add(0, new ExampleItem(R.drawable.ic_android, address, "This is Line 2"));*/
        exampleList.add(0,new ExampleItem(address));


       /* Adapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String Naddress = exampleList.get(position).getText1();
                Intent i = new Intent(
                        PagePrincipale.this,
                        PagePhoto.class );
                PagePhoto.setAddress(Naddress);
                startActivity(i);
            }

        });*/

        // Create TextView programmatically.
        /*final TextView textView = (TextView)getLayoutInflater().inflate(R.layout.text_view_chantier_template, null);
        textView.setText(address);
        textView.setId(View.generateViewId());
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        // Add TextView to LinearLayout
        final LinearLayout linearLayout = findViewById(R.id.rootLayout);
        if (linearLayout != null) {
            linearLayout.addView(textView);*/

        ////pour supprimer le boutton
//        textView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                Log.v("long clicked", "pos: " + textView.getText());
//                final String adresseasupprimer = textView.getText().toString();
//                new AlertDialog.Builder(PagePrincipale.this).setIcon(android.R.drawable.ic_delete)
//                        .setTitle("Voulez-vous supprimer ce chantier ?")
//                        .setMessage("Les photos de ce chantier ne seront pas supprimés")
//                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
//                            @RequiresApi(api = Build.VERSION_CODES.O)
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int wich) {
//                                try {
//                                    ConfigFileChantiers.deleteAddress(adresseasupprimer);
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                        })
//                        .setNegativeButton("No", null)
//                        .show();
//
//                return true;
//            }
//        });

    }



    private void createButtonAjouterUnChantier()
    {
        Button addSite = (Button) findViewById(R.id.addSite);
        addSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(
                        PagePrincipale.this,
                        PageAjouterChantier.class );
                startActivity(a);
            }
        });
    }

    @Override
    public void update(Observable observable, Object chantier)
    {
        exampleList.clear();
        adapter.notifyDataSetChanged();

        for (final String address : ListeChantiers.list)
        {
            createButtonForChantier(address);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    public void refreshTheView()
    {
        exampleList.clear();
        for (final String address : ListeChantiers.list)
        {
            createButtonForChantier(address);
        }
        setUpRecyclerView();
        setOnClick();
    }


}