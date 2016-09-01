package com.ghofrani.classapp.model;

import org.joda.time.LocalTime;

public class TimedSlimClass {

    private final String name;
    private final LocalTime startTime;
    private final LocalTime endTime;

    public TimedSlimClass(String name, LocalTime startTime, LocalTime endTime) {

        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;

    }

    public String getName() {
        return name;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

}