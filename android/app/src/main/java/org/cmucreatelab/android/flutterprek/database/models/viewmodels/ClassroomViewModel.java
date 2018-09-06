package org.cmucreatelab.android.flutterprek.database.models.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import org.cmucreatelab.android.flutterprek.database.models.Classroom;
import org.cmucreatelab.android.flutterprek.database.models.repositories.ClassroomRepository;

import java.util.List;

public class ClassroomViewModel extends AndroidViewModel {

    private ClassroomRepository repository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private LiveData<List<Classroom>> allClassrooms;

    public ClassroomViewModel (Application application) {
        super(application);
        repository = new ClassroomRepository(application);
        allClassrooms = repository.getAllClassrooms();
    }

    public LiveData<List<Classroom>> getAllClassrooms() { return allClassrooms; }

    public void insert(Classroom classroom) { repository.insert(classroom); }
}
