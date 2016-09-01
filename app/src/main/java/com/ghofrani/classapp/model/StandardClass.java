package com.ghofrani.classapp.model;

import org.joda.time.LocalTime;
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

    public StandardClass(String name, LocalTime startTime, LocalTime endTime, boolean is24Hour, DateTimeFormatter formatter, String location, String teacher, int color) {

        this.name = name;

        this.startTime = startTime;
        this.endTime = endTime;

        this.startTimeString = this.startTime.toString().substring(0, 5);
        this.endTimeString = this.endTime.toString().substring(0, 5);

        this.startTimeStringAMPM = formatter.print(startTime);
        this.endTimeStringAMPM = formatter.print(endTime);

        this.is24Hour = is24Hour;
        this.location = location;
        this.teacher = teacher;
        this.color = color;

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