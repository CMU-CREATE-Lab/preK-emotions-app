package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.students;

import static org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.CalculateHighlightInfo.setArc;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.Util;
import org.cmucreatelab.android.flutterprek.activities.adapters.CopingSkillHighlightWCIndexAdapter;
import org.cmucreatelab.android.flutterprek.activities.adapters.EmotionHighlightAdapter;
import org.cmucreatelab.android.flutterprek.activities.adapters.StudentHighlightWithCustomizationsIndexAdapter;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.classrooms.ManageClassroomActivityWithHeaderAndDrawer;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.CalculateHighlightInfo;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.classrooms.ClassroomHighlightsActivity;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.HighlightsDesignActivityWithHeaderAndDrawer;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.views.HighlightsViewDrawer;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.views.PillToggleGroup;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.UploadPhotoActivity;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.views.ArcViewOverlay;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.views.CopingSkillsHighlightGridView;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.students.UpdateStudentModelAsyncTask;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.embedded_models.CopingSkillWithCustomizations;
import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;
import org.cmucreatelab.android.flutterprek.database.models.db_file.DbFile;
import org.cmucreatelab.android.flutterprek.database.models.embedded_models.ResolvedEmotionWithImageFile;
import org.cmucreatelab.android.flutterprek.database.models.emotion.Emotion;
import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.ItineraryItem;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;
import org.cmucreatelab.android.mylibrary.CameraActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class StudentHighlightsActivity extends HighlightsDesignActivityWithHeaderAndDrawer {

    public static final int HAPPY_CODE = 100;
    public static final int SAD_CODE = 101;
    public static final int ANGRY_CODE = 102;
    public static final int SCARED_CODE = 103;
    public static final int EXCITED_CODE = 104;
    public static final int STUDENT_CODE = 105;
    // TODO @Dante refactor extras to get classroom name from classroom
    private String classroomName;
    private String studentUuid;
    private Student student;
    private Classroom classroom;
    private CalculateHighlightInfo.OverviewDateRange dateRange = CalculateHighlightInfo.OverviewDateRange.WEEK;
    private CalculateHighlightInfo.OverviewDateRange currentCopingSkillDisplayMode = CalculateHighlightInfo.OverviewDateRange.WEEK;


    //listerns for starting camera activity based on emotion
    private final EmotionHighlightAdapter.ClickListener emotionsListener = new EmotionHighlightAdapter.ClickListener() {
        @Override
        public void onClick(Emotion emotion) {
            GlobalHandler.getInstance(getApplicationContext()).isRunningActivityForImageResult = true;
            Intent intent = new Intent(StudentHighlightsActivity.this, CameraActivity.class);
            intent.putExtra(CameraActivity.EXTRA_STUDENT, studentUuid);
            intent.putExtra(CameraActivity.EXTRA_CLASSROOM_NAME, classroomName);

            switch(emotion.getName()) {
                case "Happy":
                    startActivityForResult(intent, HAPPY_CODE);
                    break;
                case "Sad":
                    startActivityForResult(intent, SAD_CODE);
                    break;
                case "Mad":
                    startActivityForResult(intent, ANGRY_CODE);
                    break;
                case "Scared":
                    startActivityForResult(intent, SCARED_CODE);
                    break;
                case "Excited":
                    startActivityForResult(intent, EXCITED_CODE);
                    break;

            }

        }
    };



    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initStudentPic(){
        ImageView profilePicture = findViewById(R.id.profilePicture);
        initStudentPillGroup();
        setStudentArc();

        if (student.getPictureFileUuid() != null) {
            final Context appContext = getApplicationContext();
            AppDatabase.getInstance(appContext).dbFileDAO().getDbFile(student.getPictureFileUuid()).observe(this, new Observer<DbFile>() {
                @Override
                public void onChanged(@Nullable DbFile dbFile) {
                    Util.setImageViewWithDbFile(appContext, profilePicture, dbFile);
                }
            });
        } else {
            profilePicture.setImageResource(R.drawable.ic_placeholder);
        }

        TextView textView = (TextView) findViewById(R.id.studentName);
        textView.setText(student.getName());
        ImageView editImage = findViewById(R.id.editImage);

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditNamePopup();
            }
        });

        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCameraActivity();

            }

        });

        findViewById(R.id.cameraIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCameraActivity();
            }
        });
    }

    private void setStudentArc(){
        ArcViewOverlay arcView = findViewById(R.id.arcViewOverlay);

        //get arc info
        CalculateHighlightInfo calculateHighlightInfo = new CalculateHighlightInfo(null, getApplicationContext(), StudentHighlightsActivity.this);
        calculateHighlightInfo.studentOverview(student,dateRange, new StudentHighlightWithCustomizationsIndexAdapter.HighlightCalculationCallback() {
            @Override
            public void onHighlightsCalculated() {
                Map<String, Integer> emotionCounts = calculateHighlightInfo.getStudentEmotionCounts();
                setArc(emotionCounts, arcView,20f, false);

            }

        });
    }

    private void initStudentPillGroup(){
        PillToggleGroup studentPillGroup = findViewById(R.id.studentPillToggle);
        studentPillGroup.check(R.id.btn_week);
        studentPillGroup.setOnCheckedChanged(new PillToggleGroup.OnCheckedChangedListener() {
            @Override
            public void onCheckedChanged(int checkedId) {
                // Set student grid view adapter
                switch (checkedId) {
                    case R.id.btn_day:
                        dateRange= CalculateHighlightInfo.OverviewDateRange.DAY;
                        break;
                    case R.id.btn_week:
                        dateRange = CalculateHighlightInfo.OverviewDateRange.WEEK;
                        break;
                    case R.id.btn_month:
                        dateRange = CalculateHighlightInfo.OverviewDateRange.MONTH;
                        break;
                    case R.id.btn_year:
                        dateRange = CalculateHighlightInfo.OverviewDateRange.YEAR;
                        break;
                }
//
                if (dateRange == null) {
                    dateRange = CalculateHighlightInfo.OverviewDateRange.WEEK;
                }
                setStudentArc();
            }
        });

    }

    private void launchCameraActivity(){
        GlobalHandler.getInstance(getApplicationContext()).isRunningActivityForImageResult = true;
        Intent intent = new Intent(StudentHighlightsActivity.this, CameraActivity.class);
        intent.putExtra(CameraActivity.EXTRA_STUDENT, studentUuid);
        intent.putExtra(CameraActivity.EXTRA_CLASSROOM_NAME, classroomName);
        startActivityForResult(intent, STUDENT_CODE);
    }

    private void showEditNamePopup(){
        // Create an EditText
        final EditText input = new EditText(this);
        input.setHint("Enter new Name");

        new AlertDialog.Builder(this)
                .setTitle("Edit Name")
                .setView(input)
                .setPositiveButton("OK", (dialog, which) -> {
                    String newText = input.getText().toString();
                    updateName(newText);
                    TextView myTextView = findViewById(R.id.studentName);
                    myTextView.setText(newText);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void updateName(String newName){
        student.setName(newName);
        new UpdateStudentModelAsyncTask(AppDatabase.getInstance(getApplicationContext()), UpdateStudentModelAsyncTask.ActionType.UPDATE, student, null, new UpdateStudentModelAsyncTask.PostExecute() {
            @Override
            public void onPostExecute(Boolean modelSaved) {
                if (!modelSaved) {
                    Toast.makeText(getApplicationContext(), "Could not save changes to Student", Toast.LENGTH_LONG).show();
                }
            }
        }).execute();
    }

    private void initEmotionsGrid(){

        AppDatabase.getInstance(getApplicationContext()).embeddedDAO().getResolvedEmotionsForStudent(student.getUuid()).observe(StudentHighlightsActivity.this, new Observer<List<ResolvedEmotionWithImageFile>>() {
            @Override
            public void onChanged(List<ResolvedEmotionWithImageFile> resolvedEmotionWithImageFiles) {

                Log.v(Constants.LOG_TAG, String.format("Room DB getResolvedEmotionsForStudent() returned with list results size = %d", resolvedEmotionWithImageFiles.size()));
                final List<Emotion> emotionList = Util.EmotionMapper.fromResolvedList(resolvedEmotionWithImageFiles);
                GridView emotionsGridView = findViewById(R.id.emotionsGridView);
                emotionsGridView.setAdapter(new EmotionHighlightAdapter(StudentHighlightsActivity.this, emotionList, emotionsListener));
            }
        });
    }

    private void initMostUsedGrid(){
        updateCopingSkillsView();

        CopingSkillsHighlightGridView copingSkillsView = findViewById(R.id.copingSkillsCustomView);
        copingSkillsView.setOnToggleCheckedChanged(new PillToggleGroup.OnCheckedChangedListener() {
            @Override
            public void onCheckedChanged(int checkedId) {
                switch (checkedId) {
                    case R.id.btn_day:
                        currentCopingSkillDisplayMode = CalculateHighlightInfo.OverviewDateRange.DAY;
                        break;
                    case R.id.btn_week:
                        currentCopingSkillDisplayMode = CalculateHighlightInfo.OverviewDateRange.WEEK;
                        break;
                    case R.id.btn_month:
                        currentCopingSkillDisplayMode = CalculateHighlightInfo.OverviewDateRange.MONTH;
                        break;
                    case R.id.btn_year:
                        currentCopingSkillDisplayMode = CalculateHighlightInfo.OverviewDateRange.YEAR;
                        break;
                }
                if(currentCopingSkillDisplayMode == null){
                    currentCopingSkillDisplayMode = CalculateHighlightInfo.OverviewDateRange.WEEK;
                }
                updateCopingSkillsView();

            }
        });
        copingSkillsView.initCollapsibleViewListener(this);
    }
    private void updateCopingSkillsView(){
        CopingSkillsHighlightGridView copingSkillsView = findViewById(R.id.copingSkillsCustomView);
        AppDatabase.getInstance(this).copingSkillDAO().getAllCopingSkillsWithCustomizations().observe(this, new Observer<List<CopingSkillWithCustomizations>>() {
            @Override
            public void onChanged(@Nullable List<CopingSkillWithCustomizations> copingSkillsWithCustomizations) {

                copingSkillsView.calculateStudentCopingSkillsOverview(currentCopingSkillDisplayMode, student, new ClassroomHighlightsActivity.CopingSkillsOverviewCallback() {
                    @Override
                    public void onOverviewCalculated(Map<String, Integer> copingSkillsMap) {

                        //sort the coping skills based on count
                        Collections.sort(copingSkillsWithCustomizations, new Comparator<CopingSkillWithCustomizations>() {
                            @Override
                            public int compare(CopingSkillWithCustomizations cp1, CopingSkillWithCustomizations cp2) {
                                Integer count1 = copingSkillsMap.get(cp1.copingSkill.getUuid());
                                Integer count2 = copingSkillsMap.get(cp2.copingSkill.getUuid());

                                if (count1 == null) count1 = 0;
                                if (count2 == null) count2 = 0;

                                return count2.compareTo(count1); //highest to lowest
                            }
                        });

                        // Now create percents list in the same order as copingSkillsWithCustomizations
                        List<Integer> percents = new ArrayList<>();
                        percents = CalculateHighlightInfo.calculateCopingSkillPercents(copingSkillsMap);


                        copingSkillsView.setAdapter(new CopingSkillHighlightWCIndexAdapter(StudentHighlightsActivity.this, copingSkillsWithCustomizations, percents));
                    }
                });
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpDrawer();

        // get classroom and update the drawer
        this.classroom = (Classroom) getIntent().getSerializableExtra(ClassroomHighlightsActivity.EXTRA_CLASSROOM);
        getDrawerHighlights().setClassroom(classroom);
        getDrawerHighlights().setHighlighted(HighlightsViewDrawer.Row.CLASS_SHOW);
        setBackNavigationForDrawer(true, String.format("Back to %s", (classroom == null) ? "Classes" : classroom.getName()), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (classroom != null) {
                    Intent intent = new Intent(StudentHighlightsActivity.this, ClassroomHighlightsActivity.class);
                    intent.putExtra(ManageClassroomActivityWithHeaderAndDrawer.EXTRA_CLASSROOM, classroom);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Log.e(Constants.LOG_TAG, "StudentHighlightsActivity navigate back in HighlightsViewDrawer but classroom is null; default to classes index.");
                    Intent intent = new Intent(StudentHighlightsActivity.this, org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.classrooms.ClassroomIndexActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });

        //get student from intent - will come from classroomhighlights
        if(getIntent().getStringExtra(ClassroomHighlightsActivity.EXTRA_CLASSROOM_NAME) != null && getIntent().getSerializableExtra(ClassroomHighlightsActivity.EXTRA_STUDENT) != null) {
            this.classroomName = getIntent().getStringExtra(ClassroomHighlightsActivity.EXTRA_CLASSROOM_NAME);
            this.student = (Student) getIntent().getSerializableExtra(ClassroomHighlightsActivity.EXTRA_STUDENT);
            this.studentUuid = student.getUuid();
        }


        //gets student from uuid if not in intent
            if(getIntent().getStringExtra(UploadPhotoActivity.STUDENT_UUID) !=null){

            this.studentUuid = getIntent().getStringExtra(UploadPhotoActivity.STUDENT_UUID);
            this.classroomName = getIntent().getStringExtra(UploadPhotoActivity.EXTRA_CLASSROOM_NAME);
           //get student object and update UI
            AppDatabase.getInstance(this).studentDAO().getStudent(studentUuid).observe(this, new Observer<Student>() {
                @Override
                public void onChanged(@Nullable Student student) {
                    StudentHighlightsActivity.this.student = student;

                    if (StudentHighlightsActivity.this.student != null) {
                        //update UI with student info -- need to call here too cause async observer
                        initStudentPic();
                        initEmotionsGrid();
                        initMostUsedGrid();
                    }

                }
            });
        }


        //process picture from uploadPhotoActivity
        Intent intent = getIntent();
        if(intent.getStringExtra("path") !=null){
            int resultCode = intent.getIntExtra("resultCode", -99);
            int requestCode = intent.getIntExtra("requestCode", -99);
            handleResult(requestCode, resultCode, intent);
        }

        if(student != null){
            initStudentPic();
            initEmotionsGrid();
            initMostUsedGrid();
        }

        //delete student listener
        View.OnClickListener deleteStudentListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(StudentHighlightsActivity.this);
                builder.setMessage(R.string.alert_message_delete_student);
                builder.setTitle(R.string.alert_title_delete_student);
                builder.setPositiveButton(R.string.alert_option_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteAndFinish();
                    }
                });
                builder.setNegativeButton(R.string.alert_option_cancel, null);
                builder.create().show();
            }
        };

        //init delete button
        findViewById(R.id.trashStudentButton).setOnClickListener(deleteStudentListener);
        findViewById(R.id.deleteStudent).setOnClickListener(deleteStudentListener);

    }

    private void deleteAndFinish(){
        Log.d(Constants.LOG_TAG, "performing DB delete");
        new UpdateStudentModelAsyncTask(AppDatabase.getInstance(getApplicationContext()), UpdateStudentModelAsyncTask.ActionType.DELETE, student, null, new UpdateStudentModelAsyncTask.PostExecute() {
            @Override
            public void onPostExecute(Boolean modelSaved) {
                if (!modelSaved) {
                    Toast.makeText(getApplicationContext(), "Could not save changes to Student", Toast.LENGTH_LONG).show();
                }
                AppDatabase.getInstance(getApplicationContext()).classroomDAO().getClassroom(student.getClassroomUuid()).observe(StudentHighlightsActivity.this, new Observer<Classroom>() {
                    @Override
                    public void onChanged(@Nullable Classroom classroom) {
                        Intent intent = new Intent(StudentHighlightsActivity.this, ClassroomHighlightsActivity.class);
                        intent.putExtra(ManageClassroomActivityWithHeaderAndDrawer.EXTRA_CLASSROOM, classroom);
                        startActivity(intent);
                    }
                });

            }
        }).execute();
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._highlights_design__activity_student_highlights;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // TODO helper class for requestCode and resultCode (determines which emotion/profile photo to update)
        handleResult(requestCode, resultCode, data);
        GlobalHandler.getInstance(getApplicationContext()).isRunningActivityForImageResult = false;

    }

    private void handleResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            launchUploadPhotoActivity(requestCode, resultCode, data);
        }
    }

    private void launchUploadPhotoActivity(int requestCode, int resultCode, Intent data){

        if (data != null) {
            Log.v(Constants.LOG_TAG, String.format("StudentHighlightsActivity got result from photo activity with resultCode=%d AND data not null", resultCode));

            Intent  intent = new Intent(StudentHighlightsActivity.this, UploadPhotoActivity.class);
            Uri imageUri = data.getParcelableExtra(CameraActivity.RESULT_INTENT_EXTRA_IMAGE_URI);

            // TODO @Dante intent extras should be defined within Activity class (e.g. "UploadPhotoActivity.EXTRA_CLASSROOM" below)
            if(student != null && classroomName != null) {

                intent.putExtra(UploadPhotoActivity.EXTRA_STUDENT, studentUuid);
                intent.putExtra(UploadPhotoActivity.EXTRA_CLASSROOM_NAME, classroomName);
            }
            intent.putExtra(UploadPhotoActivity.EXTRA_CLASSROOM, classroom);

            boolean fromFileOrVertical = data.getBooleanExtra("fromFiles", false);
            intent.putExtra("fromFiles", fromFileOrVertical);
            intent.putExtra(CameraActivity.RESULT_INTENT_EXTRA_IMAGE_URI, imageUri);
            intent.putExtra("requestCode", requestCode);

            startActivity(intent);

        } else {
            Log.v(Constants.LOG_TAG, String.format("StudentHighlightsActivity got result from photo activity with resultCode=%d (data null)", resultCode));
        }

    }

}



