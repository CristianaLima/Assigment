package com.example.assigmentemiliecristiana.viewmodel.assignment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.assigmentemiliecristiana.BaseApp;
import com.example.assigmentemiliecristiana.database.entity.AssignmentEntity;
import com.example.assigmentemiliecristiana.database.repository.AssignmentRepository;
import com.example.assigmentemiliecristiana.util.OnAsyncEventListener;

import java.util.List;

public class AssignmentListViewModel extends AndroidViewModel {
    private Application application;
    private AssignmentRepository repository;
    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<AssignmentEntity>> observableOwnAssignments;

    public AssignmentListViewModel(@NonNull Application application,
                                   final String ownerId,
                                   AssignmentRepository assignmentRepository){
        super(application);

        this.application = application;
        repository= assignmentRepository;

        observableOwnAssignments = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableOwnAssignments.setValue(null);

        LiveData<List<AssignmentEntity>> ownAssignments = repository.getByOwner(ownerId,application);
        // observe the changes of the assignment entity from the database and forward them
        observableOwnAssignments.addSource(ownAssignments,observableOwnAssignments::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory{
        @NonNull
        private final Application application;
        private final String ownerId;
        private final AssignmentRepository assignmentRepository;

        public Factory(@NonNull Application application, String ownerId) {
            this.application = application;
            this.ownerId = ownerId;
            assignmentRepository = ((BaseApp) application).getAssignmentRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new AssignmentListViewModel(application,ownerId,assignmentRepository);
        }
    }

    /**
     * Expose the LiveData AssignmentEntity query so the UI can observe it.
     */
    public LiveData<List<AssignmentEntity>> getOwnAssignments(){return observableOwnAssignments;}

}
