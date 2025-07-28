package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.button.MaterialButtonToggleGroup;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.classrooms.ManageClassroomActivityWithHeaderAndDrawer;
import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.util.Map;

public class CopingSkillsHighlightGridView extends ConstraintLayout {

    private GridView gridView;
    private TextView titleTextView;
    private ImageView editCopingSkills;
    private PillToggleGroup pillToggleGroup;



    public CopingSkillsHighlightGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CopingSkillsHighlightGridView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout._view_coping_skill_highlight_grid, this, true);
        gridView = findViewById(R.id.copingSkillsGridView);
        titleTextView = findViewById(R.id.titleMostUsed);
        editCopingSkills = findViewById(R.id.editCopingSkills);
        pillToggleGroup = findViewById(R.id.copingSkillsToggle);
        pillToggleGroup.check(R.id.btn_week);

    }

    public void calculateClassCopingSkillsOverview(CalculateHighlightInfo.OverviewDateRange range, Classroom classroom, ClassroomHighlightsActivity.CopingSkillsOverviewCallback callback){
        CalculateHighlightInfo calculateHighlightInfo = new CalculateHighlightInfo(classroom, getContext(), (AppCompatActivity) getContext());
        calculateHighlightInfo.copingSkillClassOverview(range, new CopingSkillCalculationCallback() {
            @Override
            public void onCopingSkillsCalculated() {
                Map<String, Integer> map = calculateHighlightInfo.getClassCopingSkillsCounts();
                if (callback != null) {
                    callback.onOverviewCalculated(map);
                }
            }

        });
    }
    public void calculateStudentCopingSkillsOverview(CalculateHighlightInfo.OverviewDateRange range, Student student, ClassroomHighlightsActivity.CopingSkillsOverviewCallback callback){
        CalculateHighlightInfo calculateHighlightInfo = new CalculateHighlightInfo(null, getContext(), (AppCompatActivity) getContext());
        calculateHighlightInfo.copingSkillStudentOverview(range,student, new CopingSkillCalculationCallback() {
            @Override
            public void onCopingSkillsCalculated() {
                Map<String, Integer> map = calculateHighlightInfo.getClassCopingSkillsCounts();
                if (callback != null) {
                    callback.onOverviewCalculated(map);
                }
            }

        });
    }

    public void setAdapter(ListAdapter adapter) {
        gridView.setAdapter(adapter);
    }

    public void enableSettingsConfig( boolean enable){
        ImageView editCopingSkills = findViewById(R.id.editCopingSkills);
        if(enable){
            editCopingSkills.setVisibility(View.VISIBLE);
        } else {
            editCopingSkills.setVisibility(View.GONE);
        }
    }

    public void initSettingsClickListener(Context context, Classroom classroom){
        editCopingSkills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditCopingSkillsHighlightIndex.class);
                intent.putExtra(ManageClassroomActivityWithHeaderAndDrawer.EXTRA_CLASSROOM, classroom); // if needed
                context.startActivity(intent);
            }
        });
    }

    public void initInfoListener(Context context){
        ImageView info = findViewById(R.id.copingSkillsInfo);
        info.setOnClickListener(v -> showInfoDialog(
                context,
                "What is This?",
                "This section explains emotional regulation techniques.")
        );
    }

    public void showInfoDialog(Context context, String title, String message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    public void setTitle(String title) {
        titleTextView.setText(title);
    }


    public void setOnToggleCheckedChanged(PillToggleGroup.OnCheckedChangedListener listener) {
        if (pillToggleGroup != null) {
            pillToggleGroup.setOnCheckedChanged(listener);
        }
    }


    public interface CopingSkillCalculationCallback{
        void onCopingSkillsCalculated();
    }


}
