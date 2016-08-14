package com.ghofrani.classapp.model;

import android.content.Context;

import com.ghofrani.classapp.modules.DatabaseHelper;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class StandardClass {

    private String name;
    private LocalTime startTime;
    private String startTimeString;
    private LocalTime endTime;
    private String endTimeString;
    private String location;
    private String teacher;
    private int color;

    public StandardClass(Context context, String name, String startTime, String endTime) {

        this.name = name;

        DateTimeFormatter formatter = DateTimeFormat.forPattern("HHmm");

        this.startTime = formatter.parseLocalTime(startTime);
        this.startTimeString = this.startTime.toString().substring(0, 5);
        this.endTime = formatter.parseLocalTime(endTime);
        this.endTimeString = this.endTime.toString().substring(0, 5);

        DatabaseHelper databaseHelper = new DatabaseHelper(context);

        this.location = databaseHelper.getClassLocation(name);
        this.teacher = databaseHelper.getClassTeacher(name);
        this.color = databaseHelper.getClassColor(name);

        databaseHelper.close();

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public String getStartTimeString() {
        return startTimeString;
    }

    public void setStartTimeString(String startTimeString) {
        this.startTimeString = startTimeString;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getEndTimeString() {
        return endTimeString;
    }

    public void setEndTimeString(String endTimeString) {
        this.endTimeString = endTimeString;
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