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

    private final MediatorLiveData<List<AssignmentEntity>> observableOwnAssignments;

    public AssignmentListViewModel(@NonNull Application application,
                                   final String ownerId,
                                   AssignmentRepository assignmentRepository){
        super(application);

        this.application = application;
        repository= assignmentRepository;

        observableOwnAssignments = new MediatorLiveData<>();
        observableOwnAssignments.setValue(null);

        LiveData<List<AssignmentEntity>> ownAssignments = repository.getByOwner(ownerId,application);

        observableOwnAssignments.addSource(ownAssignments,observableOwnAssignments::setValue);
    }

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
            return (T) new AssignmentListViewModel(application,ownerId,assignmentRepository);
        }
    }

    public LiveData<List<AssignmentEntity>> getOwnAssignments(){return observableOwnAssignments;}

    public void deleteAssignment(AssignmentEntity assignment, OnAsyncEventListener callback){
        repository.delete(assignment,callback,application);
    }
}
