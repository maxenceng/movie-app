package app.utils;

import android.content.Context;

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static FileInputStream getFileContent(Context context, String filename, String defaultValue) {
        FileInputStream fileInputStream = null;
        if (fileExists(context, filename)) {
            try {
                fileInputStream = context.openFileInput(filename);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            writeDefaultValue(context, filename, defaultValue);
            try {
                fileInputStream = context.openFileInput(filename);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return fileInputStream;
    }

    private static boolean fileExists(Context context, String filename) {
        File file = context.getFileStreamPath(filename);
        return file != null && file.exists();
    }

    public static String getStringFromFile(FileInputStream fileInputStream) {
        // We now read the file
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

    private static void writeDefaultValue(Context context, String filename, String defaultValue) {
        FileOutputStream outputStreamNoExist;
        try {
            outputStreamNoExist = context.openFileOutput(filename, context.MODE_PRIVATE);
            // We write the empty list in the file
            outputStreamNoExist.write(defaultValue.getBytes());
            outputStreamNoExist.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
