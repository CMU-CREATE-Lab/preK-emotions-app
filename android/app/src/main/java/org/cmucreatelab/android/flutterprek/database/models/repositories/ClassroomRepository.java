package org.cmucreatelab.android.flutterprek.database.models.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.Classroom;
import org.cmucreatelab.android.flutterprek.database.models.daos.ClassroomDAO;

import java.util.List;

public class ClassroomRepository {

    private ClassroomDAO classroomDao;

    private LiveData<List<Classroom>> allClassrooms;
//    private List<Classroom> allClassrooms;

    public ClassroomRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        classroomDao = db.classroomDAO();
        allClassrooms = classroomDao.getAllClassrooms();
    }

    public LiveData<List<Classroom>> getAllClassrooms() {
//    List<Classroom> getAllClassrooms() {
        return allClassrooms;
    }


    public void insert (Classroom classroom) {
        new insertAsyncTask(classroomDao).execute(classroom);
    }

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

}
