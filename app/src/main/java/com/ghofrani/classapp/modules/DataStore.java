package com.ghofrani.classapp.modules;

import com.ghofrani.classapp.model.SlimClass;
import com.ghofrani.classapp.model.StandardClass;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class DataStore {

    private static boolean isCurrentClass;
    private static StandardClass currentClass;

    private static boolean isNextClasses;
    private static StandardClass nextClass;
    private static LinkedList<StandardClass> nextClassesLinkedList;

    private static boolean isTomorrowClasses;
    private static LinkedList<StandardClass> tomorrowClassesLinkedList;

    private static LinkedList<StandardClass> sundayClasses;
    private static LinkedList<StandardClass> mondayClasses;
    private static LinkedList<StandardClass> tuesdayClasses;
    private static LinkedList<StandardClass> wednesdayClasses;
    private static LinkedList<StandardClass> thursdayClasses;
    private static LinkedList<StandardClass> fridayClasses;
    private static LinkedList<StandardClass> saturdayClasses;

    private static LinkedList<SlimClass> allClassesLinkedList;
    private static List<String> allClassNamesList;

    private static int progressBarProgress = 0;
    private static String progressBarText = "0%";

    private static boolean isAnimated = false;
    private static int selectedTabPosition = 0;

    public static boolean isCurrentClass() {
        return isCurrentClass;
    }

    public static void setIsCurrentClass(boolean update) {
        isCurrentClass = update;
    }

    public static StandardClass getCurrentClass() {
        return currentClass;
    }

    public static void setCurrentClass(StandardClass update) {
        currentClass = update;
    }

    public static boolean isNextClasses() {
        return isNextClasses;
    }

    public static void setIsNextClasses(boolean update) {
        isNextClasses = update;
    }

    public static StandardClass getNextClass() {
        return nextClass;
    }

    public static void setNextClass(StandardClass update) {
        nextClass = update;
    }

    public static LinkedList<StandardClass> getNextClassesLinkedList() {
        return nextClassesLinkedList;
    }

    public static void setNextClassesLinkedList(LinkedList<StandardClass> update) {
        nextClassesLinkedList = update;
    }

    public static boolean isTomorrowClasses() {
        return isTomorrowClasses;
    }

    public static void setIsTomorrowClasses(boolean update) {
        isTomorrowClasses = update;
    }

    public static LinkedList<StandardClass> getTomorrowClassesLinkedList() {
        return tomorrowClassesLinkedList;
    }

    public static void setTomorrowClassesLinkedList(LinkedList<StandardClass> update) {
        tomorrowClassesLinkedList = update;
    }

    public static LinkedList<StandardClass> getClassesLinkedListOfDay(int day) {

        LinkedList<StandardClass> returnLinkedList;

        switch (day) {

            case Calendar.SUNDAY:
                returnLinkedList = sundayClasses;
                break;
            case Calendar.MONDAY:
                returnLinkedList = mondayClasses;
                break;
            case Calendar.TUESDAY:
                returnLinkedList = tuesdayClasses;
                break;
            case Calendar.WEDNESDAY:
                returnLinkedList = wednesdayClasses;
                break;
            case Calendar.THURSDAY:
                returnLinkedList = thursdayClasses;
                break;
            case Calendar.FRIDAY:
                returnLinkedList = fridayClasses;
                break;
            case Calendar.SATURDAY:
                returnLinkedList = saturdayClasses;
                break;
            default:
                returnLinkedList = null;
                break;

        }

        return returnLinkedList;

    }

    public static void setClassesLinkedListOfDay(int day, LinkedList<StandardClass> update) {

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

    public static LinkedList<SlimClass> getAllClassesLinkedList() {
        return allClassesLinkedList;
    }

    public static void setAllClassesLinkedList(LinkedList<SlimClass> update) {
        allClassesLinkedList = update;
    }

    public static List<String> getAllClassNamesList() {
        return allClassNamesList;
    }

    public static void setAllClassNamesList(List<String> update) {
        allClassNamesList = update;
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

    public static boolean isAnimated() {
        return isAnimated;
    }

    public static void setIsAnimated(boolean update) {
        isAnimated = update;
    }

    public static int getSelectedTabPosition() {
        return selectedTabPosition;
    }

    public static void setSelectedTabPosition(int update) {
        selectedTabPosition = update;
    }

}