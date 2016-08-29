package com.ghofrani.classapp.model;

public class SlimClass {

    private final String name;
    private final String location;
    private final String teacher;
    private final int color;

    public SlimClass(String name, String location, String teacher, int color) {

        this.name = name;
        this.location = location;
        this.teacher = teacher;
        this.color = color;

    }

    public String getName() {
        return name;
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