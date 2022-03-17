package com.example.assigmentemiliecristiana.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import android.database.sqlite.SQLiteConstraintException;

import com.example.assigmentemiliecristiana.database.entity.StudentEntity;

import java.util.List;

@Dao
public interface StudentDao {

    @Query("SELECT * FROM STUDENTS WHERE username = :id")
    LiveData<StudentEntity> getById(String id);

    @Query("SELECT * FROM students")
    LiveData<List<StudentEntity>> getAll();

    @Insert
    long insert(StudentEntity studentEntity) throws SQLiteConstraintException;

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    long insertAll(List<StudentEntity> studentEntities);

    @Update
    void update(StudentEntity studentEntity);

    @Delete
    void delete(StudentEntity studentEntity);

    @Query("DELETE FROM students")
    void deleteAll();
}
