package com.example.eventmanageri.Models;

public class User {
    private String key;
    private String uname;
    private String email;
    private String bio;
    private String role;

    public User() {
    }

    public User(String key, String uname, String email, String bio, String role) {
        this.key = key;
        this.uname = uname;
        this.email = email;
        this.bio = bio;
        this.role = role;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
