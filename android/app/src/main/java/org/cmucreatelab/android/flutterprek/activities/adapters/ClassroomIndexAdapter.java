package org.cmucreatelab.android.flutterprek.activities.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;

import java.util.List;

public class ClassroomIndexAdapter extends AbstractListAdapter<Classroom> {

    private final List<Classroom> classrooms;


    public ClassroomIndexAdapter(List<Classroom> classrooms) {
        this.classrooms = classrooms;
    }


    @Override
    public List<Classroom> getList() {
        return classrooms;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View result;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.classroom_grid_view_item, parent, false);
        } else {
            result = convertView;
        }
        TextView textView = (TextView)result.findViewById(R.id.text1);
        textView.setText(classrooms.get(position).getName());

        return result;
    }

}
