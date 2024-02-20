package org.cmucreatelab.android.flutterprek.activities.adapters;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.ClassroomWithCustomizations;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.util.List;

public class ClassroomWithCustomizationsIndexAdapter extends AbstractListAdapter<ClassroomWithCustomizations> {

    private final List<ClassroomWithCustomizations> classrooms;
    private final boolean onClickListener;
    private final ClickListener clickListener;
    private final AbstractActivity activity;

    public interface ClickListener {
        void onClick(ClassroomWithCustomizations classroomWithCustomizations);
    }


    public ClassroomWithCustomizationsIndexAdapter(AbstractActivity activity, List<ClassroomWithCustomizations> classrooms) {
        this(activity, classrooms, null);
    }


    public ClassroomWithCustomizationsIndexAdapter(AbstractActivity activity, List<ClassroomWithCustomizations> classrooms, ClickListener clickListener) {
        this.activity = activity;
        this.classrooms = classrooms;
        this.clickListener = clickListener;
        this.onClickListener = (clickListener != null);
    }


    @Override
    public List<ClassroomWithCustomizations> getList() {
        return classrooms;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View result;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_view_item_classroom, parent, false);
        } else {
            result = convertView;
        }
        final ClassroomWithCustomizations classroomWithCustomizations = classrooms.get(position);
        TextView textView = result.findViewById(R.id.text1);
        textView.setText(classroomWithCustomizations.classroom.getName());

        AppDatabase.getInstance(activity.getApplicationContext()).studentDAO().getAllStudentsFromClassroom(classroomWithCustomizations.classroom.getUuid()).observe(activity, new Observer<List<Student>>() {
            @Override
            public void onChanged(@Nullable List<Student> students) {
                int numberOfStudents = students.size();
                String label = (numberOfStudents == 1) ? "Student" : "Students";
                TextView textViewStudents = result.findViewById(R.id.textViewStudents);
                textViewStudents.setText(String.format("%d %s", numberOfStudents, label));
            }
        });

        if (onClickListener) {
            result.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onClick(classroomWithCustomizations);
                }
            });
        }

        return result;
    }

}
