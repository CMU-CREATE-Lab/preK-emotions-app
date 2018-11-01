package org.cmucreatelab.android.flutterprek.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.cmucreatelab.android.flutterprek.database.gson.DateTypeAdapter;
import org.cmucreatelab.android.flutterprek.database.gson.GsonDatabaseParser;
import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;
import org.cmucreatelab.android.flutterprek.database.models.classroom.ClassroomDAO;
import org.cmucreatelab.android.flutterprek.database.models.coping_skill.CopingSkill;
import org.cmucreatelab.android.flutterprek.database.models.coping_skill.CopingSkillDAO;
import org.cmucreatelab.android.flutterprek.database.models.customization.Customization;
import org.cmucreatelab.android.flutterprek.database.models.customization.CustomizationDAO;
import org.cmucreatelab.android.flutterprek.database.models.db_file.DbFile;
import org.cmucreatelab.android.flutterprek.database.models.db_file.DbFileDAO;
import org.cmucreatelab.android.flutterprek.database.models.emotion.Emotion;
import org.cmucreatelab.android.flutterprek.database.models.emotion.EmotionDAO;
import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.EmotionCopingSkill;
import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.IntermediateTablesDAO;
import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.ItineraryItem;
import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.SessionCopingSkill;
import org.cmucreatelab.android.flutterprek.database.models.session.Session;
import org.cmucreatelab.android.flutterprek.database.models.session.SessionDAO;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;
import org.cmucreatelab.android.flutterprek.database.models.student.StudentDAO;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.UUID;

/**
 * Created by tasota on 9/6/2018.
 *
 * AppDatabase
 *
 * Implementation of a room database for the application. See Room persistence library documentation for details:
 *   https://developer.android.com/training/data-storage/room/accessing-data
 */
