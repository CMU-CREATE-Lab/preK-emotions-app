package org.cmucreatelab.android.flutterprek.database.models.classroom;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

/**
 * Created by tasota on 9/6/2018.
 *
 * ClassroomViewModel
 *
 * ViewModel for classrooms. The purpose of ViewModel is to provide data to the UI.
 * See Room persistence library documentation for details:
 *   https://developer.android.com/training/data-storage/room/accessing-data
 */
public final class ClassroomViewModel extends AndroidViewModel {

    private ClassroomRepository repository;
    private LiveData<List<Classroom>> allClassrooms;


    ClassroomViewModel (Application application) {
        super(application);
        repository = new ClassroomRepository(application);
        allClassrooms = repository.getAllClassrooms();
    }


    public LiveData<List<Classroom>> getAllClassrooms() {
        return allClassrooms;
    }


    public void insert(Classroom classroom) {
        repository.insert(classroom);
    }

}
