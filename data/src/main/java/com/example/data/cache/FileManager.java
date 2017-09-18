package com.example.data.cache;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by plnc on 2017-06-08.
 */

@Singleton
public class FileManager {

    @Inject
    FileManager() {}

    void writeToFile(File file, String fileContent) {
        if(!file.exists()) {
            try {
                FileWriter writer = new FileWriter(file);
                writer.write(fileContent);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    String readFileContent(File file) {
        StringBuilder fileContentBuilder = new StringBuilder();
        if(file.exists()) {
            String strLine;
            try {
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                while((strLine = bufferedReader.readLine()) != null) {
                    fileContentBuilder.append(strLine).append("\n");
                }
                bufferedReader.close();
                fileReader.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        return fileContentBuilder.toString();
    }

    boolean exists(File file) { return file.exists(); }

    boolean cleanDirectory(File directory) {
        boolean result = false;
        if(directory.exists()) {
            for(File file : directory.listFiles()) {
                result = file.delete();
            }
        }
        return result;
    }

    void writeToPreference(Context context, String preferenceFileName, String key, long value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    long getFromPreference(Context context, String preferenceFileName, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key, 0);
    }
}