@Database(entities = {
        Classroom.class,
        Student.class,
        CopingSkill.class,
        Customization.class,
        DbFile.class,
        Emotion.class,
        EmotionCopingSkill.class,
        ItineraryItem.class,
        SessionCopingSkill.class,
        Session.class
}, version = 5)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static String dbName = "flutterprek.sqlite3";
    private static RoomDatabase.Callback callback = new RoomDatabase.Callback() {

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            Log.i("flutterprek", "RoomDatabase.Callback.onCreate");
            super.onCreate(db);
            new PopulateDbAsync().execute();
        }

        @Override
        public void onOpen (@NonNull SupportSQLiteDatabase db){
            Log.i("flutterprek", "RoomDatabase.Callback.onOpen");
            super.onOpen(db);
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(final Void... params) {
            Log.i("flutterprek", "creating flutterprek DB");
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new DateTypeAdapter());
            Gson gson = builder.create();

            try {
                InputStream inputStream = appContext.getAssets().open("DbSeed.json");
                GsonDatabaseParser gsonDatabaseParser = gson.fromJson(new InputStreamReader(inputStream), GsonDatabaseParser.class);
                gsonDatabaseParser.populateDatabase(instance);
            } catch (IOException e) {
                Log.e("flutterprek", "Failed to create DB from JSON file!");
                e.printStackTrace();
            }

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

    /**
     * Migrate from:
     * version 3
     * to
     * version 4 - Add indices on uuid for classrooms/students tables, add pictureFileUuid to {@link Student}.
     */
    @VisibleForTesting
    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // modify students table
            database.execSQL("ALTER TABLE `students` ADD COLUMN `picture_file_uuid` TEXT");
            database.execSQL("CREATE INDEX `index_students_classrooms_uuid` ON `students` (`classroom_uuid`)");
        }
    };

    /**
     * Migrate from:
     * version 4
     * to
     * version 5 - Add new tables for: {@link CopingSkill}, {@link Customization}, {@link DbFile}, {@link Emotion}, {@link EmotionCopingSkill}, {@link ItineraryItem}, {@link SessionCopingSkill}, {@link Session}.
     */
    @VisibleForTesting
    static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // coping_skills table
            database.execSQL("CREATE TABLE IF NOT EXISTS `coping_skills` (`uuid` TEXT NOT NULL, `owner_uuid` TEXT, `name` TEXT NOT NULL, `image_file_uuid` TEXT, PRIMARY KEY(`uuid`))");
            // coping_skills indices
            database.execSQL("CREATE  INDEX `index_coping_skills_owner_uuid` ON `coping_skills` (`owner_uuid`)");
            database.execSQL("CREATE  INDEX `index_coping_skills_image_file_uuid` ON `coping_skills` (`image_file_uuid`)");

            // customizations table
            database.execSQL("CREATE TABLE IF NOT EXISTS `customizations` (`uuid` TEXT NOT NULL, `owner_uuid` TEXT, `based_on_uuid` TEXT, `key` TEXT NOT NULL, `value` TEXT NOT NULL, PRIMARY KEY(`uuid`))");
            // customizations indices
            database.execSQL("CREATE  INDEX `index_customizations_owner_uuid` ON `customizations` (`owner_uuid`)");
            database.execSQL("CREATE  INDEX `index_customizations_based_on_uuid` ON `customizations` (`based_on_uuid`)");

            // db_files table
            database.execSQL("CREATE TABLE IF NOT EXISTS `db_files` (`uuid` TEXT NOT NULL, `file_type` TEXT NOT NULL, `file_path` TEXT NOT NULL, PRIMARY KEY(`uuid`))");
            // db_files indices
            database.execSQL("CREATE  INDEX `index_db_files_file_type` ON `db_files` (`file_type`)");

            // emotions table
            database.execSQL("CREATE TABLE IF NOT EXISTS `emotions` (`uuid` TEXT NOT NULL, `owner_uuid` TEXT, `name` TEXT NOT NULL, `image_file_uuid` TEXT, PRIMARY KEY(`uuid`))");
            // emotions indices
            database.execSQL("CREATE  INDEX `index_emotions_owner_uuid` ON `emotions` (`owner_uuid`)");
            database.execSQL("CREATE  INDEX `index_emotions_image_file_uuid` ON `emotions` (`image_file_uuid`)");

            // emotions_coping_skills table
            database.execSQL("CREATE TABLE IF NOT EXISTS `emotions_coping_skills` (`uuid` TEXT NOT NULL, `owner_uuid` TEXT, `emotion_uuid` TEXT NOT NULL, `coping_skill_uuid` TEXT NOT NULL, PRIMARY KEY(`uuid`), FOREIGN KEY(`coping_skill_uuid`) REFERENCES `coping_skills`(`uuid`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`emotion_uuid`) REFERENCES `emotions`(`uuid`) ON UPDATE NO ACTION ON DELETE CASCADE )");
            // emotions_coping_skills indices
            database.execSQL("CREATE  INDEX `index_emotions_coping_skills_owner_uuid` ON `emotions_coping_skills` (`owner_uuid`)");
            database.execSQL("CREATE  INDEX `index_emotions_coping_skills_emotion_uuid` ON `emotions_coping_skills` (`emotion_uuid`)");
            database.execSQL("CREATE  INDEX `index_emotions_coping_skills_coping_skill_uuid` ON `emotions_coping_skills` (`coping_skill_uuid`)");

            // itinerary_items table
            database.execSQL("CREATE TABLE IF NOT EXISTS `itinerary_items` (`uuid` TEXT NOT NULL, `owner_uuid` TEXT NOT NULL, `sequence_id` INTEGER NOT NULL, `capability_id` TEXT NOT NULL, `capability_parameters` TEXT NOT NULL, PRIMARY KEY(`uuid`))");
            // itinerary_items indices
            database.execSQL("CREATE  INDEX `index_itinerary_items_owner_uuid` ON `itinerary_items` (`owner_uuid`)");
            database.execSQL("CREATE  INDEX `index_itinerary_items_capability_id` ON `itinerary_items` (`capability_id`)");

            // sessions_coping_skills tables
            database.execSQL("CREATE TABLE IF NOT EXISTS `sessions_coping_skills` (`uuid` TEXT NOT NULL, `session_uuid` TEXT NOT NULL, `coping_skill_uuid` TEXT NOT NULL, `started_at` INTEGER NOT NULL, PRIMARY KEY(`uuid`))");
            // sessions_coping_skills indices
            database.execSQL("CREATE  INDEX `index_sessions_coping_skills_session_uuid` ON `sessions_coping_skills` (`session_uuid`)");
            database.execSQL("CREATE  INDEX `index_sessions_coping_skills_coping_skill_uuid` ON `sessions_coping_skills` (`coping_skill_uuid`)");

            // sessions table
            database.execSQL("CREATE TABLE IF NOT EXISTS `sessions` (`uuid` TEXT NOT NULL, `student_uuid` TEXT NOT NULL, `started_at` INTEGER NOT NULL, `ended_at` INTEGER, `emotion_uuid` TEXT, PRIMARY KEY(`uuid`))");
            // sessions indices
            database.execSQL("CREATE  INDEX `index_sessions_student_uuid` ON `sessions` (`student_uuid`)");
            database.execSQL("CREATE  INDEX `index_sessions_emotion_uuid` ON `sessions` (`emotion_uuid`)");
        }
    };

    // Singleton Pattern

    private static AppDatabase instance;

    private static Context appContext;


    public static AppDatabase getInstance(final Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    appContext = context.getApplicationContext();
                    instance = Room.databaseBuilder(appContext, AppDatabase.class, dbName)
                            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5)
                            .addCallback(callback)
                            .build();
                }
            }
        }
        return instance;
    }


    public abstract ClassroomDAO classroomDAO();


    public abstract StudentDAO studentDAO();


    public abstract CopingSkillDAO copingSkillDAO();


    public abstract CustomizationDAO customizationDAO();


    public abstract DbFileDAO dbFileDAO();


    public abstract EmotionDAO emotionDAO();


    public abstract SessionDAO sessionDAO();


    public abstract IntermediateTablesDAO intermediateTablesDAO();

}
