package com.example.assigmentemiliecristiana.database;

import android.os.AsyncTask;
import android.util.Log;

import com.example.assigmentemiliecristiana.database.entity.AssignmentEntity;
import com.example.assigmentemiliecristiana.database.entity.StudentEntity;

import java.util.Date;

public class DatabaseInitializer {
    public static final String TAG = "DatabaseInitializer";

    public static void populateDatabase(final AppDatabase db) {
        Log.i(TAG, "Inserting demo data.");
        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }
    private static void addStudent(final AppDatabase db, final String email, final String username,
                                   final String password) {
        StudentEntity student = new StudentEntity(username, email, password);
        db.studentDao().insert(student);
    }

    private static void addAssignment(final AppDatabase db, final String name, final String type,
                                      final String owner, final String description,final Long date,
                                      final String status, final String course) {
        AssignmentEntity assignmentEntity = new AssignmentEntity(name,type,description,date,status,owner, course);
        db.assignmentDao().insert(assignmentEntity);
    }

    private static void populateWithTestData(AppDatabase db){
        db.studentDao().deleteAll();

        addStudent(db,"admin@gmail.com", "admin","admin");

        try {
            // Let's ensure that the clients are already stored in the database before we continue.
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        addAssignment(db,
                "Projet Mediator", "Project", "admin",
                "give back project",new Date(2022,03,
                        17).getTime(),"To do","Pattern");

        addAssignment(db,
                "TEST","Examen","admin",
                "Examen", new Date(2022,04,01).getTime(),"To do", "BPMNS");
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase database;

        PopulateDbAsync(AppDatabase db) {
            database = db;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            populateWithTestData(database);
            return null;
        }

    }

}
