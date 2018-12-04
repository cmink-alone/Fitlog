package com.example.trio.fitlog.model;

import java.io.Serializable;

import static java.lang.Math.round;

public class Activity implements Serializable {
    private int id;
    private int server_id;
    private int user_id;
    private int type_id;
    private String title;
    private int distance;
    private int hour;
    private int minute;
    private String pace;
    private String datetime;
    private String description;
    private int met;
    private int step;
    private int flag_insert;
    private int flag_update;
    private int flag_delete;

    public int getCaloriesBurned(Profile profile){
        return (int)(getTotalMinutes()*(met*3.5*profile.getWeight())/200);
    }

    public int getHeartPoint(){
        double x = 0.5;
        if(met>3 && met <6){
            x = 1;
        } else if (met >= 6){
            x = 2;
        }
        return (int) (x * getTotalMinutes());
    }

    public double getSpeed(){
        double totalMinutes = (double) getTotalMinutes()/60;
        return distance/totalMinutes;
    }

    public int getTotalMinutes(){
        return hour * 60 + minute;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getMet() {
        return met;
    }

    public void setMet(int met) {
        this.met = met;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getPace() {
        return pace;
    }

    public void setPace(String pace) {
        this.pace = pace;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getServer_id() {
        return server_id;
    }

    public void setServer_id(int server_id) {
        this.server_id = server_id;
    }

    public int getFlag_insert() {
        return flag_insert;
    }

    public void setFlag_insert(int flag_insert) {
        this.flag_insert = flag_insert;
    }

    public int getFlag_update() {
        return flag_update;
    }

    public void setFlag_update(int flag_update) {
        this.flag_update = flag_update;
    }

    public int getFlag_delete() {
        return flag_delete;
    }

    public void setFlag_delete(int flag_delete) {
        this.flag_delete = flag_delete;
    }
}
