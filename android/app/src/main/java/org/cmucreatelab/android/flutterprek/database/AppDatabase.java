package org.cmucreatelab.android.flutterprek.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import org.cmucreatelab.android.flutterprek.database.models.Classroom;
import org.cmucreatelab.android.flutterprek.database.models.ClassroomDAO;
import org.cmucreatelab.android.flutterprek.database.models.Student;
import org.cmucreatelab.android.flutterprek.database.models.StudentDAO;

/**
 * Created by tasota on 9/6/2018.
 *
 * AppDatabase
 *
 * Implementation of a room database for the application. See Room persistence library documentation for details:
 *   https://developer.android.com/training/data-storage/room/accessing-data
 */
@Database(entities = {Classroom.class, Student.class}, version = 2)
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

        private final ClassroomDAO classroomDAO;
        private final StudentDAO studentDAO;

        PopulateDbAsync(AppDatabase db) {
            classroomDAO = db.classroomDAO();
            studentDAO = db.studentDAO();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            Log.i("flutterprek", "creating flutterprek DB");
            Classroom classroom = new Classroom("classroom_1", "First Classroom");
            classroomDAO.insert(classroom);
            Student student = new Student("student_1", "Test Student");
            student.setNotes("This is a student for testing.");
            studentDAO.insert(student);

            return null;
        }
    }

    /**
     * Migrate from:
     * version 1 - using Room where the {@link Classroom#uuid} is an int
     * to
     * version 2 - using Room where the {@link Classroom#uuid} is a String
     */
    @VisibleForTesting
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Sample migration (Note: to see v1 you need to go back in git history)
            //
            // Changing the type of a column in SQLite is not directly supported, so this is what we need
            // to do:
            // Create new classrooms table
            database.execSQL(
                    "CREATE TABLE classrooms_new (uuid TEXT NOT NULL,"
                            + "name TEXT NOT NULL,"
                            + "PRIMARY KEY(uuid))");
            // Copy the data
            database.execSQL("INSERT INTO classrooms_new (uuid, name) "
                    + "SELECT id, name "
                    + "FROM classrooms");
            // Remove the old table
            database.execSQL("DROP TABLE classrooms");
            // Change the table name to the correct one
            database.execSQL("ALTER TABLE classrooms_new RENAME TO classrooms");
            // Create students table
            database.execSQL(
                    "CREATE TABLE students (uuid TEXT NOT NULL,"
                            + "name TEXT NOT NULL,"
                            + "notes TEXT,"
                            + "PRIMARY KEY(uuid))");
        }
    };

    // Singleton Pattern

    private static AppDatabase instance;


    public static AppDatabase getInstance(final Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, dbName)
                            .addMigrations(MIGRATION_1_2)
                            .addCallback(callback)
                            .build();
                }
            }
        }
        return instance;
    }


    public abstract ClassroomDAO classroomDAO();


    public abstract StudentDAO studentDAO();

}
