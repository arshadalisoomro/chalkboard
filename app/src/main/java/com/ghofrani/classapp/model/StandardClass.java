package com.ghofrani.classapp.model;

import android.content.Context;
import android.preference.PreferenceManager;

import com.ghofrani.classapp.modules.DatabaseHelper;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class StandardClass {

    private final String name;
    private final LocalTime startTime;
    private final String startTimeString;
    private final String startTimeStringAMPM;
    private final LocalTime endTime;
    private final String endTimeString;
    private final String endTimeStringAMPM;
    private final String location;
    private final String teacher;
    private final boolean is24Hour;
    private final int color;

    public StandardClass(Context context, String name, String startTime, String endTime) {

        this.name = name;

        DateTimeFormatter formatter = DateTimeFormat.forPattern("HHmm");
        DateTimeFormatter formatterAMPM = DateTimeFormat.forPattern("h:mm a");

        this.startTime = formatter.parseLocalTime(startTime);
        this.endTime = formatter.parseLocalTime(endTime);

        this.startTimeString = this.startTime.toString().substring(0, 5);
        this.endTimeString = this.endTime.toString().substring(0, 5);

        this.startTimeStringAMPM = formatterAMPM.print(this.startTime);
        this.endTimeStringAMPM = formatterAMPM.print(this.endTime);

        this.is24Hour = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("24_hour_time", true);

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

    public String getStartTimeString(boolean respectSettings) {

        if (respectSettings) {

            if (is24Hour)
                return startTimeString;
            else
                return startTimeStringAMPM;

        }

        return startTimeString;

    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getEndTimeString(boolean respectSettings) {

        if (respectSettings) {

            if (is24Hour)
                return endTimeString;
            else
                return endTimeStringAMPM;

        }

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