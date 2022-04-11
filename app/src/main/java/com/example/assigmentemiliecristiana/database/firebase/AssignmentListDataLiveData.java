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


public class AssignmentListDataLiveData extends LiveData<List<AssignmentEntity>> {
    private static final String TAG = "AssignmentListDataLiveData";

    private final DatabaseReference reference;
    private final String owner;
    private final Long date;
    private final AssignmentListDataLiveData.MyValueEventListener listener =
            new AssignmentListDataLiveData.MyValueEventListener();


    public AssignmentListDataLiveData(DatabaseReference reference, String owner, Long date) {
        this.reference = reference;
        this.owner = owner;
        this.date = date;
    }

    @Override
    protected void onActive() {
        Log.d(TAG, "onActive");
        reference.addValueEventListener(listener);
    }

    @Override
    protected void onInactive() {
        Log.d(TAG, "onInactive");
    }

    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            setValue(toAssignmentListDate(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }

    }
    private List<AssignmentEntity> toAssignmentListDate(DataSnapshot snapshot){
        List<AssignmentEntity> assignmentEntities = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()){
            if (childSnapshot.getKey().equals(owner) && childSnapshot.getValue(AssignmentEntity.class).getDate().equals(date)){
                AssignmentEntity assignmentEntity = childSnapshot.getValue(AssignmentEntity.class);
                assignmentEntity.setId(childSnapshot.getKey());
                assignmentEntity.setOwner(owner);
                assignmentEntities.add(assignmentEntity);

            }
        }
        return assignmentEntities;
    }


}
