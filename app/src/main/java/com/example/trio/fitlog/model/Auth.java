package com.example.trio.fitlog.model;

public class Auth {
    private int status;
    private String message;
    private Profile user;

    public Profile getUser() {
        return user;
    }

    public void setUser(Profile user) {
        this.user = user;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
