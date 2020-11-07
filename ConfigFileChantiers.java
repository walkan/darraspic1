package com.darras.myapplication;

import android.content.ContextWrapper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class ConfigFileChantiers
{
    static final String ConfigFileName = "liste_chantiers.txt";
    static File ConfigFile = null;
    static final String TempConfigFileName = "myTempFile.txt";
    static File TempConfigFile = null;

    static public void addAddress(final String address) throws IOException
    {
        try (FileWriter fw = new FileWriter(ConfigFile, true))
        {
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(address);
            bw.newLine();
            bw.close();
        }
        catch (Exception e)
        {
            Log.e("WALID", "Failed to add address to list of chantiers", e);
            throw e;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    static public void deleteAddress(final String address) throws IOException
    {

        BufferedReader reader = new BufferedReader(new FileReader(ConfigFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(TempConfigFile,true));

        String lineToRemove = address;
        String currentLine;

        while((currentLine = reader.readLine()) != null) {
            // trim newline when comparing with lineToRemove
            String trimmedLine = currentLine.trim();
            if(trimmedLine.equals(lineToRemove)) continue;
            writer.write(currentLine + System.getProperty("line.separator"));
        }
        writer.close();
        reader.close();
        boolean successful = TempConfigFile.renameTo(ConfigFile);
        ListeChantiers.deleteChantier(address);
    }



    static public ArrayList<String> readListOfChantiers(ContextWrapper context)
    {
        ArrayList<String> listOfAddresses = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(ConfigFile)))
        {
            String line = reader.readLine();
            while (line != null) {
                listOfAddresses.add(line);
                Log.i("WALID", "chantier trouvé : " + line );
                line = reader.readLine(); // read next line
            }
        }
        catch (Exception e)
        {
            Log.e("WALID", "J'ai pas reussi a lire " + ConfigFileName);
        }

        return listOfAddresses;
    }
}
