package org.cmucreatelab.android.flutterprek.database.models;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import org.cmucreatelab.android.flutterprek.database.AppDatabase;

import java.util.List;

/**
 * Created by tasota on 9/6/2018.
 *
 * ClassroomRepository
 *
 * Repository for the classrooms table. The purpose of a repository is to manage local and remote
 * databases. See Room persistence library documentation for details:
 *   https://developer.android.com/training/data-storage/room/accessing-data
 */
final class ClassroomRepository {

    private ClassroomDAO classroomDao;
    private LiveData<List<Classroom>> allClassrooms;

    private static class insertAsyncTask extends AsyncTask<Classroom, Void, Void> {

        private ClassroomDAO asyncTaskDao;

        insertAsyncTask(ClassroomDAO dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Classroom... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }


    ClassroomRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        classroomDao = db.classroomDAO();
        allClassrooms = classroomDao.getAllClassrooms();
    }


    LiveData<List<Classroom>> getAllClassrooms() {
        return allClassrooms;
    }


    void insert (Classroom classroom) {
        new insertAsyncTask(classroomDao).execute(classroom);
    }

}
