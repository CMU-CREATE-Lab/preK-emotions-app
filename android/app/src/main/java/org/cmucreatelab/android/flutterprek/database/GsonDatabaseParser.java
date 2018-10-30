package org.cmucreatelab.android.flutterprek.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;
import org.cmucreatelab.android.flutterprek.database.models.coping_skill.CopingSkill;
import org.cmucreatelab.android.flutterprek.database.models.customization.Customization;
import org.cmucreatelab.android.flutterprek.database.models.db_file.DbFile;
import org.cmucreatelab.android.flutterprek.database.models.emotion.Emotion;
import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.EmotionCopingSkill;
import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.ItineraryItem;
import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.SessionCopingSkill;
import org.cmucreatelab.android.flutterprek.database.models.session.Session;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GsonDatabaseParser {

    public List<Classroom> classrooms;

    public List<Student> students;

    public List<CopingSkill> copingSkills;

    public List<Customization> customizations;

    public List<DbFile> dbFiles;

    public List<Emotion> emotions;

    public List<Session> sessions;

    public List<EmotionCopingSkill> emotionsCopingSkills;

    public List<ItineraryItem> itineraryItems;

    public List<SessionCopingSkill> sessionsCopingSkills;


    public void populateDatabase(AppDatabase appDatabase) {
        appDatabase.classroomDAO().insert(classrooms);
        appDatabase.studentDAO().insert(students);
        appDatabase.copingSkillDAO().insert(copingSkills);
        appDatabase.customizationDAO().insert(customizations);
        appDatabase.dbFileDAO().insert(dbFiles);
        appDatabase.emotionDAO().insert(emotions);
        appDatabase.sessionDAO().insert(sessions);
        appDatabase.intermediateTablesDAO().insertEmotionCopingSkillList(emotionsCopingSkills);
        appDatabase.intermediateTablesDAO().insertItineraryItemList(itineraryItems);
        appDatabase.intermediateTablesDAO().insertSessionCopingSkillList(sessionsCopingSkills);
    }


    // NOTE: this tracks the number of observers when constructing objects from the database
    private static int NUMBER_OF_QUERIES_TO_OBSERVE = 10;


    public static GsonDatabaseParser fromDb(AppDatabase appDatabase) throws InterruptedException {
        final GsonDatabaseParser result = new GsonDatabaseParser();
        final CountDownLatch latch = new CountDownLatch(NUMBER_OF_QUERIES_TO_OBSERVE);

        // 1 classrooms
        final LiveData<List<Classroom>> classroomsLiveData = appDatabase.classroomDAO().getAllClassrooms();
        classroomsLiveData.observeForever(new Observer<List<Classroom>>() {
            @Override
            public void onChanged(@Nullable List<Classroom> o) {
                result.classrooms = o;
                latch.countDown();
                classroomsLiveData.removeObserver(this);
            }
        });
        // 2 students
        final LiveData<List<Student>> studentsLiveData = appDatabase.studentDAO().getAllStudents();
        studentsLiveData.observeForever(new Observer<List<Student>>() {
            @Override
            public void onChanged(@Nullable List<Student> o) {
                result.students = o;
                latch.countDown();
                studentsLiveData.removeObserver(this);
            }
        });
        // 3 coping skills
        final LiveData<List<CopingSkill>> copingSkillsLiveData = appDatabase.copingSkillDAO().getAllCopingSkills();
        copingSkillsLiveData.observeForever(new Observer<List<CopingSkill>>() {
            @Override
            public void onChanged(@Nullable List<CopingSkill> o) {
                result.copingSkills = o;
                latch.countDown();
                copingSkillsLiveData.removeObserver(this);
            }
        });
        // 4 customizations
        final LiveData<List<Customization>> customizationsLiveData = appDatabase.customizationDAO().getAllCustomizations();
        customizationsLiveData.observeForever(new Observer<List<Customization>>() {
            @Override
            public void onChanged(@Nullable List<Customization> o) {
                result.customizations = o;
                latch.countDown();
                customizationsLiveData.removeObserver(this);
            }
        });
        // 5 db files
        final LiveData<List<DbFile>> dbFilesLiveData = appDatabase.dbFileDAO().getAllDbFiles();
        dbFilesLiveData.observeForever(new Observer<List<DbFile>>() {
            @Override
            public void onChanged(@Nullable List<DbFile> o) {
                result.dbFiles = o;
                latch.countDown();
                dbFilesLiveData.removeObserver(this);
            }
        });
        // 6 emotions
        final LiveData<List<Emotion>> emotionsLiveData = appDatabase.emotionDAO().getAllEmotions();
        emotionsLiveData.observeForever(new Observer<List<Emotion>>() {
            @Override
            public void onChanged(@Nullable List<Emotion> o) {
                result.emotions = o;
                latch.countDown();
                emotionsLiveData.removeObserver(this);
            }
        });
        // 7 sessions
        final LiveData<List<Session>> sessionsLiveData = appDatabase.sessionDAO().getAllSessions();
        sessionsLiveData.observeForever(new Observer<List<Session>>() {
            @Override
            public void onChanged(@Nullable List<Session> o) {
                result.sessions = o;
                latch.countDown();
                sessionsLiveData.removeObserver(this);
            }
        });
        // 8 intermediate (emotion, copingskill)
        final LiveData<List<EmotionCopingSkill>> emotionsCopingSkillsLiveData = appDatabase.intermediateTablesDAO().getAllEmotionCopingSkills();
        emotionsCopingSkillsLiveData.observeForever(new Observer<List<EmotionCopingSkill>>() {
            @Override
            public void onChanged(@Nullable List<EmotionCopingSkill> o) {
                result.emotionsCopingSkills = o;
                latch.countDown();
                emotionsCopingSkillsLiveData.removeObserver(this);
            }
        });
        // 9 itinerary items
        final LiveData<List<ItineraryItem>> itineraryItemsLiveData = appDatabase.intermediateTablesDAO().getAllItineraryItems();
        itineraryItemsLiveData.observeForever(new Observer<List<ItineraryItem>>() {
            @Override
            public void onChanged(@Nullable List<ItineraryItem> o) {
                result.itineraryItems = o;
                latch.countDown();
                itineraryItemsLiveData.removeObserver(this);
            }
        });
        // 10 intermediate (session, copingskill)
        final LiveData<List<SessionCopingSkill>> sessionsCopingSkillsLiveData = appDatabase.intermediateTablesDAO().getAllSessionCopingSkills();
        sessionsCopingSkillsLiveData.observeForever(new Observer<List<SessionCopingSkill>>() {
            @Override
            public void onChanged(@Nullable List<SessionCopingSkill> o) {
                result.sessionsCopingSkills = o;
                latch.countDown();
                sessionsCopingSkillsLiveData.removeObserver(this);
            }
        });

        latch.await(2, TimeUnit.SECONDS);

        return result;
    }

}
