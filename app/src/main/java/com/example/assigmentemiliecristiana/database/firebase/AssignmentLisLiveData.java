package com.example.assigmentemiliecristiana.database.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.assigmentemiliecristiana.database.entity.AssignmentEntity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AssignmentLisLiveData extends LiveData<List<AssignmentEntity>> {

    private static final String TAG= "AssignmentListLiveData";

    private final DatabaseReference reference;
    private final String owner;
    private final MyValueEventListener listener = new MyValueEventListener();


    public AssignmentLisLiveData(DatabaseReference reference, String owner) {
        this.reference = reference;
        this.owner = owner;
    }

    @Override
    protected void onActive() {
        Log.d(TAG,"onActive");
        reference.addValueEventListener(listener);
    }

    @Override
    protected void onInactive() {
        Log.d(TAG, "onInactive");
    }

    private class MyValueEventListener implements ValueEventListener{
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            setValue(toAssignments(snapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.e(TAG, "Can't listen to query " + reference, error.toException());
        }
    }

    private List<AssignmentEntity> toAssignments(DataSnapshot snapshot){
        List<AssignmentEntity> assignmentEntities = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()){
            AssignmentEntity entity = childSnapshot.getValue(AssignmentEntity.class);
            entity.setId(childSnapshot.getKey());
            entity.setOwner(owner);
            assignmentEntities.add(entity);
        }
        return assignmentEntities;
    }
}
