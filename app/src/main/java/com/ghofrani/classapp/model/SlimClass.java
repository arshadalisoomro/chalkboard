package com.ghofrani.classapp.model;

import android.content.Context;

import com.ghofrani.classapp.modules.DatabaseHelper;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class SlimClass {

    private String name;
    private String location;
    private String teacher;

    public SlimClass (String name, String location, String teacher) {

        this.name = name;
        this.location = location;
        this.teacher = teacher;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

}