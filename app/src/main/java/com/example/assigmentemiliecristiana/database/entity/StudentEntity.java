package com.example.assigmentemiliecristiana.database.entity;


import androidx.annotation.NonNull;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class StudentEntity {
    private String id;
    private String username;
    private String email;
    private String password;

    public StudentEntity() {
    }

    public StudentEntity( String username,@NonNull String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername( String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    @Exclude
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
        return "username='" + username + ", email='" + email;
    }

    public int compareTo(@NonNull Object o) {
        return toString().compareTo(o.toString());
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("username", username);
        result.put("email", email);

        return result;
    }
}
