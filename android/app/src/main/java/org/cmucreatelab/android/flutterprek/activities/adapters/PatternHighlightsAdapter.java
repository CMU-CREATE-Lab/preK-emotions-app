package org.cmucreatelab.android.flutterprek.activities.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.Util;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.students.StudentDisplayItem;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.views.ArcViewOverlay;
import org.cmucreatelab.android.flutterprek.database.models.embedded_models.StudentWithCustomizations;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.util.ArrayList;
import java.util.List;

public class PatternHighlightsAdapter extends RecyclerView.Adapter<PatternHighlightsAdapter.ViewHolder>{

    private List<StudentDisplayItem> students;
    private AppCompatActivity appCompatActivity;;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private ImageView imageView;
        private ArcViewOverlay arcView;


        //holds objects to be set
        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = view.findViewById(R.id.text1);
            imageView = view.findViewById(R.id.imageView);
            arcView = view.findViewById(R.id.arcView);

        }

        public TextView getTextView() {
            return textView;
        }
        public ImageView getImageView() {
            return imageView;
        }
        public ArcViewOverlay getArcView() {
            return arcView;
        }
    }

    public PatternHighlightsAdapter(AppCompatActivity activity,List<StudentDisplayItem> s) {
        students = s;
        appCompatActivity=activity;
    }

    private void setArc(ArcViewOverlay arcView){
        int color = Color.BLACK;
        List<Integer> colors = new ArrayList<>();
        colors.add(color);
        arcView.setSegmentColors(colors);
        List<Float> angles = new ArrayList<>();
        angles.add(360f);
        arcView.setSegmentAngles(angles);
        arcView.setArcWidth(4f);
        int width = dpToPx(appCompatActivity.getApplicationContext(), 85);
        int height = dpToPx(appCompatActivity.getApplicationContext(), 85);
        arcView.setWidthAndHeight(width,height);

    }

    public static void setRecyclerViewHorizontalSpacing(RecyclerView recyclerView, int dp) {
        int px = dpToPx(recyclerView.getContext(), dp);
        RecyclerView.ItemDecoration decoration = new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect,
                                       @NonNull View view,
                                       @NonNull RecyclerView parent,
                                       @NonNull RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view);
                int itemCount = state.getItemCount();

                outRect.right = px;

                if (position == 0) {
                    outRect.left = px;
                }
            }
        };

        recyclerView.addItemDecoration(decoration);
    }

    private static int dpToPx(Context context, int dp) {
        return Math.round(dp * context.getResources().getDisplayMetrics().density);
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.grid_view_item_student_circle, viewGroup, false);

        return new ViewHolder(view);
    }

    // Displays the info
    //--Current setup w/ adapter pushes student to end of row when livedata is changed  (i.e. when a picture is update) To be fixed?

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTextView().setText(students.get(position).student.getName());
        setArc(viewHolder.arcView);

        //set image if exist else set placeholder
        if (students.get(position).dbFile != null) {
            Util.setImageViewWithDbFile(appCompatActivity.getApplicationContext(),(ImageView) viewHolder.getImageView(), students.get(position).dbFile);

        } else {
            viewHolder.getImageView().setImageResource(R.drawable.ic_placeholder);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return students.size();
    }
}
