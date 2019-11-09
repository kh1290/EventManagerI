package com.example.eventmanageri;

// Define items

public class Event {
    private String titlee;
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

    public Event(String titlee, String type, String date, String photoUrl, String memo, String photo, String video, String location, String share) {
        this.titlee = titlee;
        this.type = type;
        this.date = date;
        this.PhotoUrl = photoUrl;
        this.memo = memo;
        this.photo = photo;
        this.video = video;
        this.location = location;
        this.share = share;
    }

    public String getTitle() {
        return titlee;
    }

    public void setTitle(String title) {
        this.titlee = title;
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
