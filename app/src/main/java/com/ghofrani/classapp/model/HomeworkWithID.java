package com.ghofrani.classapp.model;

public class HomeworkWithID {

    private final Homework homework;
    private final int ID;

    public HomeworkWithID(Homework homework, int ID) {

        this.homework = homework;
        this.ID = ID;

    }

    public Homework getHomework() {
        return homework;
    }

    public int getID() {
        return ID;
    }

}