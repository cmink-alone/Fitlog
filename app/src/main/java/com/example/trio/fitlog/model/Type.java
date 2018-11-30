package com.example.trio.fitlog.model;

import android.support.annotation.NonNull;

public class Type {
    private int id;
    private String name;
    private int icon;
    private int met;
    private int step;

    public Type() {

    }

    public Type(int id, String name, int icon, int met, int step) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.met = met;
        this.step = step;
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

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getMet() {
        return met;
    }

    public void setMet(int met) {
        this.met = met;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    @NonNull
    @Override
    public String toString() {
        return this.getName();
    }


    @Override
    public boolean equals (Object obj)
    {
        if (this==obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;

        Type type = (Type) obj ;
        return this.id == type.getId();
    }
}
