package org.cmucreatelab.android.flutterprek.database.models.student;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import org.cmucreatelab.android.flutterprek.database.AppDatabase;

import java.util.List;

/**
 * Created by tasota on 9/13/2018.
 *
 * StudentRepository
 *
 * Repository for the students table. The purpose of a repository is to manage local and remote
 * databases. See Room persistence library documentation for details:
 *   https://developer.android.com/training/data-storage/room/accessing-data
 */
final class StudentRepository {

    private StudentDAO studentDAO;
    private LiveData<List<Student>> allStudents;

    private static class insertAsyncTask extends AsyncTask<Student, Void, Void> {

        private StudentDAO asyncTaskDao;

        insertAsyncTask(StudentDAO dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Student... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }


    StudentRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        studentDAO = db.studentDAO();
        allStudents = studentDAO.getAllStudents();
    }


    LiveData<List<Student>> getAllStudents() {
        return allStudents;
    }


    void insert (Student student) {
        new insertAsyncTask(studentDAO).execute(student);
    }

}
