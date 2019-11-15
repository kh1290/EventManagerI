package com.example.eventmanageri.Models;

public class User {
    private String key;
    private String email;
    private String role;
    private String uid;

    public User() {
    }

    public User(String key, String email, String role, String uid) {
        this.key = key;
        this.email = email;
        this.role = role;
        this.uid = uid;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
