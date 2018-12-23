package org.cmucreatelab.android.flutterprek.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.Util;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.util.List;

public class StudentIndexAdapter extends BaseAdapter {

    private final Context appContext;
    private final List<Student> students;

    public StudentIndexAdapter(Context appContext, List<Student> students) {
        this.appContext = appContext;
        this.students = students;
    }

    @Override
    public int getCount() {
        return students.size();
    }

    @Override
    public Object getItem(int position) {
        return students.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // TODO for demo thumbnails only; remove later
    private int getResourceFromPosition(int position) {
        switch (position % 6) {
            case 1:
                return R.drawable.ic_thumbnail_conducting;
            case 2:
                return R.drawable.ic_thumbnail_drawing;
            case 3:
                return R.drawable.ic_thumbnail_flower_breathing;
            case 4:
                return R.drawable.ic_thumbnail_talk;
            case 5:
                return R.drawable.ic_thumbnail_yoga;
        }
        return R.drawable.ic_thumbnail_balloon_squeeze;
    }
    // TODO for demo emotions only; remove later
    private String getAssetPathFromPosition(int position) {
        return "emotions/ic_icon_happy.png";
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
        //((ImageView)result.findViewById(R.id.imageView)).setImageResource(getResourceFromPosition(position));
        // TODO demo emotion (remove later)
        Util.setImageViewWithAsset(appContext, (ImageView) result.findViewById(R.id.imageView), getAssetPathFromPosition(position));

        return result;
    }

}
