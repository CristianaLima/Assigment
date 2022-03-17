package com.example.assigmentemiliecristiana.database.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;


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
    private String description;
    private Date date;
    private String status;
    private String owner;

    public AssignmentEntity() {
    }

    public AssignmentEntity(String name, String type, String description, Date date, String status, String owner) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.date = date;
        this.status = status;
        this.owner = owner;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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
