package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zigis.segmentedarcview.SegmentedArcView;
import com.zigis.segmentedarcview.custom.ArcSegment;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.Util;
import org.cmucreatelab.android.flutterprek.activities.adapters.CopingSkillHighlightWCIndexAdapter;
import org.cmucreatelab.android.flutterprek.activities.adapters.EmotionHighlightAdapter;
import org.cmucreatelab.android.flutterprek.activities.adapters.EmotionIndexAdapter;
import org.cmucreatelab.android.flutterprek.activities.student_section.choose_emotion.ChooseEmotionAbstractActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.choose_emotion.DisplayEmotionActivity;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.classrooms.ManageClassroomActivityWithHeaderAndDrawer;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.students.StudentEditActivity;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.students.StudentUpdateAbstractActivity;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.students.UpdateStudentModelAsyncTask;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.CopingSkillWithCustomizations;
import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;
import org.cmucreatelab.android.flutterprek.database.models.db_file.DbFile;
import org.cmucreatelab.android.flutterprek.database.models.emotion.Emotion;
import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.ItineraryItem;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;
import org.cmucreatelab.android.mylibrary.CameraActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StudentHighlightsActivity extends HighlightsDesignActivityWithHeaderAndDrawer {

    public static final int HAPPY_CODE = 100;
    public static final int SAD_CODE = 101;
    public static final int ANGRY_CODE = 102;
    public static final int SCARED_CODE = 103;
    public static final int EXCITED_CODE = 104;
    public static final int STUDENT_CODE = 105;
    public static final String EXTRA_CLASSROOM_NAME = "classroom_name";
    public static final String EXTRA_STUDENT = "student";

    private Button buttonAngry, buttonHappy, buttonSad;
    private String classroomName;
    private String studentUuid;
    private Student student;

    private final EmotionHighlightAdapter.ClickListener emotionsListener = new EmotionHighlightAdapter.ClickListener() {
        @Override
        public void onClick(Emotion emotion, List<ItineraryItem> itineraryItems) {
            GlobalHandler.getInstance(getApplicationContext()).isRunningActivityForImageResult = true;
            Intent intent = new Intent(StudentHighlightsActivity.this, CameraActivity.class);
            intent.putExtra(EXTRA_STUDENT, studentUuid);
            intent.putExtra(EXTRA_CLASSROOM_NAME, classroomName);

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

        if(student != null){
            initStudentPic();
            initEmotionsGrid();
            initMostUsedGrid();
        }
        TextView textView = findViewById(R.id.buttonPlaceholder);
        String text = "Back to " + classroomName;
        textView.setText(text);
        textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AppDatabase.getInstance(getApplicationContext()).classroomDAO().getClassroom(student.getClassroomUuid()).observe(StudentHighlightsActivity.this, new Observer<Classroom>() {
                    @Override
                    public void onChanged(@Nullable Classroom classroom) {
                        Intent intent = new Intent(StudentHighlightsActivity.this, ClassroomHighlightsActivity.class);
                        intent.putExtra(ManageClassroomActivityWithHeaderAndDrawer.EXTRA_CLASSROOM, classroom);
                        startActivity(intent);
                    }
                });
            }
        });

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

    private void initStudentPic(){
        ImageView profilePicture = findViewById(R.id.profilePicture);

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


        //setRings();
        setArc();
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchProfiePictureActivity();

            }

        });

        findViewById(R.id.cameraIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchProfiePictureActivity();
            }
        });
    }

    private void launchProfiePictureActivity(){
        GlobalHandler.getInstance(getApplicationContext()).isRunningActivityForImageResult = true;
        Intent intent = new Intent(StudentHighlightsActivity.this, CameraActivity.class);
        intent.putExtra(EXTRA_STUDENT, studentUuid);
        intent.putExtra(EXTRA_CLASSROOM_NAME, classroomName);
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
        LiveData<List<Emotion>> liveData = AppDatabase.getInstance(this).emotionDAO().getAllEmotions();
        liveData.observe(this, new Observer<List<Emotion>>() {
            @Override
            public void onChanged(@Nullable List<Emotion> emotions) {
                GridView emotionsGridView = findViewById(R.id.emotionsGridView);
                emotionsGridView.setAdapter(new EmotionHighlightAdapter(StudentHighlightsActivity.this, emotions, emotionsListener));
            }
        });
    }

    private void initMostUsedGrid(){
        CopingSkillsHighlightGridView copingSkillsView = findViewById(R.id.copingSkillsCustomView);

        AppDatabase.getInstance(this).copingSkillDAO().getAllCopingSkillsWithCustomizations().observe(this, new Observer<List<CopingSkillWithCustomizations>>() {
            @Override
            public void onChanged(@Nullable List<CopingSkillWithCustomizations> copingSkillsWithCustomizations) {
                List<Integer> percents = new ArrayList<>();
                percents.add(60);
                percents.add(20);
                percents.add(10);
                percents.add(10);

                copingSkillsView.setAdapter(new CopingSkillHighlightWCIndexAdapter(StudentHighlightsActivity.this, copingSkillsWithCustomizations,percents));
            }
        });
    }

