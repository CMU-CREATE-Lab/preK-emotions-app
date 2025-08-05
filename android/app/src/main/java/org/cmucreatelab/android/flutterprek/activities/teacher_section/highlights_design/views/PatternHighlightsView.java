package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.views;

import static org.cmucreatelab.android.flutterprek.activities.adapters.StudentHighlightWithCustomizationsIndexAdapter.setGridViewHeightBasedOnChildren;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.adapters.PatternHighlightsAdapter;
import org.cmucreatelab.android.flutterprek.activities.adapters.StudentHighlightWithCustomizationsIndexAdapter;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.classrooms.ClassroomHighlightsActivity;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.students.StudentDisplayItem;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.views.collapsible_view.CopingSkillsInfoCollapsibleView;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;
import org.cmucreatelab.android.flutterprek.database.models.db_file.DbFile;
import org.cmucreatelab.android.flutterprek.database.models.embedded_models.StudentWithCustomizations;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class PatternHighlightsView extends ConstraintLayout {
    private Classroom classroom;
    private List<StudentWithCustomizations> students;
    private RecyclerView recyclerView;

    public PatternHighlightsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PatternHighlightsView(@NonNull Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout._view_pattern_highlights, this, true);
        recyclerView = findViewById(R.id.patternHighlightRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

    }

    //TODO update information about what pattern highlight is
    public void initCollapsibleViewListener(Context context) {
        ImageView copingSkillsInfoImageView = findViewById(R.id.copingSkillsInfoImageView);
        CopingSkillsInfoCollapsibleView copingSkillsInfoCollapsibleView = findViewById(R.id.copingSkillsInfoCollapsibleView);
        copingSkillsInfoImageView.setOnClickListener(copingSkillsInfoCollapsibleView);
    }

    public void setClassroom(Classroom classroom){
        this.classroom = classroom;
    }

    public void setStudentList(AppCompatActivity activity){
        //PLACE HOLDER CODE TO GRAB A LIST OF STUDENTS
        //TODO Replace studentDAO call with call from custom class (DeterminePatternHighlights.class) to get students for pattern highlights
            AppDatabase.getInstance(activity).studentDAO()
                    .getAllStudentsWithCustomizationsFromClassroom(classroom.getUuid())
                    .observe(activity, students -> {
                        List<StudentWithCustomizations> limitedStudents = students.subList(0, Math.min(5, students.size()));
                        List<StudentDisplayItem> displayItems = new ArrayList<>();
        //---------------------------------------------------------------------------------------------------------------------/
                        //Atomic Integer - thread safe integer to keep track of how many dbfiles are resolved with multiple observers
                        AtomicInteger resolved = new AtomicInteger(0);

                        //Loop through student list for patterns and get files
                        for (StudentWithCustomizations studentWithCustom : limitedStudents) {
                            String fileUuid = studentWithCustom.student.getPictureFileUuid();
                            if (fileUuid != null) {
                                AppDatabase.getInstance(activity).dbFileDAO().getDbFile(fileUuid)
                                        .observe((LifecycleOwner) activity, new Observer<DbFile>() {
                                            @Override
                                            public void onChanged(DbFile dbFile) {
                                                //StudentDisplayItem is a holder class to store student and dbfile to be passed into the adapter
                                                displayItems.add(new StudentDisplayItem(studentWithCustom.student, dbFile));
                                                checkAndSetAdapterWhenAllReady(displayItems, activity, resolved.incrementAndGet(), limitedStudents.size());
                                            }
                                        });
                            } else {
                                displayItems.add(new StudentDisplayItem(studentWithCustom.student, null));
                                checkAndSetAdapterWhenAllReady(displayItems,activity, resolved.incrementAndGet(), limitedStudents.size());
                            }
                        }
                    });


    }
    //helper function to set the adapter file once all picture files are resolved
    private void checkAndSetAdapterWhenAllReady(List<StudentDisplayItem> displayItems, Context context,int readyCount, int totalCount) {
        if (readyCount == totalCount) {
            // All picture files resolved
            PatternHighlightsAdapter adapter = new PatternHighlightsAdapter((AppCompatActivity) context,displayItems);
            recyclerView.setAdapter(adapter);

            recyclerView.post(() -> {
                PatternHighlightsAdapter.setRecyclerViewHorizontalSpacing(recyclerView,12);
            });

        }
    }

}
