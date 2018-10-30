package org.cmucreatelab.android.flutterprek.internal_storage;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class WriteFileTest {

    private static String TEST_FILE_NAME = "internal_storage_write_file_test.txt";


    @Test
    public void writeTestFile() throws IOException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        String timestamp = new Date().toString();
        FileOutputStream outputStream;

        outputStream = appContext.openFileOutput(TEST_FILE_NAME, Context.MODE_PRIVATE);
        outputStream.write(timestamp.getBytes());
        outputStream.close();
    }


    @Test
    public void deleteTestFile() throws IOException {
        writeTestFile();
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals(true, appContext.deleteFile(TEST_FILE_NAME));
    }

}
