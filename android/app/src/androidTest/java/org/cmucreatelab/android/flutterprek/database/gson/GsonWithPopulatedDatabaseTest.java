package org.cmucreatelab.android.flutterprek.database.gson;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.GsonDatabaseParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@RunWith(AndroidJUnit4.class)
public class GsonWithPopulatedDatabaseTest {

    private AppDatabase db;


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
        JsonParser parser = new JsonParser();

        // build object and check attributes for correctness
        InputStream inputStream = appContext.getAssets().open("DbSeed.json");
        JsonElement e1 = parser.parse(new InputStreamReader(inputStream));
        GsonDatabaseParser gsonDatabaseParser = new Gson().fromJson(e1, GsonDatabaseParser.class);

        gsonDatabaseParser.populateDatabase(db);
    }

}
