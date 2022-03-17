package com.example.assigmentemiliecristiana.database.async.assignment;

import android.app.Application;
import android.os.AsyncTask;

import com.example.assigmentemiliecristiana.BaseApp;
import com.example.assigmentemiliecristiana.database.entity.AssignmentEntity;
import com.example.assigmentemiliecristiana.util.OnAsyncEventListener;

public class UpdateAssignment extends AsyncTask<AssignmentEntity, Void, Void> {
    private Application application;
    private OnAsyncEventListener callback;
    private Exception exception;

    public UpdateAssignment(Application application, OnAsyncEventListener callback) {
        this.application = application;
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(AssignmentEntity... params) {
        try {
            for (AssignmentEntity assignment: params)
                ((BaseApp) application).getDatabase().assignmentDao().update(assignment);
        } catch (Exception e){
            exception=e;
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
