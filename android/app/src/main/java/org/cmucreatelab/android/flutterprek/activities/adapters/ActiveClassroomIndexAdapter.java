package org.cmucreatelab.android.flutterprek.activities.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;

import java.util.List;

public class ActiveClassroomIndexAdapter extends AbstractListAdapter<Classroom> {

    private final List<Classroom> classrooms;
    private final boolean onClickListener;
    private final ClickListener clickListener;

    public interface ClickListener {
        void onClick(Classroom classroom);
    }


    public ActiveClassroomIndexAdapter(List<Classroom> classrooms) {
        this(classrooms, null);
    }


    public ActiveClassroomIndexAdapter(List<Classroom> classrooms, ClickListener clickListener) {
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
        View result;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_view_item_classroom, parent, false);
        } else {
            result = convertView;
        }
        final Classroom classroom = classrooms.get(position);
        TextView textView = (TextView)result.findViewById(R.id.text1);
        textView.setText(classroom.getName());

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
