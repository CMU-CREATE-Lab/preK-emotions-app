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
    private final boolean onClickListener;
    private final ClickListener clickListener;

    public interface ClickListener {
        void onClick(Student student);
    }


    public StudentIndexAdapter(Context appContext, List<Student> students) {
        this(appContext, students, null);
    }


    public StudentIndexAdapter(Context appContext, List<Student> students, ClickListener clickListener) {
        this.appContext = appContext;
        this.students = students;
        this.clickListener = clickListener;
        this.onClickListener = (clickListener != null);
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
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_view_item_student, parent, false);
            // NOTE: requires api level 21
            result.findViewById(R.id.imageView).setClipToOutline(true);
        } else {
            result = convertView;
        }
        final Student student = students.get(position);
        TextView textView = (TextView)result.findViewById(R.id.text1);
        textView.setText(student.getName());
        // TODO replace with student image
        Util.setImageViewWithAsset(appContext, (ImageView)result.findViewById(R.id.imageView), "etc/img/xman1.png");

        if (onClickListener) {
            result.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onClick(student);
                }
            });
        }

        return result;
    }

}
