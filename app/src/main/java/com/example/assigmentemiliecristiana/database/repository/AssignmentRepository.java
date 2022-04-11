package com.example.assigmentemiliecristiana.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.assigmentemiliecristiana.database.firebase.AssignmentLisLiveData;
import com.example.assigmentemiliecristiana.database.firebase.AssignmentListDataLiveData;
import com.example.assigmentemiliecristiana.database.firebase.AssignmentLiveData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.example.assigmentemiliecristiana.BaseApp;
import com.example.assigmentemiliecristiana.database.entity.AssignmentEntity;
import com.example.assigmentemiliecristiana.util.OnAsyncEventListener;

import java.util.List;

public class AssignmentRepository {

    private static AssignmentRepository instance;

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

    public LiveData<AssignmentEntity> getAssignment(final String assignmentId){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("students")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("assignments")
                .child(assignmentId);
        return new AssignmentLiveData(reference);
    }

    public LiveData<List<AssignmentEntity>> getByOwner(final String owner){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("students")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("assignments");
        return new AssignmentLisLiveData(reference, owner);
    }

    public LiveData<List<AssignmentEntity>> getByDate(final String owner, final Long date){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("students")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("assignments");
        return new AssignmentListDataLiveData(reference,owner,date);
    }

    public void insert(final AssignmentEntity assignment, final OnAsyncEventListener callback){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("students")
                .child(assignment.getOwner())
                .child("assignments");
        String key = reference.push().getKey();
        FirebaseDatabase.getInstance()
                .getReference("students")
                .child(assignment.getOwner())
                .child("assignments")
                .child(key)
                .setValue(assignment,(databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void update(final AssignmentEntity assignment, OnAsyncEventListener callback){
       FirebaseDatabase.getInstance()
            .getReference("students")
               .child(assignment.getOwner())
               .child("assignments")
               .child(assignment.getId())
               .updateChildren(assignment.toMap(), (databaseError, databaseReference) -> {
                   if (databaseError != null) {
                       callback.onFailure(databaseError.toException());
                   } else {
                       callback.onSuccess();
                   }
               });
    }

    public void delete(final AssignmentEntity assignment, OnAsyncEventListener callback){
        FirebaseDatabase.getInstance()
                .getReference("students")
                .child(assignment.getOwner())
                .child("assignments")
                .child(assignment.getId())
                .removeValue((databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }
}
