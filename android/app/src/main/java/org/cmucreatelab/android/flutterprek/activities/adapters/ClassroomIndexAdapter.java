package org.cmucreatelab.android.flutterprek.activities.adapters;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.Util;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.StudentWithCustomizations;
import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;
import org.cmucreatelab.android.flutterprek.database.models.db_file.DbFile;

import java.util.List;

public class ClassroomIndexAdapter extends AbstractListAdapter<Classroom> {

    private final AppCompatActivity activity;
    private final List<Classroom> classrooms;
    private final boolean onClickListener;
    private final ClickListener clickListener;

    public interface ClickListener {
        void onClick(Classroom classroom);
    }

    private static int[] studentThumbnailViews = {
            R.id.imageStudent1, R.id.imageStudent2, R.id.imageStudent3,
            R.id.imageStudent4, R.id.imageStudent5, R.id.imageStudent6,
            R.id.imageStudent7, R.id.imageStudent8, R.id.imageStudentMore
    };


    private static void handleStudentThumbnailsForView(AppCompatActivity activity, View view, @Nullable List<StudentWithCustomizations> students) {
        for (int id: studentThumbnailViews) {
            view.findViewById(id).setVisibility(View.INVISIBLE);
        }
        for (int id=0; id<students.size(); id++) {
            // TODO check edge case works (for R.id.imageStudentMore)
            if (id<studentThumbnailViews.length) {
                StudentWithCustomizations student = students.get(id);
                int studentThumbnail = studentThumbnailViews[id];
                if (studentThumbnail != R.id.imageStudentMore) {
                    final ImageView studentThumbView = view.findViewById(studentThumbnail);
                    studentThumbView.setVisibility(View.VISIBLE);
                    if (student.student.getPictureFileUuid() != null) {
                        final Context appContext = activity.getApplicationContext();
                        AppDatabase.getInstance(appContext).dbFileDAO().getDbFile(student.student.getPictureFileUuid()).observe(activity, new Observer<DbFile>() {
                            @Override
                            public void onChanged(@Nullable DbFile dbFile) {
                                Util.setImageViewWithDbFile(appContext, studentThumbView, dbFile);
                            }
                        });
                    }
                }
            } else {
                break;
            }
        }
    }


    public ClassroomIndexAdapter(AppCompatActivity activity, List<Classroom> classrooms) {
        this(activity, classrooms, null);
    }


    public ClassroomIndexAdapter(AppCompatActivity activity, List<Classroom> classrooms, ClickListener clickListener) {
        this.activity = activity;
        this.classrooms = classrooms;
        this.clickListener = clickListener;
        this.onClickListener = (clickListener != null);
    }


    @Override
    public List<Classroom> getList() {
        return classrooms;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_view_item_teacher_classroom, parent, false);
        } else {
            result = convertView;
        }
        final Classroom classroom = classrooms.get(position);
        TextView textView = (TextView)result.findViewById(R.id.textViewClassroomName);
        textView.setText(classroom.getName());

//        // TODO list students
//        CircularImageView student1 = result.findViewById(R.id.imageStudent1);
//        //student1.setimage
        String classroomUuid = classroom.getUuid();
        LiveData<List<StudentWithCustomizations>> liveData;
        liveData = AppDatabase.getInstance(parent.getContext()).studentDAO().getAllStudentsWithCustomizationsFromClassroom(classroomUuid);
        liveData.observe(activity, new Observer<List<StudentWithCustomizations>>() {
            @Override
            public void onChanged(@Nullable List<StudentWithCustomizations> students) {
                TextView textViewStudents = result.findViewById(R.id.textViewStudents);
                textViewStudents.setText(String.format("%d", students.size()));

                //ClassroomStudentsThumbnailsHelper.handleStudentThumbnailsForView(activity, result, students);
                handleStudentThumbnailsForView(activity, result, students);
            }
        });

        if (onClickListener) {
            result.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onClick(classroom);
                }
            });
        }

        return result;
    }

}
