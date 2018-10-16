package org.cmucreatelab.android.flutterprek.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.framework.FrameworkSQLiteOpenHelperFactory;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.testing.MigrationTestHelper;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import static org.cmucreatelab.android.flutterprek.database.AppDatabase.MIGRATION_1_2;
import static org.cmucreatelab.android.flutterprek.database.AppDatabase.MIGRATION_2_3;
import static org.cmucreatelab.android.flutterprek.database.AppDatabase.MIGRATION_3_4;
import static org.cmucreatelab.android.flutterprek.database.AppDatabase.MIGRATION_4_5;
import static org.junit.Assert.assertEquals;

/**
 * Created by tasota on 9/13/2018.
 *
 * MigrationTest
 *
 * Unit tests for migrations on {@link AppDatabase}.
 */
@RunWith(AndroidJUnit4.class)
public class MigrationTest {

    private static final String TEST_DB_NAME = "test-db";
    private static final String classroomName = "The Migration Test Classroom";
    private static final String studentName = "Migrated Student";

    // Helper for creating Room databases and migrations
    @Rule
    public MigrationTestHelper migrationTestHelper =
            new MigrationTestHelper(InstrumentationRegistry.getInstrumentation(),
                    AppDatabase.class.getCanonicalName(),
                    new FrameworkSQLiteOpenHelperFactory());


    @Test
    public void migrationFrom1To2_containsCorrectData() throws IOException, InterruptedException {
        // Create the database with version 1
        SupportSQLiteDatabase db = migrationTestHelper.createDatabase(TEST_DB_NAME, 1);
        // Insert some data
        insertClassroom(1, classroomName, db);
        //Prepare for the next version
        db.close();

        // Re-open the database with version 2 and provide migrations
        migrationTestHelper.runMigrationsAndValidate(TEST_DB_NAME, 2, true,
                MIGRATION_1_2);

        // MigrationTestHelper automatically verifies the schema changes, but not the data validity
        // Validate that the data was migrated properly.
        List<Classroom> allClassrooms = LiveDataTestUtil.getValue(getMigratedRoomDatabase().classroomDAO().getAllClassrooms());
        Classroom dbClassroom = allClassrooms.get(0);
        assertEquals(dbClassroom.getUuid(), "1");
        assertEquals(dbClassroom.getName(), classroomName);
    }


    @Test
    public void migrationFrom2To3_containsCorrectData() throws IOException, InterruptedException {
        // Create the database with version 2
        SupportSQLiteDatabase db = migrationTestHelper.createDatabase(TEST_DB_NAME, 2);
        // Insert some data
        insertClassroom("classroom_1", classroomName, db);
        insertStudent("student_1", studentName, db);
        //Prepare for the next version
        db.close();

        // Re-open the database with version 2 and provide migrations
        migrationTestHelper.runMigrationsAndValidate(TEST_DB_NAME, 3, true,
                MIGRATION_2_3);

        // MigrationTestHelper automatically verifies the schema changes, but not the data validity
        // Validate that the data was migrated properly.
        List<Classroom> allClassrooms = LiveDataTestUtil.getValue(getMigratedRoomDatabase().classroomDAO().getAllClassrooms());
        Classroom dbClassroom = allClassrooms.get(0);
        assertEquals(dbClassroom.getUuid(), "classroom_1");
        assertEquals(dbClassroom.getName(), classroomName);
        List<Student> allStudents = LiveDataTestUtil.getValue(getMigratedRoomDatabase().studentDAO().getAllStudents());
        Student dbStudent = allStudents .get(0);
        assertEquals(dbStudent.getUuid(), "student_1");
        assertEquals(dbStudent.getName(), studentName);
        // Migration should have added student to first classroom
        assertEquals(dbStudent.getClassroomUuid(), "classroom_1");
    }


    @Test
    public void startInVersion2_containsCorrectData() throws IOException, InterruptedException {
        // Create the database with version 4
        SupportSQLiteDatabase db = migrationTestHelper.createDatabase(TEST_DB_NAME, 2);
        // insert some data
        String uuid = "someuuid";
        insertClassroom(uuid, classroomName, db);
        db.close();

        // Get the latest, migrated, version of the database
        AppDatabase appDatabase = getMigratedRoomDatabase();

        // verify that the data is correct
        List<Classroom> allClassrooms = LiveDataTestUtil.getValue(appDatabase.classroomDAO().getAllClassrooms());
        Classroom dbClassroom = allClassrooms.get(0);
        assertEquals(dbClassroom.getUuid(), uuid);
        assertEquals(dbClassroom.getName(), classroomName);
    }


    @Test
    public void startInVersion3_containsCorrectData() throws IOException, InterruptedException {
        // Create the database with version 4
        SupportSQLiteDatabase db = migrationTestHelper.createDatabase(TEST_DB_NAME, 3);
        // insert some data
        String classroomUuid = "classroom_1", studentUuid = "student_1";
        insertClassroom(classroomUuid, classroomName, db);
        insertStudent(studentUuid, studentName, classroomUuid, db);
        db.close();

        // Get the latest, migrated, version of the database
        AppDatabase appDatabase = getMigratedRoomDatabase();

        // verify that the data is correct
        List<Classroom> allClassrooms = LiveDataTestUtil.getValue(appDatabase.classroomDAO().getAllClassrooms());
        Classroom dbClassroom = allClassrooms.get(0);
        assertEquals(dbClassroom.getUuid(), classroomUuid);
        assertEquals(dbClassroom.getName(), classroomName);
        List<Student> allStudents = LiveDataTestUtil.getValue(appDatabase.studentDAO().getAllStudents());
        Student dbStudent = allStudents .get(0);
        assertEquals(dbStudent.getUuid(), studentUuid);
        assertEquals(dbStudent.getName(), studentName);
        assertEquals(dbStudent.getClassroomUuid(), classroomUuid);
    }


    private AppDatabase getMigratedRoomDatabase() {
        AppDatabase database = Room.databaseBuilder(InstrumentationRegistry.getTargetContext(),
                AppDatabase.class, TEST_DB_NAME)
                .addMigrations(MIGRATION_1_2,MIGRATION_2_3,MIGRATION_3_4,MIGRATION_4_5)
                .build();
        // close the database and release any stream resources when the test finishes
        migrationTestHelper.closeWhenFinished(database);
        return database;
    }


    private void insertClassroom(int id, String name, SupportSQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("name", name);

        db.insert("classrooms", SQLiteDatabase.CONFLICT_REPLACE, values);
    }


    private void insertClassroom(String uuid, String name, SupportSQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("uuid", uuid);
        values.put("name", name);

        db.insert("classrooms", SQLiteDatabase.CONFLICT_REPLACE, values);
    }


    private void insertStudent(String uuid, String name, SupportSQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("uuid", uuid);
        values.put("name", name);

        db.insert("students", SQLiteDatabase.CONFLICT_REPLACE, values);
    }


    private void insertStudent(String uuid, String name, String classroomUuid, SupportSQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("uuid", uuid);
        values.put("name", name);
        values.put("classroom_uuid", classroomUuid);

        db.insert("students", SQLiteDatabase.CONFLICT_REPLACE, values);
    }

}
