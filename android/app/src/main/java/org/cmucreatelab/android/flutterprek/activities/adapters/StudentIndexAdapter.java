package org.cmucreatelab.android.flutterprek.activities.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.Util;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.util.List;

public class StudentIndexAdapter extends AbstractListAdapter<Student> {

    private final Context appContext;
    private final List<Student> students;


    public StudentIndexAdapter(Context appContext, List<Student> students) {
        this.appContext = appContext;
        this.students = students;
    }


    @Override
    public List<Student> getList() {
        return students;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View result;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_grid_view_item, parent, false);
            // NOTE: requires api level 21
            result.findViewById(R.id.imageView).setClipToOutline(true);
        } else {
            result = convertView;
        }
        TextView textView = (TextView)result.findViewById(R.id.text1);
        textView.setText(students.get(position).getName());
        // TODO replace with student image
        Util.setImageViewWithAsset(appContext, (ImageView)result.findViewById(R.id.imageView), "etc/img/xman1.png");

        return result;
    }

}
