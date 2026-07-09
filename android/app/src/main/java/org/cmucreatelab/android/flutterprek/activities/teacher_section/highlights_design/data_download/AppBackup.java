package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.data_download;

import android.content.Context;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class AppBackup {

    public static void createBackup(Context context, OutputStream outputStream)
            throws IOException {

        try (ZipOutputStream zos =
                     new ZipOutputStream(new BufferedOutputStream(outputStream))) {

            backupDirectory(context.getFilesDir(), "files", zos);

            File dbDir = context.getDatabasePath("dummy").getParentFile();
            if (dbDir != null && dbDir.exists()) {
                backupDirectory(dbDir, "databases", zos);
            }

            File prefsDir = new File(
                    context.getApplicationInfo().dataDir,
                    "shared_prefs");

            if (prefsDir.exists()) {
                backupDirectory(prefsDir, "shared_prefs", zos);
            }
        }
    }

    public static void createBackup(Context context, File destination)
            throws IOException {

        ZipOutputStream zos =
                new ZipOutputStream(new FileOutputStream(destination));

        backupDirectory(context.getFilesDir(), "files", zos);

        File dbDir = context.getDatabasePath("dummy").getParentFile();
        if (dbDir != null)
            backupDirectory(dbDir, "databases", zos);

        File prefsDir = new File(context.getApplicationInfo().dataDir,
                "shared_prefs");
        if (prefsDir.exists())
            backupDirectory(prefsDir, "shared_prefs", zos);

        zos.close();
    }

    private static void backupDirectory(File dir,
                                        String path,
                                        ZipOutputStream zos)
            throws IOException {

        File[] files = dir.listFiles();
        if (files == null)
            return;

        for (File file : files) {

            String entryName = path + "/" + file.getName();

            if (file.isDirectory()) {
                backupDirectory(file, entryName, zos);
            } else {

                zos.putNextEntry(new ZipEntry(entryName));

                FileInputStream fis = new FileInputStream(file);

                byte[] buffer = new byte[8192];
                int len;

                while ((len = fis.read(buffer)) > 0)
                    zos.write(buffer, 0, len);

                fis.close();

                zos.closeEntry();
            }
        }
    }
}