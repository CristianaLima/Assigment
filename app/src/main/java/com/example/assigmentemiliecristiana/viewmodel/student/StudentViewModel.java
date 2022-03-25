package com.example.assigmentemiliecristiana.viewmodel.student;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.assigmentemiliecristiana.BaseApp;
import com.example.assigmentemiliecristiana.database.AppDatabase;
import com.example.assigmentemiliecristiana.database.entity.StudentEntity;
import com.example.assigmentemiliecristiana.database.repository.StudentRepository;
import com.example.assigmentemiliecristiana.util.OnAsyncEventListener;

public class StudentViewModel extends AndroidViewModel {

    private StudentRepository repository;
    private Application application;
    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<StudentEntity> observableStudent;

    public StudentViewModel(@NonNull Application application,
                            final String studentId, StudentRepository studentRepository) {
        super(application);
        repository = studentRepository;
        this.application = application;

        observableStudent = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableStudent.setValue(null);

        LiveData<StudentEntity> student = repository.getStudent(studentId,application);

        // observe the changes of the student entity from the database and forward them
        observableStudent.addSource(student,observableStudent::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory{
        @NonNull
        private final Application application;
        private final String studentId;
        private final StudentRepository repository;

        public Factory(@NonNull Application application, String studentId) {
            this.application = application;
            this.studentId = studentId;
            repository = ((BaseApp) application).getStudentRepository();
        }


        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new StudentViewModel(application,studentId,repository);
        }
    }

    /**
     * Expose the LiveData StudentEntity query so the UI can observe it.
     */
    public LiveData<StudentEntity> getStudent(){return observableStudent;}

    public void createStudent(StudentEntity student, OnAsyncEventListener callback){
        repository.insert(student,callback,application);
    }

    public void updateStudent(StudentEntity student, OnAsyncEventListener callback){
        repository.update(student,callback,application);
    }

    public void deleteStudent(StudentEntity student, OnAsyncEventListener callback){
        repository.delete(student,callback,application);
    }

}
