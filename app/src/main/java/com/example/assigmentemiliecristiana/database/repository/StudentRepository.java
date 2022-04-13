package com.example.assigmentemiliecristiana.database.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.assigmentemiliecristiana.BaseApp;
import com.example.assigmentemiliecristiana.database.entity.StudentEntity;
import com.example.assigmentemiliecristiana.database.firebase.StudentLiveData;
import com.example.assigmentemiliecristiana.util.OnAsyncEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudentRepository {
    private static final String TAG = "StudentRepository";
    private static StudentRepository instance;

    private StudentRepository() {
    }

    public static StudentRepository getInstance(){
        if (instance == null){
            synchronized (AssignmentRepository.class){
                if (instance ==null){
                    instance = new StudentRepository();
                }
            }
        }
        return instance;
    }

    public void signIn(final String email, final String password, final OnCompleteListener<AuthResult> listener){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(listener);
    }

    public LiveData<StudentEntity> getStudent(final String studentId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("students").child(studentId);
        return new StudentLiveData(reference);
    }

    public void register(final StudentEntity student, final OnAsyncEventListener callback){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                student.getEmail(),
                student.getPassword()
        ).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                student.setId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                insert(student,callback);
            } else {
                callback.onFailure(task.getException());
            }
        });
    }

    public void insert(final StudentEntity student, final OnAsyncEventListener callback){
        FirebaseDatabase.getInstance()
                .getReference("students")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(student.toMap(), (databaseError, databaseReference) ->{
                    if (databaseError!= null){
                        callback.onFailure(databaseError.toException());
                        FirebaseAuth.getInstance().getCurrentUser().delete()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        callback.onFailure(null);
                                        Log.d(TAG, "Rollback successful: User assignment deleted");
                                    } else {
                                        callback.onFailure(task.getException());
                                        Log.d(TAG, "Rollback failed: signInWithEmail:failure",
                                                task.getException());
                                    }
                                });
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void update(final StudentEntity student, OnAsyncEventListener callback){
        FirebaseDatabase.getInstance()
                .getReference("students")
                .child(student.getId())
                .updateChildren(student.toMap(), (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
        FirebaseAuth.getInstance().getCurrentUser().updatePassword(student.getPassword())
                .addOnFailureListener(
                        e -> Log.d(TAG, "updatePassword failure!", e)
                );
    }

    public void delete(final StudentEntity student, OnAsyncEventListener callback){
        FirebaseDatabase.getInstance()
                .getReference("students")
                .child(student.getId())
                .removeValue((databaseError, databaseReference) -> {
            if (databaseError != null) {
                callback.onFailure(databaseError.toException());
            } else {
                callback.onSuccess();
            }
        });
        FirebaseAuth.getInstance().getCurrentUser().delete();
    }

}
