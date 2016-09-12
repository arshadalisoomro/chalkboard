package com.ghofrani.classapp.event;

public class Update {

    private boolean data;
    private boolean homework;
    private boolean timetable;
    private boolean classes;

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