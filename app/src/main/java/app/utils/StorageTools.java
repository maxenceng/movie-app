package app.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Storage tools provides the functions to read/write a file
 */
public class StorageTools {
    // Function that writes the content sent to a file on the device
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

    // Gets the content from the file
    public static FileInputStream getFileContent(Context context, String filename, String defaultValue) {
        FileInputStream fileInputStream = null;
        if (fileExists(context, filename)) {
            try {
                fileInputStream = context.openFileInput(filename);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            writeFile(context, filename, defaultValue);
            try {
                fileInputStream = context.openFileInput(filename);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return fileInputStream;
    }

    // Checks if the file already exists
    private static boolean fileExists(Context context, String filename) {
        File file = context.getFileStreamPath(filename);
        return file != null && file.exists();
    }

    // Transforms the stream received into a string
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
}
