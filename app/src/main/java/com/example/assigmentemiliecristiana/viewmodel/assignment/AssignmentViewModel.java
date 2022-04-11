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

public class AssignmentViewModel extends AndroidViewModel {
    private Application application;
    private AssignmentRepository repository;
    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<AssignmentEntity> observableAssigment;

    public AssignmentViewModel(@NonNull Application application,
                               final String ownerId,
                               AssignmentRepository assignmentRepository){
        super(application);

        this.application= application;
        repository = assignmentRepository;

        observableAssigment = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableAssigment.setValue(null);

        LiveData<AssignmentEntity> assignment = repository.getAssignment(ownerId);
        // observe the changes of the assignment entity from the database and forward them
        observableAssigment.addSource(assignment,observableAssigment::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory{
        @NonNull
        private final Application application;
        private final String assignmentId;
        private final AssignmentRepository repository;

        public Factory(@NonNull Application application, String assignmentId) {
            this.application = application;
            this.assignmentId = assignmentId;
            repository= ((BaseApp) application).getAssignmentRepository();
        }

        @Override
        public <T extends ViewModel> T create( Class<T> modelClass) {
            //noinspection unchecked
            return (T) new AssignmentViewModel(application,assignmentId,repository);
        }
    }

    /**
     * Expose the LiveData AssignmentEntity query so the UI can observe it.
     */
    public LiveData<AssignmentEntity> getAssignment(){return observableAssigment;}

    public void createAssignment(AssignmentEntity assignment, OnAsyncEventListener callback){
        repository.insert(assignment,callback);
    }

    public void updateAssignment(AssignmentEntity assignment, OnAsyncEventListener callback){
        repository.update(assignment,callback);
    }

    public void deleteAssignment(AssignmentEntity assignment, OnAsyncEventListener callback){
        repository.delete(assignment,callback);
    }
}
