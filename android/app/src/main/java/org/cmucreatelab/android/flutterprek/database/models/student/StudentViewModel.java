package org.cmucreatelab.android.flutterprek.database.models.student;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

/**
 * Created by tasota on 9/13/2018.
 *
 * StudentViewModel
 *
 * ViewModel for classrooms. The purpose of ViewModel is to provide data to the UI.
 * See Room persistence library documentation for details:
 *   https://developer.android.com/training/data-storage/room/accessing-data
 */
public final class StudentViewModel extends AndroidViewModel {

    private StudentRepository repository;
    private LiveData<List<Student>> allStudents;


    StudentViewModel(Application application) {
        super(application);
        repository = new StudentRepository(application);
        allStudents = repository.getAllStudents();
    }


    public LiveData<List<Student>> getAllStudents() {
        return allStudents;
    }


    public void insert(Student student) {
        repository.insert(student);
    }

}
