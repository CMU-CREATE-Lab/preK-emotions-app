package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.classrooms.ManageClassroomActivityWithHeaderAndDrawer;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.CalculateHighlightInfo;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.classrooms.ClassroomHighlightsActivity;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.EditCopingSkillsHighlightIndex;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.views.collapsible_view.CopingSkillsInfoCollapsibleView;
import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.util.Map;

public class CopingSkillsHighlightGridView extends ConstraintLayout {

    private GridView gridView;
    private TextView titleTextView;
    private ImageView editCopingSkills;
    private PillToggleGroup pillToggleGroup;
    private CopingSkillsInfoCollapsibleView copingSkillsInfoCollapsibleView;



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
        this.copingSkillsInfoCollapsibleView = findViewById(R.id.copingSkillsInfoCollapsibleView);
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

    public void initCollapsibleViewListener(Context context) {
        ImageView copingSkillsInfoImageView = findViewById(R.id.copingSkillsInfoImageView);
        copingSkillsInfoImageView.setOnClickListener(copingSkillsInfoCollapsibleView);
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


    public CopingSkillsInfoCollapsibleView getCopingSkillsInfoCollapsibleView() {
        return copingSkillsInfoCollapsibleView;
    }


    public interface CopingSkillCalculationCallback{
        void onCopingSkillsCalculated();
    }


}
