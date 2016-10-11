package com.ghofrani.classapp.module;

import android.util.SparseArray;

import com.ghofrani.classapp.model.Homework;
import com.ghofrani.classapp.model.SlimClass;
import com.ghofrani.classapp.model.StandardClass;

import org.joda.time.DateTime;

import java.util.ArrayList;

public class DataSingleton {

    private static DataSingleton dataSingletonInstance = null;

    private StandardClass currentClass;

    private StandardClass nextClass;
    private ArrayList<StandardClass> nextClassesArrayList;

    private ArrayList<StandardClass> tomorrowClassesArrayList;

    private String minutesLeftText;
    private int progressBarProgress;
    private String progressBarText;

    private ArrayList<StandardClass> sundayClasses;
    private ArrayList<StandardClass> mondayClasses;
    private ArrayList<StandardClass> tuesdayClasses;
    private ArrayList<StandardClass> wednesdayClasses;
    private ArrayList<StandardClass> thursdayClasses;
    private ArrayList<StandardClass> fridayClasses;
    private ArrayList<StandardClass> saturdayClasses;

    private ArrayList<SlimClass> allClassesArrayList;
    private ArrayList<String> allClassNamesArrayList;

    private ArrayList<Homework> todayHomeworkArrayList;
    private ArrayList<Homework> tomorrowHomeworkArrayList;
    private ArrayList<Homework> thisWeekHomeworkArrayList;
    private ArrayList<Homework> nextWeekHomeworkArrayList;
    private ArrayList<Homework> thisMonthHomeworkArrayList;
    private ArrayList<Homework> beyondThisMonthHomeworkArrayList;
    private ArrayList<Homework> pastHomeworkArrayList;

    private ArrayList<Object> dataArrayList;
    private SparseArray<Object> dataSparseArrayLastRemoved;

    private DateTime thisWeekEnd;
    private DateTime nextWeekEnd;

    private boolean isAnimated;
    private int selectedTabPosition;

    private boolean isExpandableListViewCollapsed;

    private boolean is24Hour;

    private boolean recreate;
    private boolean isChangedFirstDay;

    private boolean reactToBroadcast;

    private DataSingleton() {

        nextClassesArrayList = new ArrayList<>();

        tomorrowClassesArrayList = new ArrayList<>();

        progressBarProgress = 0;
        progressBarText = "0%";

        sundayClasses = new ArrayList<>();
        mondayClasses = new ArrayList<>();
        tuesdayClasses = new ArrayList<>();
        wednesdayClasses = new ArrayList<>();
        thursdayClasses = new ArrayList<>();
        fridayClasses = new ArrayList<>();
        saturdayClasses = new ArrayList<>();

        allClassesArrayList = new ArrayList<>();
        allClassNamesArrayList = new ArrayList<>();

        todayHomeworkArrayList = new ArrayList<>();
        tomorrowHomeworkArrayList = new ArrayList<>();
        thisWeekHomeworkArrayList = new ArrayList<>();
        nextWeekHomeworkArrayList = new ArrayList<>();
        thisMonthHomeworkArrayList = new ArrayList<>();
        beyondThisMonthHomeworkArrayList = new ArrayList<>();
        pastHomeworkArrayList = new ArrayList<>();

        dataArrayList = new ArrayList<>();

        isAnimated = false;
        selectedTabPosition = 0;

        isExpandableListViewCollapsed = false;

        is24Hour = true;

        recreate = false;
        isChangedFirstDay = false;

        reactToBroadcast = true;

    }

    public static void initInstance() {

        if (dataSingletonInstance == null)
            dataSingletonInstance = new DataSingleton();

    }

    public static DataSingleton getInstance() {

        return dataSingletonInstance;

    }

    public StandardClass getCurrentClass() {
        return currentClass;
    }

    public void setCurrentClass(StandardClass currentClass) {
        this.currentClass = currentClass;
    }

    public StandardClass getNextClass() {
        return nextClass;
    }

    public void setNextClass(StandardClass nextClass) {
        this.nextClass = nextClass;
    }

