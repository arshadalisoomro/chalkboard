package com.ghofrani.classapp.modules;

import com.ghofrani.classapp.model.SlimClass;
import com.ghofrani.classapp.model.StandardClass;

import java.util.ArrayList;

public class DataStore {

    public static boolean isCurrentClass;
    public static StandardClass currentClass;

    public static boolean isNextClasses;
    public static StandardClass nextClass;
    public static ArrayList<StandardClass> nextClassesArrayList;

    public static boolean isTomorrowClasses;
    public static ArrayList<StandardClass> tomorrowClassesArrayList;

    public static ArrayList<StandardClass> sundayClasses;
    public static ArrayList<StandardClass> mondayClasses;
    public static ArrayList<StandardClass> tuesdayClasses;
    public static ArrayList<StandardClass> wednesdayClasses;
    public static ArrayList<StandardClass> thursdayClasses;
    public static ArrayList<StandardClass> fridayClasses;
    public static ArrayList<StandardClass> saturdayClasses;

    public static ArrayList<SlimClass> allClassesArrayList;
    public static ArrayList<String> allClassNamesArrayList;

    public static int progressBarProgress = 0;
    public static String progressBarText = "0%";

    public static boolean isAnimated = false;
    public static int selectedTabPosition = 0;

    public static boolean recreate = false;

}