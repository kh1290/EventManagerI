package com.example.eventmanageri.Models;

public class Follow {
    private String key;
    private String following;

    public Follow() {
    }

    public Follow(String key, String following) {
        this.key = key;
        this.following = following;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }
}
