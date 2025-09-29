package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.views;

import android.content.Context;
import android.content.Intent;

import org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.UploadPhotoActivity;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.students.StudentHighlightsActivity;
import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

public class PutExtraStudentHighlightsActivityHelper {
    //path, resultCode, requestCode, studentUuid, classroomName, classroom
    public static Intent getPutExtraIntentUpdate(String path, int requestCode, Student student, Classroom classroom, Context context){
        Intent intent = new Intent(context, StudentHighlightsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("path", path);
        //intent.putExtra("resultCode", resultCode);
        intent.putExtra("requestCode", requestCode);
        intent.putExtra(StudentHighlightsActivity.EXTRA_STUDENT, student);
        intent.putExtra(StudentHighlightsActivity.EXTRA_CLASSROOM_NAME, classroom.getName());
        intent.putExtra(StudentHighlightsActivity.EXTRA_CLASSROOM, classroom);
        return intent;
    }
}
