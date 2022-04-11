package com.example.assigmentemiliecristiana.database.firebase;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.assigmentemiliecristiana.database.entity.StudentEntity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class StudentLiveData extends LiveData<StudentEntity> {
    private static final String TAG = "StrudentLiveData";

    private final DatabaseReference reference;
    private final StudentLiveData.MyValueEventListener listener = new   StudentLiveData.MyValueEventListener();


    public StudentLiveData(DatabaseReference reference) {
        this.reference = reference;
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
            StudentEntity entity = snapshot.getValue(StudentEntity.class);
            entity.setId(snapshot.getKey());
            setValue(entity);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.e(TAG, "Can't listen to query " + reference, error.toException());
        }
    }
}
