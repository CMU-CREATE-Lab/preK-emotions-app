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
import org.cmucreatelab.android.flutterprek.database.models.daos.ClassroomDAO;

import java.util.List;

@Database(entities = {Classroom.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static String dbName = "flutterprek.sqlite3";


    public abstract ClassroomDAO classroomDAO();


    // singleton pattern

    private static AppDatabase instance;


    public static AppDatabase getDatabase(final Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, dbName).addCallback(callback).build();
                }
            }
        }
        return instance;
    }

    /**
     * Override the onOpen method to populate the database.
     * For this sample, we clear the database every time it is created or opened.
     */
    private static RoomDatabase.Callback callback = new RoomDatabase.Callback(){

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

    /**
     * Populate the database in the background.
     * If you want to start with more words, just add them.
     */
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final ClassroomDAO dao;

        PopulateDbAsync(AppDatabase db) {
            dao = db.classroomDAO();
        }

        @Override
        protected Void doInBackground(final Void... params) {
//            // Start the app with a clean database every time.
//            // Not needed if you only populate on creation.
//            mDao.deleteAll();
//
//            Word word = new Word("Hello");
//            mDao.insert(word);
//            word = new Word("World");
//            mDao.insert(word);
//            return null;
//            List<Classroom> classrooms = dao.getAllClassrooms().getValue();
//            //ViewModelProviders.of(this).get(ClassroomViewModel.class)
//
//            if (classrooms.size() > 0) {
//                Log.i("flutterprek", "not creating new classroom since at least 1 classroom entry already exists.");
//            } else {
//                Log.i("flutterprek", "creating flutterprek DB");
//                Classroom classroom = new Classroom();
//                classroom.setId(1);
//                classroom.setName("First Classroom");
//                dao.insert(classroom);
//            }

            Log.i("flutterprek", "creating flutterprek DB");
            Classroom classroom = new Classroom();
            classroom.setId(1);
            classroom.setName("First Classroom");
            dao.insert(classroom);

            return null;
        }
    }

}