    public ArrayList<StandardClass> getNextClassesArrayList() {
        return nextClassesArrayList;
    }

    public void setNextClassesArrayList(ArrayList<StandardClass> nextClassesArrayList) {
        this.nextClassesArrayList = nextClassesArrayList;
    }

    public ArrayList<StandardClass> getTomorrowClassesArrayList() {
        return tomorrowClassesArrayList;
    }

    public void setTomorrowClassesArrayList(ArrayList<StandardClass> tomorrowClassesArrayList) {
        this.tomorrowClassesArrayList = tomorrowClassesArrayList;
    }

    public String getMinutesLeftText() {
        return minutesLeftText;
    }

    public void setMinutesLeftText(String minutesLeftText) {
        this.minutesLeftText = minutesLeftText;
    }

    public int getProgressBarProgress() {
        return progressBarProgress;
    }

    public void setProgressBarProgress(int progressBarProgress) {
        this.progressBarProgress = progressBarProgress;
    }

    public String getProgressBarText() {
        return progressBarText;
    }

    public void setProgressBarText(String progressBarText) {
        this.progressBarText = progressBarText;
    }

    public ArrayList<StandardClass> getSundayClasses() {
        return sundayClasses;
    }

    public void setSundayClasses(ArrayList<StandardClass> sundayClasses) {
        this.sundayClasses = sundayClasses;
    }

    public ArrayList<StandardClass> getMondayClasses() {
        return mondayClasses;
    }

    public void setMondayClasses(ArrayList<StandardClass> mondayClasses) {
        this.mondayClasses = mondayClasses;
    }

    public ArrayList<StandardClass> getTuesdayClasses() {
        return tuesdayClasses;
    }

    public void setTuesdayClasses(ArrayList<StandardClass> tuesdayClasses) {
        this.tuesdayClasses = tuesdayClasses;
    }

    public ArrayList<StandardClass> getWednesdayClasses() {
        return wednesdayClasses;
    }

    public void setWednesdayClasses(ArrayList<StandardClass> wednesdayClasses) {
        this.wednesdayClasses = wednesdayClasses;
    }

    public ArrayList<StandardClass> getThursdayClasses() {
        return thursdayClasses;
    }

    public void setThursdayClasses(ArrayList<StandardClass> thursdayClasses) {
        this.thursdayClasses = thursdayClasses;
    }

    public ArrayList<StandardClass> getFridayClasses() {
        return fridayClasses;
    }

    public void setFridayClasses(ArrayList<StandardClass> fridayClasses) {
        this.fridayClasses = fridayClasses;
    }

    public ArrayList<StandardClass> getSaturdayClasses() {
        return saturdayClasses;
    }

    public void setSaturdayClasses(ArrayList<StandardClass> saturdayClasses) {
        this.saturdayClasses = saturdayClasses;
    }

    public ArrayList<SlimClass> getAllClassesArrayList() {
        return allClassesArrayList;
    }

    public void setAllClassesArrayList(ArrayList<SlimClass> allClassesArrayList) {
        this.allClassesArrayList = allClassesArrayList;
    }

    public ArrayList<String> getAllClassNamesArrayList() {
        return allClassNamesArrayList;
    }

    public void setAllClassNamesArrayList(ArrayList<String> allClassNamesArrayList) {
        this.allClassNamesArrayList = allClassNamesArrayList;
    }

    public ArrayList<Homework> getTodayHomeworkArrayList() {
        return todayHomeworkArrayList;
    }

    public void setTodayHomeworkArrayList(ArrayList<Homework> todayHomeworkArrayList) {
        this.todayHomeworkArrayList = todayHomeworkArrayList;
    }

    public ArrayList<Homework> getTomorrowHomeworkArrayList() {
        return tomorrowHomeworkArrayList;
    }

    public void setTomorrowHomeworkArrayList(ArrayList<Homework> tomorrowHomeworkArrayList) {
        this.tomorrowHomeworkArrayList = tomorrowHomeworkArrayList;
    }

