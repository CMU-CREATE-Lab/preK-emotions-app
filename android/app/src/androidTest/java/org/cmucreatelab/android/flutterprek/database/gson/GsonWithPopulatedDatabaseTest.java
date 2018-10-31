package org.cmucreatelab.android.flutterprek.database.gson;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.DateTypeAdapter;
import org.cmucreatelab.android.flutterprek.database.GsonDatabaseParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class GsonWithPopulatedDatabaseTest {

    private AppDatabase db;


    private Gson buildGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new DateTypeAdapter());
        return builder.create();
    }


    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                // Allowing main thread queries, just for testing.
                .allowMainThreadQueries()
                .build();
    }


    @After
    public void closeDb() {
        db.close();
    }


    @Test
    public void populateDatabaseFromGsonDbSeed() throws IOException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        Gson gson = buildGson();

        // build object and populate DB
        InputStream inputStream = appContext.getAssets().open("DbSeed.json");
        GsonDatabaseParser gsonDatabaseParser = gson.fromJson(new InputStreamReader(inputStream), GsonDatabaseParser.class);
        gsonDatabaseParser.populateDatabase(db);
    }


    @Test
    public void generateParserFromPopulatedDatabase() throws IOException, InterruptedException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        JsonParser parser = new JsonParser();
        Gson gson = buildGson();

        // build object and populate DB
        InputStream inputStream = appContext.getAssets().open("DbSeed.json");
        GsonDatabaseParser gsonDatabaseParser = gson.fromJson(new InputStreamReader(inputStream), GsonDatabaseParser.class);
        gsonDatabaseParser.populateDatabase(db);

        // store as json element
        JsonElement e1 = parser.parse(buildGson().toJson(gsonDatabaseParser, GsonDatabaseParser.class));
        // build second json element from DB
        JsonElement e2 = parser.parse(buildGson().toJson(GsonDatabaseParser.fromDb(db), GsonDatabaseParser.class));
        // compare
        assertEquals(e1, e2);
    }

}
