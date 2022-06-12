package org.cmucreatelab.android.flutterprek.activities.teacher_section.students;

import android.os.AsyncTask;

import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.db_file.DbFile;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.io.File;

public class UpdateStudentModelAsyncTask extends AsyncTask<Void, Void, Boolean> {

    private AppDatabase appDatabase;
    private ActionType actionType;
    private Student student;
    private File newStudentPicture;
    private PostExecute postExecute;

    public enum ActionType {
        UPDATE,
        INSERT,
        DELETE
    }

    public static abstract class PostExecute {
        public abstract void onPostExecute(Boolean modelSaved);
    }


    public UpdateStudentModelAsyncTask(AppDatabase appDatabase, ActionType actionType, Student student, File newStudentPicture, PostExecute postExecute) {
        super();
        this.appDatabase = appDatabase;
        this.actionType = actionType;
        this.student = student;
        this.newStudentPicture = newStudentPicture;
        this.postExecute = postExecute;
    }


    @Override
    protected Boolean doInBackground(Void... voids) {
        if (newStudentPicture != null) {
            // insert DbFile first, then update student
            DbFile dbFile = new DbFile(DbFile.DbFileType.FILEPATH, newStudentPicture.getPath());
            appDatabase.dbFileDAO().insert(dbFile);
            if (dbFile == null) {
                return false;
            }
            student.setPictureFileUuid(dbFile.getUuid());
        }
        switch (actionType) {
            case INSERT:
                appDatabase.studentDAO().insert(student);
                break;
            case DELETE:
                appDatabase.studentDAO().delete(student);
                break;
            case UPDATE:
            default:
                // update student
                appDatabase.studentDAO().update(student);
                break;
        }
        return true;
    }


    @Override
    protected void onPostExecute(Boolean modelSaved) {
        postExecute.onPostExecute(modelSaved);
    }

}
