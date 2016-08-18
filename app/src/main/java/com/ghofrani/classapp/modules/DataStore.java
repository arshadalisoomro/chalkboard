package com.ghofrani.classapp.modules;

import com.ghofrani.classapp.model.SlimClass;
import com.ghofrani.classapp.model.StandardClass;

import java.util.LinkedList;
import java.util.List;

public class DataStore {

    public static boolean isCurrentClass;
    public static StandardClass currentClass;

    public static boolean isNextClasses;
    public static StandardClass nextClass;
    public static LinkedList<StandardClass> nextClassesLinkedList;

    public static boolean isTomorrowClasses;
    public static LinkedList<StandardClass> tomorrowClassesLinkedList;

    public static LinkedList<StandardClass> sundayClasses;
    public static LinkedList<StandardClass> mondayClasses;
    public static LinkedList<StandardClass> tuesdayClasses;
    public static LinkedList<StandardClass> wednesdayClasses;
    public static LinkedList<StandardClass> thursdayClasses;
    public static LinkedList<StandardClass> fridayClasses;
    public static LinkedList<StandardClass> saturdayClasses;

    public static LinkedList<SlimClass> allClassesLinkedList;
    public static List<String> allClassNamesList;

    public static int progressBarProgress = 0;
    public static String progressBarText = "0%";

    public static boolean isAnimated = false;
    public static int selectedTabPosition = 0;

}