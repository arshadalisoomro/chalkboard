package com.ghofrani.classapp.model;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

public class Homework {

    private final String name;
    private final String className;
    private final LocalDateTime localDateTime;
    private final boolean attach;

    public Homework(String name, String className, String localDateTime, int attach) {

        this.name = name;
        this.className = className;
        this.localDateTime = LocalDateTime.parse(localDateTime);
        this.attach = attach == 1;

    }

    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }

    public DateTime getDateTime() {
        return localDateTime.toDateTime();
    }

    public LocalDate getLocalDate() {
        return localDateTime.toLocalDate();
    }

    public boolean isAttach() {
        return attach;
    }

}