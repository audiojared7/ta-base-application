package com.tecace.loggerta;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

public class FileLog {

    public static void log(String tag, String message, Context context) {
        try {
            int permissionStorage = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permissionStorage == PackageManager.PERMISSION_GRANTED) {
                String path = "Log";
                String fileNameTimeStamp = new SimpleDateFormat("dd-MM-yyyy",
                        Locale.getDefault()).format(new Date());
                String logTimeStamp = new SimpleDateFormat("E MMM dd yyyy 'at' hh:mm:ss:SSS aaa",
                        Locale.getDefault()).format(new Date());
                String fileName = fileNameTimeStamp + ".html";

                // Create file
                File file = generateFile(path, fileName);

                // If file created or exists save logs
                if (file != null) {
                    FileWriter writer = new FileWriter(file, true);
                    writer.append("<p style=\"background:lightgray;\"><strong "
                            + "style=\"background:lightblue;\">&nbsp&nbsp")
                            .append(logTimeStamp)
                            .append(" :&nbsp&nbsp</strong><strong>&nbsp&nbsp")
                            .append(tag)
                            .append("</strong> - ")
                            .append(message)
                            .append("</p>");
                    writer.flush();
                    writer.close();
                }
            }
        } catch (Exception e) {
            Timber.e(e,"Error while logging into file : " + e);
        }
    }

    /*  Helper method to create file*/
    @Nullable
    private static File generateFile(@NonNull String path, @NonNull String fileName) {
        File file = null;
        try {
            if (isExternalStorageAvailable()) {
                File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
                        BuildConfig.APPLICATION_ID + File.separator + path);

                boolean dirExists = true;

                if (!root.exists()) {
                    dirExists = root.mkdirs();
                }

                if (dirExists) {
                    file = new File(root, fileName);
                }
            }
        } catch (Exception e) {
            Timber.e(e,"Error while create log file : " + e);
        }
        return file;
    }

    /* Helper method to determine if external storage is available*/
    private static boolean isExternalStorageAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }
}
