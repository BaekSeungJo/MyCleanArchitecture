package com.example.data.cache;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by plnc on 2017-06-08.
 */

public class FileManager {

    private FileManager() {
    }

    private static class LazyHolder {
        private static final FileManager INSTANCE = new FileManager();
    }

    public static FileManager getInstance() { return LazyHolder.INSTANCE; }

    public void writeToFile(File file, String fileContent) {
        if(!file.exists()) {
            try {
                FileWriter writer = new FileWriter(file);
                writer.write(fileContent);
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

            }
        }
    }

    public String readFileContent(File file) {
        StringBuilder fileContentBuilder = new StringBuilder();
        if(file.exists()) {
            String strLine;
            try {
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                while((strLine = bufferedReader.readLine()) != null) {
                    fileContentBuilder.append(strLine + "\n");
                }
                bufferedReader.close();
                fileReader.close();
            } catch(FileNotFoundException e) {
                e.printStackTrace();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        return fileContentBuilder.toString();
    }

    public boolean exists(File file) { return file.exists(); }

    public void cleanDirectory(File directory) {
        if(directory.exists()) {
            for(File file : directory.listFiles()) {
                file.delete();
            }
        }
    }

    public void writeToPreference(Context context, String preferenceFileName, String key, long value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public long getFromPreference(Context context, String preferenceFileName, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key, 0);
    }
}

