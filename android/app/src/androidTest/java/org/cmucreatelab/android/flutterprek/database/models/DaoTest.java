package org.cmucreatelab.android.flutterprek.database.models;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.InstantTaskExecutorRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 * Created by tasota on 10/17/2018.
 *
 * DaoTest
 *
 * Avoid duplicate code in the DAO tests.
 */
public abstract class DaoTest {

    private AppDatabase db;

    @Rule public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    @Rule public final ExpectedException expectedException = ExpectedException.none();


    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                // Allowing main thread queries, just for testing.
                .allowMainThreadQueries()
                .build();
        initializeDaos();
    }


    @After
    public void closeDb() {
        db.close();
    }


    public AppDatabase getDb() {
        return db;
    }


    public abstract void initializeDaos();

}
