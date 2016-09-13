package com.ghofrani.classapp.event;

public class Update {

    private final boolean data;
    private final boolean homework;
    private final boolean timetable;
    private final boolean classes;

    public Update(boolean data, boolean homework, boolean timetable, boolean classes) {

        this.data = data;
        this.homework = homework;
        this.timetable = timetable;
        this.classes = classes;

    }

    public boolean isData() {
        return data;
    }

    public boolean isHomework() {
        return homework;
    }

    public boolean isTimetable() {
        return timetable;
    }

    public boolean isClasses() {
        return classes;
    }

}