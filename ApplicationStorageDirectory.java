package com.darras.myapplication;

import android.content.ContextWrapper;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class ApplicationStorageDirectory
{
    static File DIR = null;
    static String PATH = null;

    static public void createDirectoryIfNeeded(ContextWrapper context)
    {
        DIR = context.getExternalFilesDir(null);
        PATH = ApplicationStorageDirectory.DIR.getAbsolutePath();
        Log.e("WALID", ApplicationStorageDirectory.PATH);
        if (!DIR.exists())
        {
            Log.i("WALID", DIR.getAbsolutePath());
            Log.i("WALID", PATH);
            Log.i("WALID", "Le dossier n'existe pas j'essaye de le creer");
            DIR.setWritable(true);
            DIR.setReadable(true);
            boolean success = DIR.mkdirs();
            if (!success)
            {
                Log.e("WALID", "J'ai pas reussi a creer le dossier");
            }
        }
        createConfigFileIfNeeded();
    }

    private static void createConfigFileIfNeeded()
    {
        if (ConfigFileChantiers.ConfigFile == null)
        {
            ConfigFileChantiers.ConfigFile = new File(DIR, ConfigFileChantiers.ConfigFileName);
        }
        if (ConfigFileChantiers.TempConfigFile == null)
        {
            ConfigFileChantiers.TempConfigFile = new File(DIR, ConfigFileChantiers.TempConfigFileName);
        }

        if (DIR.exists() && !ConfigFileChantiers.ConfigFile.exists()) {
            ConfigFileChantiers.ConfigFile.setWritable(true);
            ConfigFileChantiers.ConfigFile.setReadable(true);
            try {
                ConfigFileChantiers.ConfigFile.createNewFile();
            } catch (IOException e) {
                Log.e("WALID", "Jai pas reussi a creer " + ConfigFileChantiers.ConfigFileName);
            }
        }

        if (DIR.exists() && !ConfigFileChantiers.TempConfigFile.exists()) {
            ConfigFileChantiers.TempConfigFile.setWritable(true);
            ConfigFileChantiers.TempConfigFile.setReadable(true);
            try {
                ConfigFileChantiers.TempConfigFile.createNewFile();
            }
            catch (IOException e)
            {
                Log.e("WALID", "Jai pas reussi a creer " + ConfigFileChantiers.TempConfigFileName);
            }
        }




    }
}
