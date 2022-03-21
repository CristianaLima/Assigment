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

import java.util.List;

public class AssignmentListDateViewModel extends AndroidViewModel {
    private Application application;
    private AssignmentRepository repository;

    private final MediatorLiveData<List<AssignmentEntity>> observableDateAssignments;

    public AssignmentListDateViewModel(@NonNull Application application,
                                   final String ownerId, Long date,
                                   AssignmentRepository assignmentRepository){
        super(application);
        this.application = application;
        repository= assignmentRepository;

        observableDateAssignments = new MediatorLiveData<>();
        observableDateAssignments.setValue(null);

        LiveData<List<AssignmentEntity>> dateAssignments = repository.getByDate(ownerId,date,application);

        observableDateAssignments.addSource(dateAssignments, observableDateAssignments::setValue);

    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory{
        @NonNull
        private final Application application;
        private final String ownerId;
        private final Long date;
        private final AssignmentRepository assignmentRepository;

        public Factory(@NonNull Application application, String ownerId, Long date) {
            this.application = application;
            this.ownerId = ownerId;
            assignmentRepository = ((BaseApp) application).getAssignmentRepository();
            this.date = date;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new AssignmentListDateViewModel(application,ownerId,date,assignmentRepository);
        }
    }

    public LiveData<List<AssignmentEntity>> getDateAssignments(){return observableDateAssignments;}

}
