package org.cmucreatelab.android.flutterprek.activities;

import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.Classroom;
import org.cmucreatelab.android.flutterprek.database.models.viewmodels.ClassroomViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ClassroomViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //final AppDatabase db = AppDatabase.getDatabase(getApplicationContext());

        viewModel = ViewModelProviders.of(this).get(ClassroomViewModel.class);

        viewModel.getAllClassrooms().observe(this, new Observer<List<Classroom>>() {
            @Override
            public void onChanged(@Nullable final List<Classroom> classrooms) {
                Log.i("flutterprek","onChanged");
                Log.i("flutterprek","classroom size = " + classrooms.size());
//                if (classrooms.size() > 0) {
//                    Log.i("flutterprek", "not creating new classroom since at least 1 classroom entry already exists.");
//                } else {
//                    Log.i("flutterprek", "creating flutterprek DB");
//                    Classroom classroom = new Classroom();
//                    classroom.setId(1);
//                    classroom.setName("First Classroom");
//                    viewModel.insert(classroom);
//                }
            }
        });

//        if (db.classroomDAO().getAllClassrooms().size() > 0) {
//            Log.i("flutterprek", "not creating new classroom since at least 1 classroom entry already exists.");
//        } else {
//            Log.i("flutterprek", "creating flutterprek DB");
//            Classroom classroom = new Classroom();
//            classroom.setId(1);
//            classroom.setName("First Classroom");
//            db.classroomDAO().insert(classroom);
//        }
    }
}
