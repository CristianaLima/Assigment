package com.example.assigmentemiliecristiana.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.assigmentemiliecristiana.BaseApp;
import com.example.assigmentemiliecristiana.database.async.student.CreateStudent;
import com.example.assigmentemiliecristiana.database.async.student.DeleteStudent;
import com.example.assigmentemiliecristiana.database.async.student.UpdateStudent;
import com.example.assigmentemiliecristiana.database.entity.StudentEntity;
import com.example.assigmentemiliecristiana.util.OnAsyncEventListener;


public class StudentRepository {
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

    public LiveData<StudentEntity> getStudent(final String studentId, Application application){
        return ((BaseApp) application).getDatabase().studentDao().getById(studentId);
    }

    public void insert(final StudentEntity student, OnAsyncEventListener callback,
                       Application application){
        new CreateStudent(application,callback).execute(student);
    }

    public void update(final StudentEntity student, OnAsyncEventListener callback,
                       Application application){
        new UpdateStudent(application,callback).execute(student);
    }

    public void delete(final StudentEntity student, OnAsyncEventListener callback,
                       Application application){
        new DeleteStudent(application, callback).execute(student);
    }

}
