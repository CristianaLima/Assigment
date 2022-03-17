package com.example.assigmentemiliecristiana.database.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.annotation.NonNull;

import java.util.Objects;

@Entity(tableName = "students", primaryKeys = {"username"})
public class StudentEntity {

    @NonNull
    private String username;

    @ColumnInfo(name = "email")
    private String email;

    private String password;

    @Ignore
    public StudentEntity() {
    }

    public StudentEntity(@NonNull String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof StudentEntity)) return false;
        StudentEntity obj = (StudentEntity) o;
        return obj.getEmail().equals(this.getEmail());
    }

    @Override
    public String toString() {
        return
                "username='" + username + '\'' +
                ", email='" + email + '\'';
    }

    public int compareTo(@NonNull Object o) {
        return toString().compareTo(o.toString());
    }

}
