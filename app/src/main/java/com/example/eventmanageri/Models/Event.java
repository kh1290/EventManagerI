package com.example.eventmanageri.Models;

// Define items

public class Event {
    private String eventId;
    private String userId;
    private String title;
    private String type;
    private String date;
    private String PhotoUrl;
    private String memo;
    private String photo;
    private String video;
    private String location;
    private String share;

    public Event() {
    }

    public Event(String eventId, String userId, String title, String type, String date, String photoUrl, String memo, String photo, String video, String location, String share) {
        this.eventId = eventId;
        this.userId = userId;
        this.title = title;
        this.type = type;
        this.date = date;
        this.PhotoUrl = photoUrl;
        this.memo = memo;
        this.photo = photo;
        this.video = video;
        this.location = location;
        this.share = share;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.PhotoUrl = PhotoUrl;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }
}
