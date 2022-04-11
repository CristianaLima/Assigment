package com.example.assigmentemiliecristiana.database.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;


import java.io.Serializable;


public class AssignmentEntity implements Serializable {
    private String id;
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

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    @Exclude
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

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("type", type);
        result.put("status", status);
        result.put("date", date);
        result.put("description", description);
        result.put("course", course);

        return result;
    }
}
