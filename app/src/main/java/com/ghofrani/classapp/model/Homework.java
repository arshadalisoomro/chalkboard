package com.ghofrani.classapp.model;

import org.joda.time.DateTime;

public class Homework {

    private final String name;
    private final String className;
    private final DateTime dateTime;
    private final boolean attach;
    private final int color;
    private final boolean highPriority;

    public Homework(String name, String className, DateTime dateTime, boolean attach, int color, boolean highPriority) {

        this.name = name;
        this.className = className;
        this.dateTime = dateTime;
        this.attach = attach;
        this.color = color;
        this.highPriority = highPriority;

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

    public boolean isHighPriority() {
        return highPriority;
    }

}