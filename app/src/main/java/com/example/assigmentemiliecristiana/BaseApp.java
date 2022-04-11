package com.example.assigmentemiliecristiana;

import android.app.Application;

import com.example.assigmentemiliecristiana.database.repository.AssignmentRepository;
import com.example.assigmentemiliecristiana.database.repository.StudentRepository;
/**
 * Android Application class. Used for accessing singletons.
 */
public class BaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this);
    }

    public AssignmentRepository getAssignmentRepository() {
        return AssignmentRepository.getInstance();
    }

    public StudentRepository getStudentRepository() {
        return StudentRepository.getInstance();
    }
}
