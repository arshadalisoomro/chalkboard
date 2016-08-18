package com.ghofrani.classapp.model;

import android.content.Context;

import com.ghofrani.classapp.modules.DatabaseHelper;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class StandardClass {

    private final String name;
    private final LocalTime startTime;
    private final String startTimeString;
    private final LocalTime endTime;
    private final String endTimeString;
    private final String location;
    private final String teacher;
    private final int color;

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

    public LocalTime getStartTime() {
        return startTime;
    }

    public String getStartTimeString() {
        return startTimeString;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getEndTimeString() {
        return endTimeString;
    }

    public String getLocation() {
        return location;
    }

    public String getTeacher() {
        return teacher;
    }

    public int getColor() {
        return color;
    }

}