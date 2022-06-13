package org.cmucreatelab.android.flutterprek.activities.teacher_section.classrooms;

import android.os.AsyncTask;

import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;

public class UpdateClassroomModelAsyncTask extends AsyncTask<Void, Void, Boolean> {

    private AppDatabase appDatabase;
    private ActionType actionType;
    private Classroom classroom;
    private PostExecute postExecute;

    public enum ActionType {
        UPDATE,
        INSERT,
        DELETE
    }

    public static abstract class PostExecute {
        public abstract void onPostExecute(Boolean modelSaved, Classroom classroom);
    }


    public UpdateClassroomModelAsyncTask(AppDatabase appDatabase, ActionType actionType, Classroom classroom, PostExecute postExecute) {
        super();
        this.appDatabase = appDatabase;
        this.actionType = actionType;
        this.classroom = classroom;
        this.postExecute = postExecute;
    }


    @Override
    protected Boolean doInBackground(Void... voids) {
        switch (actionType) {
            case INSERT:
                appDatabase.classroomDAO().insert(classroom);
                break;
            case DELETE:
                appDatabase.classroomDAO().delete(classroom);
                break;
            case UPDATE:
            default:
                appDatabase.classroomDAO().update(classroom);
                break;
        }
        return true;
    }


    @Override
    protected void onPostExecute(Boolean modelSaved) {
        postExecute.onPostExecute(modelSaved, this.classroom);
    }

}
