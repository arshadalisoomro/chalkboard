package com.ghofrani.classapp.modules;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class DataStore {

    private static boolean currentClass;
    private static String[] currentClassInfo;

    private static boolean nextClasses;
    private static boolean tomorrowClasses;

    private static HashMap<String, List<String>> nextClassesHM;
    private static HashMap<String, List<String>> tomorrowClassesHM;

    private static int progressBarProgress = 0;
    private static String progressBarText = "0%";

    private static String[] sundayClasses;
    private static String[] mondayClasses;
    private static String[] tuesdayClasses;
    private static String[] wednesdayClasses;
    private static String[] thursdayClasses;
    private static String[] fridayClasses;
    private static String[] saturdayClasses;

    private static String nextClassString;
    private static String nextClassLocation;

    private static boolean animationState = false;

    public static boolean isAnimationState() {

        return animationState;

    }

    public static void setAnimationState(boolean update) {

        animationState = update;

    }

    public static boolean getCurrentClass() {

        return currentClass;

    }

    public static void setCurrentClass(boolean update) {

        currentClass = update;

    }

    public static String[] getCurrentClassInfo() {

        return currentClassInfo;

    }

    public static void setCurrentClassInfo(String[] update) {

        currentClassInfo = update;

    }

    public static boolean getNextClasses() {

        return nextClasses;

    }

    public static void setNextClasses(boolean update) {

        nextClasses = update;

    }

    public static boolean getTomorrowClasses() {

        return tomorrowClasses;

    }

    public static void setTomorrowClasses(boolean update) {

        tomorrowClasses = update;

    }

    public static HashMap<String, List<String>> getNextClassesHM() {

        return nextClassesHM;

    }

    public static void setNextClassesHM(HashMap<String, List<String>> update) {

        nextClassesHM = update;

    }

    public static HashMap<String, List<String>> getTomorrowClassesHM() {

        return tomorrowClassesHM;

    }

    public static void setTomorrowClassesHM(HashMap<String, List<String>> update) {

        tomorrowClassesHM = update;

    }

    public static int getProgressBarProgress() {

        return progressBarProgress;

    }

    public static void setProgressBarProgress(int update) {

        progressBarProgress = update;

    }

    public static String getProgressBarText() {

        return progressBarText;

    }

    public static void setProgressBarText(String update) {

        progressBarText = update;

    }

    public static String[] getClassesArray(int day) {

        String[] returnArray = null;

        switch (day) {

            case Calendar.SUNDAY:
                returnArray = sundayClasses;
                break;
            case Calendar.MONDAY:
                returnArray = mondayClasses;
                break;
            case Calendar.TUESDAY:
                returnArray = tuesdayClasses;
                break;
            case Calendar.WEDNESDAY:
                returnArray = wednesdayClasses;
                break;
            case Calendar.THURSDAY:
                returnArray = thursdayClasses;
                break;
            case Calendar.FRIDAY:
                returnArray = fridayClasses;
                break;
            case Calendar.SATURDAY:
                returnArray = saturdayClasses;
                break;

        }

        return returnArray;

    }

    public static void setClassesArray(String[] update, int day) {

        switch (day) {

            case Calendar.SUNDAY:
                sundayClasses = update;
                break;
            case Calendar.MONDAY:
                mondayClasses = update;
                break;
            case Calendar.TUESDAY:
                tuesdayClasses = update;
                break;
            case Calendar.WEDNESDAY:
                wednesdayClasses = update;
                break;
            case Calendar.THURSDAY:
                thursdayClasses = update;
                break;
            case Calendar.FRIDAY:
                fridayClasses = update;
                break;
            case Calendar.SATURDAY:
                saturdayClasses = update;
                break;

        }

    }

    public static String getNextClassString() {

        return nextClassString;

    }

    public static void setNextClassString(String update) {

        nextClassString = update;

    }

    public static String getNextClassLocation() {

        return nextClassLocation;

    }

    public static void setNextClassLocation(String update) {

        nextClassLocation = update;

    }

}