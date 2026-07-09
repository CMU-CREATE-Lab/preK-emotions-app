package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.data_download;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.HighlightsDesignActivityWithHeaderAndDrawer;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.views.HighlightsViewDrawer;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;

public class HighlightsDesignDataDownloadMainActivity extends HighlightsDesignActivityWithHeaderAndDrawer {


    private static final String filenameDescriptor = "MindfulNestAppData";


    private String generateTimestamp() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HHmm", Locale.US);
        String timestamp = formatter.format(new Date());
        return timestamp;
    }

    private String generateFilename() {
        // yyyy-MM-dd-HHmm-MindfulNestAppBackup.zip
        return String.format("%s-%s.zip", generateTimestamp(), filenameDescriptor);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpDrawer();
        getDrawerHighlights().setHighlighted(HighlightsViewDrawer.Row.DATA_SHARING);
        setBackNavigationForDrawer(true, "Back", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.buttonGenerateArchive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Executors.newSingleThreadExecutor().execute(() -> {

                    try {

                        String filename = generateFilename();

                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Downloads.DISPLAY_NAME, filename);
                        values.put(MediaStore.Downloads.MIME_TYPE, "application/zip");
                        values.put(MediaStore.Downloads.IS_PENDING, 1);

                        Uri uri = getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);

                        if (uri == null) {
                            throw new IOException("Unable to create download.");
                        }

                        try (OutputStream out = getContentResolver().openOutputStream(uri)) {
                            if (out == null) {
                                throw new IOException("Unable to open output stream.");
                            }
                            AppBackup.createBackup(HighlightsDesignDataDownloadMainActivity.this, out);
                        }

                        values.clear();
                        values.put(MediaStore.Downloads.IS_PENDING, 0);
                        getContentResolver().update(uri, values, null, null);

                        runOnUiThread(() -> {
                            Toast.makeText(HighlightsDesignDataDownloadMainActivity.this, String.format("Backup saved to Downloads/%s", filename), Toast.LENGTH_LONG).show();
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(() -> {
                            Toast.makeText(HighlightsDesignDataDownloadMainActivity.this, "Backup failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        });
                    }

                });

            }
        });
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._highlights_design__activity_data_download_main;
    }

}