    public ArrayList<Homework> getThisWeekHomeworkArrayList() {
        return thisWeekHomeworkArrayList;
    }

    public void setThisWeekHomeworkArrayList(ArrayList<Homework> thisWeekHomeworkArrayList) {
        this.thisWeekHomeworkArrayList = thisWeekHomeworkArrayList;
    }

    public ArrayList<Homework> getNextWeekHomeworkArrayList() {
        return nextWeekHomeworkArrayList;
    }

    public void setNextWeekHomeworkArrayList(ArrayList<Homework> nextWeekHomeworkArrayList) {
        this.nextWeekHomeworkArrayList = nextWeekHomeworkArrayList;
    }

    public ArrayList<Homework> getThisMonthHomeworkArrayList() {
        return thisMonthHomeworkArrayList;
    }

    public void setThisMonthHomeworkArrayList(ArrayList<Homework> thisMonthHomeworkArrayList) {
        this.thisMonthHomeworkArrayList = thisMonthHomeworkArrayList;
    }

    public ArrayList<Homework> getBeyondThisMonthHomeworkArrayList() {
        return beyondThisMonthHomeworkArrayList;
    }

    public void setBeyondThisMonthHomeworkArrayList(ArrayList<Homework> beyondThisMonthHomeworkArrayList) {
        this.beyondThisMonthHomeworkArrayList = beyondThisMonthHomeworkArrayList;
    }

    public ArrayList<Homework> getPastHomeworkArrayList() {
        return pastHomeworkArrayList;
    }

    public void setPastHomeworkArrayList(ArrayList<Homework> pastHomeworkArrayList) {
        this.pastHomeworkArrayList = pastHomeworkArrayList;
    }

    public ArrayList<Object> getDataArrayList() {
        return dataArrayList;
    }

    public void setDataArrayList(ArrayList<Object> dataArrayList) {
        this.dataArrayList = dataArrayList;
    }

    public SparseArray<Object> getDataSparseArrayLastRemoved() {
        return dataSparseArrayLastRemoved;
    }

    public void setDataSparseArrayLastRemoved(SparseArray<Object> dataSparseArrayLastRemoved) {
        this.dataSparseArrayLastRemoved = dataSparseArrayLastRemoved;
    }

    public DateTime getThisWeekEnd() {
        return thisWeekEnd;
    }

    public void setThisWeekEnd(DateTime thisWeekEnd) {
        this.thisWeekEnd = thisWeekEnd;
    }

    public DateTime getNextWeekEnd() {
        return nextWeekEnd;
    }

    public void setNextWeekEnd(DateTime nextWeekEnd) {
        this.nextWeekEnd = nextWeekEnd;
    }

    public boolean isAnimated() {
        return isAnimated;
    }

    public void setAnimated(boolean animated) {
        isAnimated = animated;
    }

    public int getSelectedTabPosition() {
        return selectedTabPosition;
    }

    public void setSelectedTabPosition(int selectedTabPosition) {
        this.selectedTabPosition = selectedTabPosition;
    }

    public boolean isExpandableListViewCollapsed() {
        return isExpandableListViewCollapsed;
    }

    public void setIsExpandableListViewCollapsed(boolean isExpandableListViewCollapsed) {
        this.isExpandableListViewCollapsed = isExpandableListViewCollapsed;
    }

    public boolean is24Hour() {
        return is24Hour;
    }

    public void setIs24Hour(boolean is24Hour) {
        this.is24Hour = is24Hour;
    }

    public boolean isRecreate() {
        return recreate;
    }

    public void setRecreate(boolean recreate) {
        this.recreate = recreate;
    }

    public boolean isChangedFirstDay() {
        return isChangedFirstDay;
    }

    public void setChangedFirstDay(boolean changedFirstDay) {
        isChangedFirstDay = changedFirstDay;
    }

    public boolean isReactToBroadcast() {
        return reactToBroadcast;
    }

    public void setReactToBroadcast(boolean reactToBroadcast) {
        this.reactToBroadcast = reactToBroadcast;
    }

}