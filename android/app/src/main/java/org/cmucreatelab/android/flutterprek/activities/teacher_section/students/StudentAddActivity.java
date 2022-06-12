package org.cmucreatelab.android.flutterprek.activities.teacher_section.students;

import android.util.Log;
import android.widget.Toast;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.io.File;

public class StudentAddActivity extends StudentUpdateAbstractActivity {

    private static final String headerTitleAddStudent = "Add Student";


    @Override
    public String getHeaderTitle() {
        return headerTitleAddStudent;
    }


    @Override
    public boolean isDisplayDeleteButton() {
        return false;
    }


    @Override
    public void updateModel(final Student student, final File newStudentPicture) {
        Log.d(Constants.LOG_TAG, "performing DB writes in updateModel()");
        new UpdateStudentModelAsyncTask(AppDatabase.getInstance(getApplicationContext()), UpdateStudentModelAsyncTask.ActionType.INSERT, student, newStudentPicture, new UpdateStudentModelAsyncTask.PostExecute() {
            @Override
            public void onPostExecute(Boolean modelSaved) {
                if (!modelSaved) {
                    Toast.makeText(getApplicationContext(), "Could not save changes to Student", Toast.LENGTH_LONG).show();
                }
                finish();
            }
        }).execute();
    }



}
