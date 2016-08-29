package com.ghofrani.classapp.model;

import org.joda.time.DateTime;

public class Homework {

    private final String name;
    private final String className;
    private final DateTime dateTime;
    private final boolean attach;
    private final int color;

    public Homework(String name, String className, DateTime dateTime, int attach, int color) {

        this.name = name;
        this.className = className;
        this.dateTime = dateTime;
        this.attach = attach == 1;
        this.color = color;

    }

    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public boolean isAttach() {
        return attach;
    }

    public int getColor() {
        return color;
    }

}