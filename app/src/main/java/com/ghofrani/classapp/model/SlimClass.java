package com.ghofrani.classapp.model;

import android.content.Context;

import com.ghofrani.classapp.modules.DatabaseHelper;

public class SlimClass {

    private Context context;
    private String name;
    private String location;
    private String teacher;
    private int color;

    public SlimClass(Context context, String name, String location, String teacher) {

        this.context = context;
        this.name = name;
        this.location = location;
        this.teacher = teacher;

        DatabaseHelper databaseHelper = new DatabaseHelper(context);

        this.color = databaseHelper.getClassColor(name);

        databaseHelper.close();

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

}