//    private void setRings() {
//
//        SegmentedArcView sa = findViewById(R.id.studentArcView);
//
//        List<ArcSegment> segments = new ArrayList<>();
//        segments.add(new ArcSegment(Color.RED, Color.RED,false, 45f));    // 45 degrees
//        segments.add(new ArcSegment(Color.GREEN, Color.GREEN,false, 90f)); // 90 degrees
//        segments.add(new ArcSegment(Color.BLUE, Color.BLUE,false, 225f));  // 225 degrees
//
//        // Set the segments (custom sweep angles are taken from constructor)
//        sa.setSegments(segments);
//
//    }

    private void setArc(){
        ArcViewOverlay arcView = findViewById(R.id.arcViewOverlay);
        List<Integer> colors = Arrays.asList(Color.RED, Color.GREEN, Color.BLUE);
        List<Float> angles = Arrays.asList(120f, 120f, 120f);

        arcView.setSegmentColors(colors);
        arcView.setSegmentAngles(angles);
        arcView.setArcWidth(20f);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpDrawer();
        //get student from intent
        if(getIntent().getStringExtra(EXTRA_CLASSROOM_NAME) != null && getIntent().getSerializableExtra(EXTRA_STUDENT) != null) {
            this.classroomName = getIntent().getStringExtra(EXTRA_CLASSROOM_NAME);
            this.student = (Student) getIntent().getSerializableExtra(EXTRA_STUDENT);
            this.studentUuid = student.getUuid();
        }


            if(getIntent().getStringExtra(UploadPhotoActivity.STUDENT_UUID) !=null){
            Log.v("penguin", "studentUuid");

            this.studentUuid = getIntent().getStringExtra(UploadPhotoActivity.STUDENT_UUID);
            this.classroomName = getIntent().getStringExtra(UploadPhotoActivity.EXTRA_CLASSROOM_NAME);
           //get student object and update UI
            AppDatabase.getInstance(this).studentDAO().getStudent(studentUuid).observe(this, new Observer<Student>() {
                @Override
                public void onChanged(@Nullable Student student) {
                    StudentHighlightsActivity.this.student = student;
                    Log.v("penguin", studentUuid);

                    if (StudentHighlightsActivity.this.student != null) {

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
            int resultCode = intent.getIntExtra("resultCode", -1);
            int requestCode = intent.getIntExtra("requestCode", -1);
            handleResult(requestCode, resultCode, intent);
        }


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
      switch(resultCode) {
          case Activity.RESULT_OK:
              launchUploadPhotoActivity(requestCode, resultCode, data);
              break;

          case Activity.RESULT_CANCELED:
              break;

          case UploadPhotoActivity.UPDATE:
              break;

          case UploadPhotoActivity.KEEP:
              break;

          case UploadPhotoActivity.RESET:
              break;

      }
    }

    private void launchUploadPhotoActivity(int requestCode, int resultCode, Intent data){

        if (data != null) {
            Log.v(Constants.LOG_TAG, String.format("StudentHighlightsActivity got result from photo activity with resultCode=%d AND data not null", resultCode));

            Intent  intent = new Intent(StudentHighlightsActivity.this, UploadPhotoActivity.class);
            Uri imageUri = data.getParcelableExtra(CameraActivity.RESULT_INTENT_EXTRA_IMAGE_URI);

            if(student != null && classroomName != null) {
//                this.classroomName = getIntent().getStringExtra(EXTRA_CLASSROOM_NAME);
//                this.student = (Student) getIntent().getSerializableExtra(EXTRA_STUDENT);
//                this.studentUuid = student.getUuid();
                intent.putExtra(EXTRA_STUDENT, studentUuid);
                intent.putExtra(EXTRA_CLASSROOM_NAME, classroomName);
            }

            boolean fromFile = data.getBooleanExtra("fromFiles", false);
            intent.putExtra("fromFiles", fromFile);
            intent.putExtra(CameraActivity.RESULT_INTENT_EXTRA_IMAGE_URI, imageUri);
            intent.putExtra("requestCode", requestCode);

            startActivity(intent);

        } else {
            Log.v(Constants.LOG_TAG, String.format("StudentHighlightsActivity got result from photo activity with resultCode=%d (data null)", resultCode));
        }

    }

    private void saveImage(){
        return;
    }

}



