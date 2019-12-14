package com.example.eventmanageri.Models;

public class Rating {

    private String value, uid, uname;
    //private Object timestamp;


    public Rating() {
    }

    public Rating(String value, String uid, String uname) {
        this.value = value;
        this.uid = uid;
        this.uname = uname;
        //this.timestamp = ServerValue.TIMESTAMP;
    }


    public String getValue() {return value;}

    public void setValue(String content) {this.value = content;}

    public String getUid() {return uid;}

    public void setUid(String uid) {this.uid = uid;}

    public String getUname() {return uname;}

    public void setUname(String uname) {this.uname = uname;}


}
