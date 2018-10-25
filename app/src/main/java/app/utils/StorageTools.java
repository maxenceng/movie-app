package app.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class StorageTools {
    public static void writeFile(Context context, String fileName, String stringToWrite) {
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(fileName, context.MODE_PRIVATE);
            outputStream.write(stringToWrite.getBytes());
            outputStream.close();
            Log.i("writeFile_LOG", stringToWrite + " written into " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static FileInputStream getFileContent(Context context, String filename, String defaultValue) {
        FileInputStream fileInputStream = null;
        if (fileExists(context, filename)) {
            Log.i("getFileContent_LOG", filename + " exists");
            try {
                fileInputStream = context.openFileInput(filename);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Log.i("getFileContent_LOG", filename + " doesn't exists");
            writeDefaultValue(context, filename, defaultValue);
            try {
                fileInputStream = context.openFileInput(filename);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return fileInputStream;
    }

    public static boolean fileExists(Context context, String filename) {
        File file = context.getFileStreamPath(filename);
        if (file == null || !file.exists()) {
            return false;
        }
        return true;
    }

    public static String getStringFromFile(FileInputStream fileInputStream) {
        // We now read the file
        Log.i("getStringFromFile_LOG", "Just got the string from the file");
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String lineData = null;
        try {
            lineData = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lineData;
    }

    public static void writeDefaultValue(Context context, String filename, String defaultValue) {
        FileOutputStream outputStreamNoExist = null;
        try {
            outputStreamNoExist = context.openFileOutput(filename, context.MODE_PRIVATE);
            // We write the empty list in the file
            outputStreamNoExist.write(defaultValue.getBytes());
            outputStreamNoExist.close();
            Log.i("writeDefaultValue_LOG", filename + " was created with default value " + defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
