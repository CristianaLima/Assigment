package com.example.assigmentemiliecristiana.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Database;

import com.example.assigmentemiliecristiana.BaseApp;
import com.example.assigmentemiliecristiana.database.AppDatabase;
import com.example.assigmentemiliecristiana.database.async.assignment.CreateAssignment;
import com.example.assigmentemiliecristiana.database.async.assignment.DeleteAssignment;
import com.example.assigmentemiliecristiana.database.async.assignment.UpdateAssignment;
import com.example.assigmentemiliecristiana.database.entity.AssignmentEntity;
import com.example.assigmentemiliecristiana.util.OnAsyncEventListener;

import java.util.List;

public class AssignmentRepository {

    private static AssignmentRepository instance;

    private AssignmentRepository() {
    }

    public static AssignmentRepository getInstance(){
        if (instance==null){
            synchronized (AssignmentRepository.class){
                if (instance==null){
                    instance= new AssignmentRepository();
                }
            }
        }
        return instance;
    }

    public LiveData<AssignmentEntity> getAssignment(final Long assignmentId, Application application){
        return ((BaseApp) application).getDatabase().assignmentDao().getById(assignmentId);
    }

    public LiveData<List<AssignmentEntity>> getByOwner(final String owner, Application application){
        return ((BaseApp) application).getDatabase().assignmentDao().getOwned(owner);
    }

    public LiveData<List<AssignmentEntity>> getByDate(final String owner, final Long date, Application application){
        return ((BaseApp) application).getDatabase().assignmentDao().getAssigByDate(owner, date);
    }

    public void insert(final AssignmentEntity assignment, OnAsyncEventListener callback,
                       Application application){
        new CreateAssignment(application,callback).execute(assignment);
    }

    public void update(final AssignmentEntity assignment, OnAsyncEventListener callback,
                       Application application){
        new UpdateAssignment(application,callback).execute(assignment);
    }

    public void delete(final AssignmentEntity assignment, OnAsyncEventListener callback,
                       Application application){
        new DeleteAssignment(application,callback).execute(assignment);
    }
}
