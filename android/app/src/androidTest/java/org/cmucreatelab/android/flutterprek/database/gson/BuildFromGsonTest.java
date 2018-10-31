package org.cmucreatelab.android.flutterprek.database.gson;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.cmucreatelab.android.flutterprek.database.DateTypeAdapter;
import org.cmucreatelab.android.flutterprek.database.GsonDatabaseParser;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class BuildFromGsonTest {

    private static String JSON_FILE_NAME = "buildFromGsonTest.json";


    private Gson buildGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new DateTypeAdapter());
        return builder.create();
    }


    @Test
    public void buildObjectFromGsonDbSeed() throws IOException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        JsonParser parser = new JsonParser();
        Gson gson = buildGson();

        // build object and check attributes for correctness
        InputStream inputStream = appContext.getAssets().open("DbSeed.json");
        JsonElement e1 = parser.parse(new InputStreamReader(inputStream));
        GsonDatabaseParser gsonDatabaseParser = gson.fromJson(e1, GsonDatabaseParser.class);
        assertEquals(2, gsonDatabaseParser.classrooms.size());

        // check that the constructed object is equal to the original JSON
        String jsonString = gson.toJson(gsonDatabaseParser, GsonDatabaseParser.class);
        JsonElement e2 = parser.parse(jsonString);
        assertEquals(e1, e2);
    }


    @Test
    public void writeJsonFromDbSeedToInternalStorage() throws IOException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        Gson gson = buildGson();

        // build object
        InputStream inputStream = appContext.getAssets().open("DbSeed.json");
        GsonDatabaseParser gsonDatabaseParser = gson.fromJson(new InputStreamReader(inputStream), GsonDatabaseParser.class);

        // convert to json and write to file
        String json = gson.toJson(gsonDatabaseParser);
        FileOutputStream outputStream = appContext.openFileOutput(JSON_FILE_NAME, Context.MODE_PRIVATE);
        outputStream.write(json.getBytes());
        outputStream.close();
    }

}
