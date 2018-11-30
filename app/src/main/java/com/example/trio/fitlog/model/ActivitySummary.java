package com.example.trio.fitlog.model;

public class ActivitySummary {
    String date;
    int total_minutes;
    int total_distance;

    public ActivitySummary() {
    }

    public ActivitySummary(int total_minutes, int total_distance) {
        this.total_minutes = total_minutes;
        this.total_distance = total_distance;
    }

    public int getTotal_minutes() {
        return total_minutes;
    }

    public void setTotal_minutes(int total_minutes) {
        this.total_minutes = total_minutes;
    }

    public int getTotal_distance() {
        return total_distance;
    }

    public void setTotal_distance(int total_distance) {
        this.total_distance = total_distance;
    }
}
