package com.ghofrani.classapp.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.RemoteViews;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.activity.AddHomework;
import com.ghofrani.classapp.activity.Main;
import com.ghofrani.classapp.model.SlimClass;
import com.ghofrani.classapp.model.StandardClass;
import com.ghofrani.classapp.modules.DataStore;
import com.ghofrani.classapp.modules.DatabaseHelper;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalTime;
import org.joda.time.Minutes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Background extends Service {

    private static IntentFilter backgroundIntentFilter;
    private static BroadcastReceiver backgroundBroadcastReceiver;

    static {

        backgroundIntentFilter = new IntentFilter();
        backgroundIntentFilter.addAction(Intent.ACTION_TIME_TICK);
        backgroundIntentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        backgroundIntentFilter.addAction(Intent.ACTION_TIME_CHANGED);
        backgroundIntentFilter.addAction(Intent.ACTION_DATE_CHANGED);

    }

    private Handler notificationHandler;
    private Runnable notificationRunnable;
    private NotificationCompat.Builder notificationCompatBuilder;
    private NotificationManager notificationManager;
    private RemoteViews remoteViews;

    private StandardClass currentClass;

    private boolean currentToNextTransition = false;
    private boolean nextToCurrentTransition = false;
    private boolean simpleToDetailedTransition = false;
    private boolean detailedToSimpleTransition = false;

    private BroadcastReceiver updateData = new BroadcastReceiver() {

        @Override

        public void onReceive(Context context, Intent intent) {

            getData();
            getTimetable();

        }

    };

    private BroadcastReceiver updateClasses = new BroadcastReceiver() {

        @Override

        public void onReceive(Context context, Intent intent) {

            getClasses();

        }

    };

    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;

    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;

    }

    @Override
    public void onCreate() {

        super.onCreate();

        JodaTimeAndroid.init(this);

        registerBroadcastReceiver();

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(updateData, new IntentFilter("update_data"));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(updateClasses, new IntentFilter("update_classes"));

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        getData();

        getTimetable();

        getClasses();

    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(updateData);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(updateClasses);

        if (backgroundBroadcastReceiver != null) {

            getApplicationContext().unregisterReceiver(backgroundBroadcastReceiver);

        }

        if (notificationHandler != null) {

            notificationHandler.removeCallbacksAndMessages(null);
            notificationHandler = null;

        }

        notificationManager.cancelAll();

        updateData = null;
        updateClasses = null;
        backgroundIntentFilter = null;
        backgroundBroadcastReceiver = null;
        notificationRunnable = null;
        notificationCompatBuilder = null;
        notificationManager = null;
        remoteViews = null;
        currentClass = null;

    }

    private void registerBroadcastReceiver() {

        backgroundBroadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                switch (intent.getAction()) {

                    case Intent.ACTION_TIME_TICK:

                        getData();

                        if (notificationHandler != null) {

                            notificationHandler.removeCallbacksAndMessages(null);
                            notificationHandler.post(notificationRunnable);

                        }

                        break;

                    case Intent.ACTION_TIMEZONE_CHANGED:

                        getData();

                        if (notificationHandler != null) {

                            notificationHandler.removeCallbacksAndMessages(null);
                            notificationHandler.post(notificationRunnable);

                        }

                        getTimetable();

                        break;

                    case Intent.ACTION_TIME_CHANGED:

                        getData();

                        if (notificationHandler != null) {

                            notificationHandler.removeCallbacksAndMessages(null);
                            notificationHandler.post(notificationRunnable);

                        }

                        getTimetable();

                        break;

                    case Intent.ACTION_DATE_CHANGED:

                        getData();

                        if (notificationHandler != null) {

                            notificationHandler.removeCallbacksAndMessages(null);
                            notificationHandler.post(notificationRunnable);

                        }

                        getTimetable();

                        break;

                    default:

                        break;

                }

            }

        };

        getApplicationContext().registerReceiver(backgroundBroadcastReceiver, backgroundIntentFilter);

    }

    private void getData() {

        boolean isCurrentClass = false;
        boolean isNextClasses = false;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        Cursor todayCursor = databaseHelper.getClasses(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));

        LinkedList<StandardClass> nextClassesLinkedList = new LinkedList<>();
        boolean nextClassDefined = false;

        LocalTime currentTime = new LocalTime().now();

        while (todayCursor.moveToNext()) {

            StandardClass standardClass = new StandardClass(getApplicationContext(), todayCursor.getString(1), todayCursor.getString(2), todayCursor.getString(3));

            if (standardClass.getStartTime().isAfter(currentTime)) {

                nextClassesLinkedList.add(standardClass);
                isNextClasses = true;
                DataStore.setIsNextClasses(true);

                if (!nextClassDefined) {

                    DataStore.setNextClass(standardClass);
                    nextClassDefined = true;

                }

            } else if (standardClass.getEndTime().isAfter(currentTime) && standardClass.getStartTime().isBefore(currentTime)) {

                isCurrentClass = true;
                currentClass = standardClass;
                DataStore.setCurrentClass(standardClass);

            }

        }

        DataStore.setIsNextClasses(isNextClasses);

        if (DataStore.isNextClasses())
            DataStore.setNextClassesLinkedList(nextClassesLinkedList);

        DataStore.setIsCurrentClass(isCurrentClass);

        if (DataStore.isCurrentClass()) {

            if (sharedPreferences.getBoolean("detailed_notification", false)) {

                currentToNextTransition = true;

                if (nextToCurrentTransition) {

                    notificationManager.cancelAll();

                    if (notificationHandler != null) {

                        notificationHandler.removeCallbacksAndMessages(null);
                        notificationHandler = null;

                    }

                    nextToCurrentTransition = false;

                }

                detailedToSimpleTransition = true;

                if (simpleToDetailedTransition) {

                    notificationManager.cancelAll();

                    if (notificationHandler != null) {

                        notificationHandler.removeCallbacksAndMessages(null);
                        notificationHandler = null;

                    }

                    simpleToDetailedTransition = false;

                }

                if (notificationHandler == null) {

                    Intent homeActivityIntent = new Intent(getApplicationContext(), Main.class);
                    PendingIntent addHomeActivityIntent = PendingIntent.getActivity(getApplicationContext(), 0, homeActivityIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                    Intent homeworkActivityIntent = new Intent(getApplicationContext(), AddHomework.class).putExtra("origin_notification", true);
                    PendingIntent addHomeworkActivityIntent = PendingIntent.getActivity(getApplicationContext(), 0, homeworkActivityIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                    remoteViews = new RemoteViews(getPackageName(), R.layout.view_notification);
                    remoteViews.setOnClickPendingIntent(R.id.view_notification_button, addHomeworkActivityIntent);

                    notificationCompatBuilder = new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setOngoing(true)
                            .setContentIntent(addHomeActivityIntent)
                            .setContent(remoteViews)
                            .setPriority(Notification.PRIORITY_MAX)
                            .setWhen(0);

                    notificationRunnable = new Runnable() {

                        public void run() {

                            DateTime currentTime = new DateTime();

                            long currentClassTotal = new Interval(currentClass.getStartTime().toDateTimeToday(), currentClass.getEndTime().toDateTimeToday()).toDurationMillis();
                            long currentClassProgress = new Interval(currentClass.getStartTime().toDateTimeToday(), currentTime).toDurationMillis();

                            int percentageValueInt = (int) (currentClassProgress * 100 / currentClassTotal);

                            int minutesRemaining = currentClass.getEndTime().toDateTimeToday().getMinuteOfDay() - currentTime.getMinuteOfDay() - 1;
                            String remainingTitleText;

                            if (minutesRemaining == 1)
                                remainingTitleText = "1 min. left";
                            else
                                remainingTitleText = minutesRemaining + " mins. left";

                            remoteViews.setTextViewText(R.id.view_notification_header, currentClass.getName() + " - " + remainingTitleText);

                            if (DataStore.isNextClasses())
                                remoteViews.setTextViewText(R.id.view_notification_text, "Next: " + DataStore.getNextClass().getName() + " at " + DataStore.getNextClass().getLocation());
                            else
                                remoteViews.setTextViewText(R.id.view_notification_text, "No further classes");

                            String progressBarText = "";
                            int progressBarProgress = 0;

                            if (percentageValueInt >= 0 && percentageValueInt <= 100) {

                                progressBarText = String.valueOf(percentageValueInt) + "%";
                                progressBarProgress = percentageValueInt;

                                remoteViews.setTextViewText(R.id.view_notification_progress_text, String.valueOf(percentageValueInt));
                                remoteViews.setProgressBar(R.id.view_notification_progress_bar, 100, percentageValueInt, false);

                            } else if (percentageValueInt < 0) {

                                progressBarText = "0%";
                                progressBarProgress = 0;

                                remoteViews.setTextViewText(R.id.view_notification_progress_text, "0");
                                remoteViews.setProgressBar(R.id.view_notification_progress_bar, 100, 0, false);

                            } else if (percentageValueInt > 100) {

                                progressBarText = "100%";
                                progressBarProgress = 100;

                                remoteViews.setTextViewText(R.id.view_notification_progress_text, "100");
                                remoteViews.setProgressBar(R.id.view_notification_progress_bar, 100, 100, false);

                            }

                            DataStore.setProgressBarText(progressBarText);
                            DataStore.setProgressBarProgress(progressBarProgress);

                            notificationManager.notify(0, notificationCompatBuilder.build());

                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent("update_progress_bar"));

                            notificationHandler.postDelayed(this, 5000);

                        }

                    };

                    notificationHandler = new Handler();

                    notificationHandler.post(notificationRunnable);

                }

            } else {

                currentToNextTransition = true;

                if (nextToCurrentTransition) {

                    notificationManager.cancelAll();

                    if (notificationHandler != null) {

                        notificationHandler.removeCallbacksAndMessages(null);
                        notificationHandler = null;

                    }

                    nextToCurrentTransition = false;

                }

                simpleToDetailedTransition = true;

                if (detailedToSimpleTransition) {

                    notificationManager.cancelAll();

                    if (notificationHandler != null) {

                        notificationHandler.removeCallbacksAndMessages(null);
                        notificationHandler = null;

                    }

                    detailedToSimpleTransition = false;

                }

                if (notificationHandler == null) {

                    Intent homeActivityIntent = new Intent(getApplicationContext(), Main.class);
                    PendingIntent addHomeActivityIntent = PendingIntent.getActivity(getApplicationContext(), 0, homeActivityIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                    //Intent homeworkActivityIntent = new Intent(getApplicationContext(), AddHomework.class).putExtra("origin_notification", true);
                    //PendingIntent addHomeworkActivityIntent = PendingIntent.getActivity(getApplicationContext(), 0, homeworkActivityIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                    notificationCompatBuilder = new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setOngoing(true)
                            .setContentIntent(addHomeActivityIntent)
                            .setPriority(Notification.PRIORITY_MAX)
                            .setWhen(0);

                    notificationRunnable = new Runnable() {

                        public void run() {

                            DateTime currentTime = new DateTime();

                            long currentClassTotal = new Interval(currentClass.getStartTime().toDateTimeToday(), currentClass.getEndTime().toDateTimeToday()).toDurationMillis();
                            long currentClassProgress = new Interval(currentClass.getStartTime().toDateTimeToday(), currentTime).toDurationMillis();

                            int percentageValueInt = (int) (currentClassProgress * 100 / currentClassTotal);

                            int minutesRemaining = currentClass.getEndTime().toDateTimeToday().getMinuteOfDay() - currentTime.getMinuteOfDay() - 1;
                            String remainingText;

                            if (minutesRemaining == 1)
                                remainingText = "1 minute left";
                            else
                                remainingText = minutesRemaining + " minutes left";

                            if (DataStore.isNextClasses())
                                remainingText += ", " + DataStore.getNextClass().getName() + " next";
                            else
                                remainingText += ", no further classes";

                            notificationCompatBuilder.setContentTitle(currentClass.getName());
                            notificationCompatBuilder.setContentText(remainingText);

                            String progressBarText = "";
                            int progressBarProgress = 0;

                            if (percentageValueInt >= 0 && percentageValueInt <= 100) {

                                progressBarText = String.valueOf(percentageValueInt) + "%";
                                progressBarProgress = percentageValueInt;

                            } else if (percentageValueInt < 0) {

                                progressBarText = "0%";
                                progressBarProgress = 0;

                            } else if (percentageValueInt > 100) {

                                progressBarText = "100%";
                                progressBarProgress = 100;

                            }

                            DataStore.setProgressBarText(progressBarText);
                            DataStore.setProgressBarProgress(progressBarProgress);

                            notificationManager.notify(0, notificationCompatBuilder.build());

                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent("update_progress_bar"));

                            notificationHandler.postDelayed(this, 5000);

                        }

                    };

                    notificationHandler = new Handler();

                    notificationHandler.post(notificationRunnable);

                }

            }

        } else if (DataStore.isNextClasses()) {

            nextToCurrentTransition = true;

            if (currentToNextTransition) {

                notificationManager.cancelAll();

                if (notificationHandler != null) {

                    notificationHandler.removeCallbacksAndMessages(null);
                    notificationHandler = null;

                }

                currentToNextTransition = false;

            }

            DateTime currentTimeNow = new DateTime();

            int minutesLeft = Minutes.minutesBetween(currentTimeNow, DataStore.getNextClass().getStartTime().toDateTimeToday()).getMinutes();

            if (minutesLeft <= Integer.parseInt(sharedPreferences.getString("next_class_notification_minutes", "30"))) {

                Intent homeActivityIntent = new Intent(getApplicationContext(), Main.class);
                PendingIntent addHomeActivityIntent = PendingIntent.getActivity(getApplicationContext(), 0, homeActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                notificationCompatBuilder = new NotificationCompat.Builder(getApplicationContext());
                notificationCompatBuilder.setOngoing(true);
                notificationCompatBuilder.setSmallIcon(R.mipmap.ic_launcher);
                notificationCompatBuilder.setContentIntent(addHomeActivityIntent);
                notificationCompatBuilder.setWhen(0);
                notificationCompatBuilder.setVisibility(Notification.VISIBILITY_PUBLIC);
                notificationCompatBuilder.setPriority(Notification.PRIORITY_MAX);

                notificationCompatBuilder.setContentTitle("Next: " + DataStore.getNextClass().getName());

                if (minutesLeft >= 60)
                    notificationCompatBuilder.setContentText("In 60 minutes at " + DataStore.getNextClass().getLocation());
                else if (minutesLeft <= 0)
                    notificationCompatBuilder.setContentText("In 0 minutes at " + DataStore.getNextClass().getLocation());
                else if (minutesLeft == 1)
                    notificationCompatBuilder.setContentText("In 1 minute at " + DataStore.getNextClass().getLocation());
                else
                    notificationCompatBuilder.setContentText("In " + minutesLeft + " minutes at " + DataStore.getNextClass().getLocation());

                notificationManager.notify(1, notificationCompatBuilder.build());

            }

        } else {

            currentToNextTransition = false;
            nextToCurrentTransition = false;

            notificationManager.cancelAll();

            if (notificationHandler != null) {

                notificationHandler.removeCallbacksAndMessages(null);
                notificationHandler = null;

            }

        }

        if (sharedPreferences.getBoolean("tomorrow_classes", true)) {

            int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

            if (day > 0 && day < 7)
                day++;
            else
                day = 1;

            Cursor tomorrowCursor = databaseHelper.getClasses(day);

            LinkedList<StandardClass> tomorrowClassesLinkedList = new LinkedList<>();

            while (tomorrowCursor.moveToNext()) {

                tomorrowClassesLinkedList.add(new StandardClass(getApplicationContext(), tomorrowCursor.getString(1), tomorrowCursor.getString(2), tomorrowCursor.getString(3)));

            }

            if (!tomorrowClassesLinkedList.isEmpty()) {

                DataStore.setIsTomorrowClasses(true);
                DataStore.setTomorrowClassesLinkedList(tomorrowClassesLinkedList);

            } else {

                DataStore.setIsTomorrowClasses(false);

            }

        }

        databaseHelper.close();

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent("update_UI"));

    }

    private void getTimetable() {

        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        Cursor cursor;

        for (int i = 1; i < Calendar.SATURDAY + 1; i++) {

            LinkedList<StandardClass> classesLinkedList = new LinkedList<>();

            cursor = databaseHelper.getClasses(i);

            while (cursor.moveToNext()) {

                classesLinkedList.add(new StandardClass(getApplicationContext(), cursor.getString(1), cursor.getString(2), cursor.getString(3)));

            }

            if (!classesLinkedList.isEmpty())
                DataStore.setClassesLinkedListOfDay(i, classesLinkedList);
            else
                DataStore.setClassesLinkedListOfDay(i, null);

        }

        databaseHelper.close();

    }

    private void getClasses() {

        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        Cursor cursor = databaseHelper.getClasses();

        LinkedList<SlimClass> slimClassLinkedList = new LinkedList<>();
        List<String> classNamesList = new ArrayList<>();

        while (cursor.moveToNext()) {

            slimClassLinkedList.add(new SlimClass(cursor.getString(1), cursor.getString(3), cursor.getString(2)));
            classNamesList.add(cursor.getString(1));

        }

        Collections.reverse(classNamesList);

        DataStore.setAllClassesLinkedList(slimClassLinkedList);
        DataStore.setAllClassNamesList(classNamesList);

        databaseHelper.close();

    }

}