package org.cmucreatelab.android.flutterprek.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;
import org.cmucreatelab.android.flutterprek.database.models.classroom.ClassroomDAO;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;
import org.cmucreatelab.android.flutterprek.database.models.student.StudentDAO;

import java.util.UUID;

/**
 * Created by tasota on 9/6/2018.
 *
 * AppDatabase
 *
 * Implementation of a room database for the application. See Room persistence library documentation for details:
 *   https://developer.android.com/training/data-storage/room/accessing-data
 */
@Database(entities = {Classroom.class, Student.class}, version = 3)
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
            Student student = new Student("student_1", "Test Student", classroom.getUuid());
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

    /**
     * Migrate from:
     * version 2
     * to
     * version 3 - Add foreign key for {@link Classroom} in {@link Student}.
     */
    @VisibleForTesting
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Create new classrooms table
            database.execSQL(
                    "ALTER TABLE students ADD COLUMN classroom_uuid TEXT");

            // Update every student to be a part of the first classroom
            String classroomUuid;
            Cursor queryClassrooms = database.query("SELECT * FROM classrooms");
            if (queryClassrooms.getCount() > 0) {
                // use the first classroom to add students to ...
                queryClassrooms.moveToFirst();
                classroomUuid = queryClassrooms.getString(queryClassrooms.getColumnIndex("uuid"));
            } else {
                // ... or create a classroom if none exist
                classroomUuid = UUID.randomUUID().toString();
                ContentValues values = new ContentValues();
                values.put("uuid", classroomUuid);
                values.put("name", "My Classroom");

                database.insert("classrooms", SQLiteDatabase.CONFLICT_REPLACE, values);
            }
            Cursor queryStudents = database.query("SELECT * FROM students");
            queryStudents.moveToFirst();
            while(!queryStudents.isAfterLast()) {
                ContentValues values = new ContentValues();
                DatabaseUtils.cursorRowToContentValues(queryStudents,values);
                values.put("classroom_uuid", classroomUuid);
                database.update("students", SQLiteDatabase.CONFLICT_REPLACE, values, "", null);
                queryStudents.moveToNext();
            }

            // Force NOT NULL on column classroom_uuid (and define foreign key)
            database.execSQL(
                    "CREATE TABLE students_new(`uuid` TEXT NOT NULL, `name` TEXT NOT NULL, `notes` TEXT, `classroom_uuid` TEXT NOT NULL, PRIMARY KEY(`uuid`), FOREIGN KEY(`classroom_uuid`) REFERENCES `classrooms`(`uuid`) ON UPDATE NO ACTION ON DELETE CASCADE)");
            database.execSQL(
                    "INSERT INTO students_new(uuid,name,notes,classroom_uuid) SELECT uuid,name,notes,classroom_uuid FROM students");
            database.execSQL(
                    "DROP TABLE students");
            database.execSQL(
                    "ALTER TABLE students_new RENAME TO students");
        }
    };

    // Singleton Pattern

    private static AppDatabase instance;


    public static AppDatabase getInstance(final Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, dbName)
                            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
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
