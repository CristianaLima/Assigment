package com.example.assigmentemiliecristiana.database.dao;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.assigmentemiliecristiana.database.entity.AssignmentEntity;

import java.util.List;

/**
 * https://developer.android.com/topic/libraries/architecture/room.html#no-object-references
 */
@Dao
public abstract class AssignmentDao {
    @Query("SELECT * FROM assignments WHERE id = :id")
    public abstract LiveData<AssignmentEntity> getById(Long id);

    @Query("SELECT * FROM assignments")
    public abstract LiveData<List<AssignmentEntity>> getAll();

    @Query("SELECT * FROM assignments WHERE owner = :owner")
    public abstract LiveData<List<AssignmentEntity>> getOwned(String owner);

    @Query("SELECT * FROM assignments WHERE owner= :owner AND date = :date")
    public abstract LiveData<List<AssignmentEntity>> getAssigByDate(String owner, Long date);

    @Insert
    public abstract long insert(AssignmentEntity assignmentEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(List<AssignmentEntity> assignmentEntities);

    @Update
    public abstract void update(AssignmentEntity assignmentEntity);

    @Delete
    public abstract void delete(AssignmentEntity assignmentEntity);

    @Query("DELETE FROM assignments")
    public abstract void deleteAll();
}
