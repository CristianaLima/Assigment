package com.example.assigmentemiliecristiana.database.async.student;

import android.app.Application;
import android.os.AsyncTask;

import com.example.assigmentemiliecristiana.BaseApp;
import com.example.assigmentemiliecristiana.database.entity.StudentEntity;
import com.example.assigmentemiliecristiana.util.OnAsyncEventListener;

public class CreateStudent extends AsyncTask<StudentEntity, Void, Void> {

    private Application application;
    private OnAsyncEventListener callback;
    private Exception exception;

    public CreateStudent(Application application, OnAsyncEventListener callback) {
        this.application = application;
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(StudentEntity... params) {
        try {
            for (StudentEntity studentEntity : params)
                ((BaseApp) application).getDatabase().studentDao().insert(studentEntity);
        } catch (Exception e) {
            exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (callback != null) {
            if (exception == null) {
                callback.onSuccess();
            } else {
                callback.onFailure(exception);
            }
        }
    }
}
