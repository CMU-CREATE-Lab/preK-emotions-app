package org.cmucreatelab.android.flutterprek.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import org.cmucreatelab.android.flutterprek.database.models.Classroom;
import org.cmucreatelab.android.flutterprek.database.models.ClassroomDAO;

/**
 * Created by tasota on 9/6/2018.
 *
 * AppDatabase
 *
 * Implementation of a room database for the application. See Room persistence library documentation for details:
 *   https://developer.android.com/training/data-storage/room/accessing-data
 */
@Database(entities = {Classroom.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static String dbName = "flutterprek.sqlite3";
    private static RoomDatabase.Callback callback = new RoomDatabase.Callback() {

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            Log.i("flutterprek", "RoomDatabase.Callback.onCreate");
            super.onCreate(db);
            new PopulateDbAsync(instance).execute();
        }

        @Override
        public void onOpen (@NonNull SupportSQLiteDatabase db){
            Log.i("flutterprek", "RoomDatabase.Callback.onOpen");
            super.onOpen(db);
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final ClassroomDAO dao;

        PopulateDbAsync(AppDatabase db) {
            dao = db.classroomDAO();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            Log.i("flutterprek", "creating flutterprek DB");
            Classroom classroom = new Classroom();
            classroom.setId(1);
            classroom.setName("First Classroom");
            dao.insert(classroom);

            return null;
        }
    }

    // Singleton Pattern

    private static AppDatabase instance;


    public static AppDatabase getInstance(final Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, dbName).addCallback(callback).build();
                }
            }
        }
        return instance;
    }


    public abstract ClassroomDAO classroomDAO();

}
