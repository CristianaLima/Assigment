package com.example.assigmentemiliecristiana.database.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.example.assigmentemiliecristiana.database.entity.AssignmentEntity;
import com.google.firebase.database.ValueEventListener;

public class AssignmentLiveData extends LiveData<AssignmentEntity> {
    private static final String TAG = "AssignmentLiveData";

    private final DatabaseReference reference;
    private final String owner;
    private final AssignmentLiveData.MyValueEventListener listener = new AssignmentLiveData.MyValueEventListener();


    public AssignmentLiveData(DatabaseReference reference) {
        this.reference = reference;
        this.owner = reference.getParent().getKey();
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

    private class MyValueEventListener implements ValueEventListener{
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            AssignmentEntity entity = snapshot.getValue(AssignmentEntity.class);
            entity.setId(snapshot.getKey());
            entity.setOwner(owner);
            setValue(entity);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.e(TAG,"Can't listen to query " + reference, error.toException());
        }
    }
}
