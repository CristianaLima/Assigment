package com.example.assigmentemiliecristiana.database.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * https://developer.android.com/reference/android/arch/persistence/room/Entity.html
 *
 * interesting: owner column references a foreign key, that's why this column is indexed.
 * If not indexed, it might trigger full table scans whenever parent table is modified so you are
 * highly advised to create an index that covers this column.
 *
 * Further information to Parcelable:
 * https://developer.android.com/reference/android/os/Parcelable
 * Why we use Parcelable over Serializable:
 * https://android.jlelse.eu/parcelable-vs-serializable-6a2556d51538
 */
@Entity(tableName = "assignments",
        foreignKeys =
        @ForeignKey(
                entity = StudentEntity.class,
                parentColumns = "username",
                childColumns = "owner",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {
                @Index(
                        value = {"owner"}
                )}
)
public class AssignmentEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String name;
    private String type;
    private String course;
    private String description;
    private Long date;
    private String status;
    private String owner;

    public AssignmentEntity() {
    }

    public AssignmentEntity(String name, String type, String description, Long date, String status, String owner, String course) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.date = date;
        this.status = status;
        this.owner = owner;
        this.course = course;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof AssignmentEntity)) return false;
        AssignmentEntity o = (AssignmentEntity) obj;
        return o.getId().equals(this.getId());
    }

    @Override
    public String toString() {
        return name;
    }


}
