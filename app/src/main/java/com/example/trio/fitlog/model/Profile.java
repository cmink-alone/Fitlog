package com.example.trio.fitlog.model;

public class Profile {
    private int id;
    private String name;
    private int move_minutes;
    private int move_distance;
    private String gender;
    private String birthday;
    private int weight;
    private int height;

    public Profile() {
    }

    public Profile(int id, String name, int move_minutes, int move_distance, String gender, String birthday, int weight, int height) {
        this.id = id;
        this.name = name;
        this.move_minutes = move_minutes;
        this.move_distance = move_distance;
        this.gender = gender;
        this.birthday = birthday;
        this.weight = weight;
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMove_minutes() {
        return move_minutes;
    }

    public void setMove_minutes(int move_minutes) {
        this.move_minutes = move_minutes;
    }

    public int getMove_distance() {
        return move_distance;
    }

    public void setMove_distance(int move_distance) {
        this.move_distance = move_distance;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
