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

    private final MediatorLiveData<AssignmentEntity> observableAssigment;

    public AssignmentViewModel(@NonNull Application application,
                               final Long ownerId,
                               AssignmentRepository assignmentRepository){
        super(application);

        this.application= application;
        repository = assignmentRepository;

        observableAssigment = new MediatorLiveData<>();
        observableAssigment.setValue(null);

        LiveData<AssignmentEntity> assignment = repository.getAssignment(ownerId,application);

        observableAssigment.addSource(assignment,observableAssigment::setValue);
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory{
        @NonNull
        private final Application application;
        private final Long assignmentId;
        private final AssignmentRepository repository;

        public Factory(@NonNull Application application, Long assignmentId) {
            this.application = application;
            this.assignmentId = assignmentId;
            repository= ((BaseApp) application).getAssignmentRepository();
        }

        @Override
        public <T extends ViewModel> T create( Class<T> modelClass) {
            return (T) new AssignmentViewModel(application,assignmentId,repository);
        }
    }

    public LiveData<AssignmentEntity> getAssignment(){return observableAssigment;}

    public void createAssignment(AssignmentEntity assignment, OnAsyncEventListener callback){
        repository.insert(assignment,callback,application);
    }

    public void updateAssignment(AssignmentEntity assignment, OnAsyncEventListener callback){
        repository.update(assignment,callback,application);
    }

    public void deleteAssignment(AssignmentEntity assignment, OnAsyncEventListener callback){
        repository.delete(assignment,callback,application);
    }
}
