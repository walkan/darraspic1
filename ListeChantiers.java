package com.darras.myapplication;

import android.content.ContextWrapper;
import android.util.Log;
import java.util.ArrayList;
import java.util.Observer;


public class ListeChantiers
{
    static ArrayList<String> list = new ArrayList<>();
    static private ArrayList<Observer> observersList = new ArrayList<>();


    static public void addChantier(final String chantier)
    {
        list.add(chantier);
        notifyObservers();
    }

    static public void deleteChantier(String chantier)
    {
        list.remove(chantier);
        notifyObservers();
    }

    static public void initializeListOfChantiers(ContextWrapper context)
    {
        try
        {
            list = ConfigFileChantiers.readListOfChantiers(context);
        }
        catch (Exception e)
        {
            Log.e("WALID","FAILED to readList");
        }
    }

    static private void notifyObservers()
    {
        for (Observer obs : observersList)
        {
            obs.update(null, null);
        }
    }

    static public void addObserver(Observer observer)
    {
        observersList.add(observer);
    }
}
