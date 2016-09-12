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
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RemoteViews;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.activity.AddHomework;
import com.ghofrani.classapp.activity.Main;
import com.ghofrani.classapp.event.Update;
import com.ghofrani.classapp.event.UpdateClassesUI;
import com.ghofrani.classapp.event.UpdateHomeworkUI;
import com.ghofrani.classapp.event.UpdateProgressUI;
import com.ghofrani.classapp.model.Homework;
import com.ghofrani.classapp.model.SlimClass;
import com.ghofrani.classapp.model.StandardClass;
import com.ghofrani.classapp.module.DataSingleton;
import com.ghofrani.classapp.module.DatabaseHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.LocalTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

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

    private int progressBarId;
    private int progressTextId;
    private int headerId;
    private int textId;
    private Handler handler;
    private Runnable notificationRunnable;
    private Runnable noNotificationRunnable;
    private NotificationCompat.Builder notificationCompatBuilder;
    private NotificationManager notificationManager;
    private RemoteViews remoteViews;

    private int red;
    private int pink;
    private int purple;
    private int deepPurple;
    private int indigo;
    private int blue;
    private int lightBlue;
    private int cyan;
    private int teal;
    private int green;
    private int lightGreen;
    private int lime;
    private int yellow;
    private int amber;
    private int orange;
    private int deepOrange;
    private int black;
    private int blueGrey;

    private DateTimeFormatter dateTimeFormatterAMPM;

    private SharedPreferences sharedPreferences;
    private DatabaseHelper databaseHelper;

    private boolean currentToNextTransition = false;
    private boolean nextToCurrentTransition = false;
    private boolean simpleToDetailedTransition = false;
    private boolean detailedToSimpleTransition = false;

    private boolean is24Hour;

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

        EventBus.getDefault().register(this);
        registerBroadcastReceiver();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        databaseHelper = new DatabaseHelper(this);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        remoteViews = new RemoteViews(getPackageName(), R.layout.view_notification);

        progressBarId = R.id.view_notification_progress_bar_red;
        textId = R.id.view_notification_text;
        headerId = R.id.view_notification_header;
        progressTextId = R.id.view_notification_progress_text;

        dateTimeFormatterAMPM = DateTimeFormat.forPattern("h:mm a");

        red = ContextCompat.getColor(this, R.color.red);
        pink = ContextCompat.getColor(this, R.color.pink);
        purple = ContextCompat.getColor(this, R.color.purple);
        deepPurple = ContextCompat.getColor(this, R.color.deep_purple);
        indigo = ContextCompat.getColor(this, R.color.indigo);
        blue = ContextCompat.getColor(this, R.color.blue);
        lightBlue = ContextCompat.getColor(this, R.color.light_blue);
        cyan = ContextCompat.getColor(this, R.color.cyan);
        teal = ContextCompat.getColor(this, R.color.teal);
        green = ContextCompat.getColor(this, R.color.green);
        lightGreen = ContextCompat.getColor(this, R.color.light_green);
        lime = ContextCompat.getColor(this, R.color.lime);
        yellow = ContextCompat.getColor(this, R.color.yellow);
        amber = ContextCompat.getColor(this, R.color.amber);
        orange = ContextCompat.getColor(this, R.color.orange);
        deepOrange = ContextCompat.getColor(this, R.color.deep_orange);
        black = ContextCompat.getColor(this, R.color.black);
        blueGrey = ContextCompat.getColor(this, R.color.blue_grey);

        getData();
        getHomework();
        getTimetable();
        getClasses();

    }

    @Subscribe
    public void onEvent(Update updateEvent) {

        if (updateEvent.isData())
            getData();

        if (updateEvent.isHomework())
            getHomework();

        if (updateEvent.isTimetable())
            getTimetable();

        if (updateEvent.isClasses())
            getClasses();

    }

    @Override
    public void onDestroy() {

        unregisterReceiver(backgroundBroadcastReceiver);
        EventBus.getDefault().unregister(this);

        notificationManager.cancelAll();

        if (handler != null) {

            handler.removeCallbacksAndMessages(null);
            handler = null;

        }

        sharedPreferences = null;
        databaseHelper.close();

        backgroundIntentFilter = null;
        backgroundBroadcastReceiver = null;
        notificationRunnable = null;
        noNotificationRunnable = null;
        notificationCompatBuilder = null;
        notificationManager = null;
        remoteViews = null;
        databaseHelper = null;
        dateTimeFormatterAMPM = null;

        super.onDestroy();

    }

    private void registerBroadcastReceiver() {

        backgroundBroadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                switch (intent.getAction()) {

                    case Intent.ACTION_TIME_TICK:

                        getData();
                        getHomework();

                        break;

                    case Intent.ACTION_TIMEZONE_CHANGED:

                        getData();
                        getHomework();
                        getTimetable();

                        break;

                    case Intent.ACTION_TIME_CHANGED:

                        getData();
                        getHomework();
                        getTimetable();

                        break;

                    case Intent.ACTION_DATE_CHANGED:

                        getData();
                        getHomework();
                        getTimetable();

                        break;

                }

            }

        };

        registerReceiver(backgroundBroadcastReceiver, backgroundIntentFilter);

    }

    private void getData() {

        if (handler != null) {

            handler.removeCallbacksAndMessages(null);
            handler = null;

        }

        is24Hour = sharedPreferences.getBoolean("24_hour_time", true);

        final Cursor todayCursor = databaseHelper.getClassesCursor(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));

        StandardClass currentClass = null;
        StandardClass nextClass = null;
        ArrayList<StandardClass> nextClassesArrayList = new ArrayList<>();

        final LocalTime currentTime = new LocalTime().now();

        try {

            while (todayCursor.moveToNext()) {

                final StandardClass standardClass = new StandardClass(todayCursor.getString(1),
                        LocalTime.parse(todayCursor.getString(2)),
                        LocalTime.parse(todayCursor.getString(3)),
                        is24Hour,
                        dateTimeFormatterAMPM,
                        databaseHelper.getClassLocation(todayCursor.getString(1)),
                        databaseHelper.getClassTeacher(todayCursor.getString(1)),
                        databaseHelper.getClassColor(todayCursor.getString(1)));

                if (standardClass.getStartTime().isAfter(currentTime)) {

                    nextClassesArrayList.add(standardClass);

                    if (nextClass == null)
                        nextClass = standardClass;

                } else if (standardClass.getStartTime().isBefore(currentTime) && standardClass.getEndTime().isAfter(currentTime)) {

                    currentClass = standardClass;

                }

            }

        } finally {

            todayCursor.close();

        }

        if (currentClass != null) {

            final StandardClass finalCurrentClass = currentClass;

            if (sharedPreferences.getBoolean("class_notification", true)) {

                if (sharedPreferences.getBoolean("detailed_notification", true)) {

                    currentToNextTransition = true;

                    if (nextToCurrentTransition) {

                        notificationManager.cancelAll();
                        nextToCurrentTransition = false;

                    }

                    detailedToSimpleTransition = true;

                    if (simpleToDetailedTransition) {

                        notificationManager.cancelAll();
                        simpleToDetailedTransition = false;

                    }

                    final Intent homeActivityIntent = new Intent(this, Main.class);
                    final PendingIntent addHomeActivityIntent = PendingIntent.getActivity(this, 0, homeActivityIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                    final Intent homeworkActivityIntent = new Intent(this, AddHomework.class).putExtra("origin_notification", true);
                    final PendingIntent addHomeworkActivityIntent = PendingIntent.getActivity(this, 0, homeworkActivityIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                    remoteViews.setOnClickPendingIntent(R.id.view_notification_button, addHomeworkActivityIntent);

                    remoteViews.setInt(progressBarId, "setVisibility", View.GONE);
                    remoteViews.setInt(textId, "setVisibility", View.GONE);
                    remoteViews.setInt(progressTextId, "setVisibility", View.GONE);
                    remoteViews.setInt(headerId, "setVisibility", View.GONE);

                    if (sharedPreferences.getBoolean("flip_colors", false)) {

                        remoteViews.setInt(R.id.view_notification_layout, "setBackgroundColor", finalCurrentClass.getColor());

                        if (finalCurrentClass.getColor() == lime || finalCurrentClass.getColor() == yellow || finalCurrentClass.getColor() == amber) {

                            remoteViews.setInt(R.id.view_notification_button, "setColorFilter", Color.BLACK);

                            progressBarId = R.id.view_notification_progress_bar_black_contrast;
                            textId = R.id.view_notification_text_black;
                            progressTextId = R.id.view_notification_progress_text;
                            headerId = R.id.view_notification_header_black;

                        } else {

                            remoteViews.setInt(R.id.view_notification_button, "setColorFilter", Color.WHITE);

                            progressBarId = R.id.view_notification_progress_bar_white;
                            textId = R.id.view_notification_text_white;
                            progressTextId = R.id.view_notification_progress_text_white;
                            headerId = R.id.view_notification_header_white;

                        }


                    } else {

                        remoteViews.setInt(R.id.view_notification_button, "setColorFilter", finalCurrentClass.getColor());
                        remoteViews.setInt(R.id.view_notification_layout, "setBackgroundColor", Color.TRANSPARENT);

                        if (finalCurrentClass.getColor() == red) {

                            progressBarId = R.id.view_notification_progress_bar_red;

                        } else if (finalCurrentClass.getColor() == pink) {

                            progressBarId = R.id.view_notification_progress_bar_pink;

                        } else if (finalCurrentClass.getColor() == purple) {

                            progressBarId = R.id.view_notification_progress_bar_purple;

                        } else if (finalCurrentClass.getColor() == deepPurple) {

                            progressBarId = R.id.view_notification_progress_bar_deep_purple;

                        } else if (finalCurrentClass.getColor() == indigo) {

                            progressBarId = R.id.view_notification_progress_bar_indigo;

                        } else if (finalCurrentClass.getColor() == blue) {

                            progressBarId = R.id.view_notification_progress_bar_blue;

                        } else if (finalCurrentClass.getColor() == lightBlue) {

                            progressBarId = R.id.view_notification_progress_bar_light_blue;

                        } else if (finalCurrentClass.getColor() == cyan) {

                            progressBarId = R.id.view_notification_progress_bar_cyan;

                        } else if (finalCurrentClass.getColor() == teal) {

                            progressBarId = R.id.view_notification_progress_bar_teal;

                        } else if (finalCurrentClass.getColor() == green) {

                            progressBarId = R.id.view_notification_progress_bar_green;

                        } else if (finalCurrentClass.getColor() == lightGreen) {

                            progressBarId = R.id.view_notification_progress_bar_light_green;

                        } else if (finalCurrentClass.getColor() == lime) {

                            progressBarId = R.id.view_notification_progress_bar_lime;

                        } else if (finalCurrentClass.getColor() == yellow) {

                            progressBarId = R.id.view_notification_progress_bar_yellow;

                        } else if (finalCurrentClass.getColor() == amber) {

                            progressBarId = R.id.view_notification_progress_bar_amber;

                        } else if (finalCurrentClass.getColor() == orange) {

                            progressBarId = R.id.view_notification_progress_bar_orange;

                        } else if (finalCurrentClass.getColor() == deepOrange) {

                            progressBarId = R.id.view_notification_progress_bar_deep_orange;

                        } else if (finalCurrentClass.getColor() == black) {

                            progressBarId = R.id.view_notification_progress_bar_black;

                        } else if (finalCurrentClass.getColor() == blueGrey) {

                            progressBarId = R.id.view_notification_progress_bar_blue_grey;

                        }

                        textId = R.id.view_notification_text;
                        progressTextId = R.id.view_notification_progress_text;
                        headerId = R.id.view_notification_header;

                    }

                    remoteViews.setInt(progressBarId, "setVisibility", View.VISIBLE);
                    remoteViews.setInt(textId, "setVisibility", View.VISIBLE);
                    remoteViews.setInt(progressTextId, "setVisibility", View.VISIBLE);
                    remoteViews.setInt(headerId, "setVisibility", View.VISIBLE);

                    notificationCompatBuilder = new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setOngoing(true)
                            .setContentIntent(addHomeActivityIntent)
                            .setContent(remoteViews)
                            .setPriority(Notification.PRIORITY_MAX)
                            .setWhen(0);

                    notificationRunnable = new Runnable() {

                        public void run() {

                            final DateTime currentTime = new DateTime();

                            final long currentClassTotal = new Interval(finalCurrentClass.getStartTime().toDateTimeToday(), finalCurrentClass.getEndTime().toDateTimeToday()).toDurationMillis();
                            final long currentClassProgress = new Interval(finalCurrentClass.getStartTime().toDateTimeToday(), currentTime).toDurationMillis();

                            final int percentageValueInt = (int) (currentClassProgress * 100 / currentClassTotal);

                            final int minutesRemaining = finalCurrentClass.getEndTime().toDateTimeToday().getMinuteOfDay() - currentTime.getMinuteOfDay() - 1;
                            String remainingTitleText;

                            if (minutesRemaining == 1)
                                remainingTitleText = "1 min. left";
                            else
                                remainingTitleText = minutesRemaining + " mins. left";

                            remoteViews.setTextViewText(headerId, finalCurrentClass.getName() + " • " + remainingTitleText);

                            if (DataSingleton.getInstance().getNextClass() != null)
                                remoteViews.setTextViewText(textId, "Next: " + DataSingleton.getInstance().getNextClass().getName() + (DataSingleton.getInstance().getNextClass().hasLocation() ? " at " + DataSingleton.getInstance().getNextClass().getLocation() : ""));
                            else
                                remoteViews.setTextViewText(textId, "No further classes");

                            String progressBarText = "";
                            int progressBarProgress = 0;

                            if (percentageValueInt >= 0 && percentageValueInt <= 100) {

                                progressBarText = String.valueOf(percentageValueInt) + "%";
                                progressBarProgress = percentageValueInt;

                                remoteViews.setTextViewText(progressTextId, String.valueOf(percentageValueInt));
                                remoteViews.setProgressBar(progressBarId, 100, percentageValueInt, false);

                            } else if (percentageValueInt < 0) {

                                progressBarText = "0%";
                                progressBarProgress = 0;

                                remoteViews.setTextViewText(progressTextId, "0");
                                remoteViews.setProgressBar(progressBarId, 100, 0, false);

                            } else if (percentageValueInt > 100) {

                                progressBarText = "100%";
                                progressBarProgress = 100;

                                remoteViews.setTextViewText(progressTextId, "100");
                                remoteViews.setProgressBar(progressBarId, 100, 100, false);

                            }

                            DataSingleton.getInstance().setMinutesLeftText(remainingTitleText);
                            DataSingleton.getInstance().setProgressBarProgress(progressBarProgress);
                            DataSingleton.getInstance().setProgessbarText(progressBarText);

                            EventBus.getDefault().post(new UpdateProgressUI());

                            notificationManager.notify(0, notificationCompatBuilder.build());

                            handler.postDelayed(this, 15000);

                        }

                    };

                    handler = new Handler();

                    handler.post(notificationRunnable);

                } else {

                    currentToNextTransition = true;

                    if (nextToCurrentTransition) {

                        notificationManager.cancelAll();
                        nextToCurrentTransition = false;

                    }

                    simpleToDetailedTransition = true;

                    if (detailedToSimpleTransition) {

                        notificationManager.cancelAll();
                        detailedToSimpleTransition = false;

                    }

                    final Intent homeActivityIntent = new Intent(this, Main.class);
                    final PendingIntent addHomeActivityIntent = PendingIntent.getActivity(this, 0, homeActivityIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                    //final Intent homeworkActivityIntent = new Intent(this, AddHomework.class).putExtra("origin_notification", true);
                    //final PendingIntent addHomeworkActivityIntent = PendingIntent.getActivity(this, 0, homeworkActivityIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                    notificationCompatBuilder = new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setOngoing(true)
                            .setColor(finalCurrentClass.getColor())
                            .setContentIntent(addHomeActivityIntent)
                            .setPriority(Notification.PRIORITY_MAX)
                            .setWhen(0);

                    notificationRunnable = new Runnable() {

                        public void run() {

                            final DateTime currentTime = new DateTime();

                            final long currentClassTotal = new Interval(finalCurrentClass.getStartTime().toDateTimeToday(), finalCurrentClass.getEndTime().toDateTimeToday()).toDurationMillis();
                            final long currentClassProgress = new Interval(finalCurrentClass.getStartTime().toDateTimeToday(), currentTime).toDurationMillis();

                            final int percentageValueInt = (int) (currentClassProgress * 100 / currentClassTotal);

                            final int minutesRemaining = finalCurrentClass.getEndTime().toDateTimeToday().getMinuteOfDay() - currentTime.getMinuteOfDay() - 1;
                            String remainingText;

                            if (minutesRemaining == 1)
                                remainingText = "1 min. left";
                            else
                                remainingText = minutesRemaining + " mins. left";

                            if (DataSingleton.getInstance().getNextClass() != null)
                                remainingText += " • Next: " + DataSingleton.getInstance().getNextClass().getName();
                            else
                                remainingText += " • No further classes";

                            notificationCompatBuilder.setContentTitle(finalCurrentClass.getName());
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

                            DataSingleton.getInstance().setMinutesLeftText(remainingText);
                            DataSingleton.getInstance().setProgressBarProgress(progressBarProgress);
                            DataSingleton.getInstance().setProgessbarText(progressBarText);

                            EventBus.getDefault().post(new UpdateProgressUI());

                            notificationManager.notify(0, notificationCompatBuilder.build());

                            handler.postDelayed(this, 15000);

                        }

                    };

                    handler = new Handler();

                    handler.post(notificationRunnable);

                }

            } else {

                detailedToSimpleTransition = false;
                simpleToDetailedTransition = false;

                currentToNextTransition = true;
                nextToCurrentTransition = false;

                notificationManager.cancelAll();

                if (noNotificationRunnable == null) {

                    noNotificationRunnable = new Runnable() {

                        @Override
                        public void run() {

                            final DateTime currentTime = new DateTime();

                            final long currentClassTotal = new Interval(finalCurrentClass.getStartTime().toDateTimeToday(), finalCurrentClass.getEndTime().toDateTimeToday()).toDurationMillis();
                            final long currentClassProgress = new Interval(finalCurrentClass.getStartTime().toDateTimeToday(), currentTime).toDurationMillis();

                            final int percentageValueInt = (int) (currentClassProgress * 100 / currentClassTotal);

                            final int minutesRemaining = finalCurrentClass.getEndTime().toDateTimeToday().getMinuteOfDay() - currentTime.getMinuteOfDay() - 1;
                            String remainingText;

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

                            if (minutesRemaining == 1)
                                remainingText = "1 min. left";
                            else
                                remainingText = minutesRemaining + " mins. left";

                            DataSingleton.getInstance().setMinutesLeftText(remainingText);
                            DataSingleton.getInstance().setProgressBarProgress(progressBarProgress);
                            DataSingleton.getInstance().setProgessbarText(progressBarText);

                            EventBus.getDefault().post(new UpdateProgressUI());

                            handler.postDelayed(this, 15000);

                        }

                    };

                }

                handler = new Handler();

                handler.post(noNotificationRunnable);

            }

        } else if (nextClass != null) {

            nextToCurrentTransition = true;

            if (currentToNextTransition) {

                notificationManager.cancelAll();
                currentToNextTransition = false;

            }

            final DateTime currentTimeNow = new DateTime();

            final int minutesLeft = Minutes.minutesBetween(currentTimeNow, nextClass.getStartTime().toDateTimeToday()).getMinutes();

            if (minutesLeft <= Integer.parseInt(sharedPreferences.getString("next_class_notification_minutes", "30"))) {

                final Intent homeActivityIntent = new Intent(this, Main.class);
                final PendingIntent addHomeActivityIntent = PendingIntent.getActivity(this, 0, homeActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                notificationCompatBuilder = new NotificationCompat.Builder(this)
                        .setOngoing(true)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(addHomeActivityIntent)
                        .setWhen(0)
                        .setColor(nextClass.getColor())
                        .setVisibility(Notification.VISIBILITY_PUBLIC)
                        .setPriority(Notification.PRIORITY_MAX);

                notificationCompatBuilder.setContentTitle("Next: " + nextClass.getName());

                if (minutesLeft >= 60)
                    notificationCompatBuilder.setContentText("In 60 minutes" + (nextClass.hasLocation() ? " at " + nextClass.getLocation() : ""));
                else if (minutesLeft <= 0)
                    notificationCompatBuilder.setContentText("In 0 minutes" + (nextClass.hasLocation() ? " at " + nextClass.getLocation() : ""));
                else if (minutesLeft == 1)
                    notificationCompatBuilder.setContentText("In 1 minute" + (nextClass.hasLocation() ? " at " + nextClass.getLocation() : ""));
                else
                    notificationCompatBuilder.setContentText("In " + minutesLeft + " minutes" + (nextClass.hasLocation() ? " at " + nextClass.getLocation() : ""));

                notificationManager.notify(1, notificationCompatBuilder.build());

            } else {

                notificationManager.cancelAll();

            }

        } else {

            currentToNextTransition = false;
            nextToCurrentTransition = false;
            detailedToSimpleTransition = false;
            simpleToDetailedTransition = false;

            notificationManager.cancelAll();

        }

        ArrayList<StandardClass> tomorrowClassesArrayList = new ArrayList<>();

        if (sharedPreferences.getBoolean("tomorrow_classes", true)) {

            int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

            if (day > 0 && day < 7)
                day++;
            else
                day = 1;

            Cursor tomorrowCursor = databaseHelper.getClassesCursor(day);

            try {

                while (tomorrowCursor.moveToNext()) {

                    tomorrowClassesArrayList.add(new StandardClass(tomorrowCursor.getString(1),
                            LocalTime.parse(tomorrowCursor.getString(2)),
                            LocalTime.parse(tomorrowCursor.getString(3)),
                            is24Hour,
                            dateTimeFormatterAMPM,
                            databaseHelper.getClassLocation(tomorrowCursor.getString(1)),
                            databaseHelper.getClassTeacher(tomorrowCursor.getString(1)),
                            databaseHelper.getClassColor(tomorrowCursor.getString(1))));

                }

            } finally {

                tomorrowCursor.close();

            }

        }

        DataSingleton.getInstance().setCurrentClass(currentClass);
        DataSingleton.getInstance().setNextClass(nextClass);
        DataSingleton.getInstance().setNextClassesArrayList(nextClassesArrayList);
        DataSingleton.getInstance().setTomorrowClassesArrayList(tomorrowClassesArrayList);

        EventBus.getDefault().post(new UpdateClassesUI());

    }

    private void getHomework() {

        Cursor homeworkCursor = databaseHelper.getHomeworkCursor();

        final ArrayList<Homework> todayHomeworkArrayList = new ArrayList<>();
        final ArrayList<Homework> tomorrowHomeworkArrayList = new ArrayList<>();
        final ArrayList<Homework> thisWeekHomeworkArrayList = new ArrayList<>();
        final ArrayList<Homework> nextWeekHomeworkArrayList = new ArrayList<>();
        final ArrayList<Homework> thisMonthHomeworkArrayList = new ArrayList<>();
        final ArrayList<Homework> beyondThisMonthHomeworkArrayList = new ArrayList<>();
        final ArrayList<Homework> pastHomeworkArrayList = new ArrayList<>();

        DateTime today = DateTime.now().withTime(DateTime.now().getHourOfDay(), DateTime.now().getMinuteOfHour(), 0, 0);
        DateTime tomorrow = today.plusDays(1);

        boolean thisWeekEnabled = true;
        Interval thisWeek;
        Interval nextWeek;

        switch (today.getDayOfWeek()) {

            case DateTimeConstants.FRIDAY:

                thisWeekEnabled = false;
                thisWeek = new Interval(today, today);
                nextWeek = new Interval(today.plusDays(2).withTime(0, 0, 0, 0), today.plusDays(9).withTime(0, 0, 0, 0));

                break;

            case DateTimeConstants.SATURDAY:

                thisWeekEnabled = false;
                thisWeek = new Interval(today, today);
                nextWeek = new Interval(today.plusDays(1).withTime(0, 0, 0, 0), today.plusDays(8).withTime(0, 0, 0, 0));

                break;

            default:

                thisWeek = new Interval(tomorrow.plusDays(1).withTime(0, 0, 0, 0), tomorrow.plusDays(DateTimeConstants.SUNDAY - tomorrow.getDayOfWeek()).withTime(0, 0, 0, 0));
                nextWeek = new Interval(thisWeek.getEnd(), thisWeek.getEnd().plusDays(7).withTimeAtStartOfDay());

        }

        boolean thisMonthEnabled = false;
        Interval thisMonth = new Interval(today, today);

        if (nextWeek.getEnd().getMonthOfYear() == today.getMonthOfYear()) {

            if (today.dayOfMonth().withMaximumValue().getDayOfMonth() > nextWeek.getEnd().getDayOfMonth() - 1) {

                thisMonthEnabled = true;
                thisMonth = new Interval(nextWeek.getEnd(), today.dayOfMonth().withMaximumValue());

            }

        }

        DateTimeZone dateTimeZone = DateTimeZone.getDefault();

        try {

            while (homeworkCursor.moveToNext()) {

                Homework homework = new Homework(homeworkCursor.getString(1), homeworkCursor.getString(2), DateTime.parse(homeworkCursor.getString(3)).withZone(dateTimeZone), homeworkCursor.getInt(4) == 1, databaseHelper.getClassColor(homeworkCursor.getString(2)), homeworkCursor.getInt(5) == 1);

                if (today.isAfter(homework.getDateTime())) {

                    pastHomeworkArrayList.add(homework);

                } else if (today.withTimeAtStartOfDay().isEqual(homework.getDateTime().withTimeAtStartOfDay())) {

                    todayHomeworkArrayList.add(homework);

                } else if (tomorrow.withTimeAtStartOfDay().isEqual(homework.getDateTime().withTimeAtStartOfDay())) {

                    tomorrowHomeworkArrayList.add(homework);

                } else if (thisWeek.contains(homework.getDateTime()) && thisWeekEnabled) {

                    thisWeekHomeworkArrayList.add(homework);

                } else if (nextWeek.contains(homework.getDateTime())) {

                    nextWeekHomeworkArrayList.add(homework);

                } else if (thisMonth.contains(homework.getDateTime()) && thisMonthEnabled) {

                    thisMonthHomeworkArrayList.add(homework);

                } else {

                    beyondThisMonthHomeworkArrayList.add(homework);

                }

            }

        } finally {

            homeworkCursor.close();

        }

        DataSingleton.getInstance().setTodayHomeworkArrayList(todayHomeworkArrayList);
        DataSingleton.getInstance().setTomorrowHomeworkArrayList(tomorrowHomeworkArrayList);
        DataSingleton.getInstance().setThisWeekHomeworkArrayList(thisWeekHomeworkArrayList);
        DataSingleton.getInstance().setNextWeekHomeworkArrayList(nextWeekHomeworkArrayList);
        DataSingleton.getInstance().setThisMonthHomeworkArrayList(thisMonthHomeworkArrayList);
        DataSingleton.getInstance().setBeyondThisMonthHomeworkArrayList(beyondThisMonthHomeworkArrayList);
        DataSingleton.getInstance().setPastHomeworkArrayList(pastHomeworkArrayList);

        DataSingleton.getInstance().setNextWeekEnd(nextWeek.getEnd());

        EventBus.getDefault().post(new UpdateHomeworkUI());

    }

    private void setClassesArrayListOfDay(int day, ArrayList<StandardClass> standardClassArrayList) {

        switch (day) {

            case 1:

                DataSingleton.getInstance().setSundayClasses(standardClassArrayList);

                break;

            case 2:

                DataSingleton.getInstance().setMondayClasses(standardClassArrayList);

                break;

            case 3:

                DataSingleton.getInstance().setTuesdayClasses(standardClassArrayList);

                break;

            case 4:

                DataSingleton.getInstance().setWednesdayClasses(standardClassArrayList);

                break;

            case 5:

                DataSingleton.getInstance().setThursdayClasses(standardClassArrayList);

                break;

            case 6:

                DataSingleton.getInstance().setFridayClasses(standardClassArrayList);

                break;

            case 7:

                DataSingleton.getInstance().setSaturdayClasses(standardClassArrayList);

                break;

        }

    }

    private void getTimetable() {

        Cursor timetableCursor = null;

        try {

            for (int i = 1; i < Calendar.SATURDAY + 1; i++) {

                final ArrayList<StandardClass> classesArrayList = new ArrayList<>();

                timetableCursor = databaseHelper.getClassesCursor(i);

                while (timetableCursor.moveToNext()) {

                    classesArrayList.add(new StandardClass(timetableCursor.getString(1),
                            LocalTime.parse(timetableCursor.getString(2)),
                            LocalTime.parse(timetableCursor.getString(3)),
                            is24Hour,
                            dateTimeFormatterAMPM,
                            databaseHelper.getClassLocation(timetableCursor.getString(1)),
                            databaseHelper.getClassTeacher(timetableCursor.getString(1)),
                            databaseHelper.getClassColor(timetableCursor.getString(1))));

                }

                if (!classesArrayList.isEmpty())
                    setClassesArrayListOfDay(i, classesArrayList);
                else
                    setClassesArrayListOfDay(i, null);

            }

        } finally {

            timetableCursor.close();

        }

    }

    private void getClasses() {

        Cursor classesCursor = databaseHelper.getClassesCursor();

        final ArrayList<SlimClass> slimClassArrayList = new ArrayList<>();
        final ArrayList<String> classNamesArrayList = new ArrayList<>();

        try {

            while (classesCursor.moveToNext()) {

                slimClassArrayList.add(new SlimClass(classesCursor.getString(1), classesCursor.getString(2), classesCursor.getString(3), databaseHelper.getClassColor(classesCursor.getString(1))));
                classNamesArrayList.add(classesCursor.getString(1));

            }

        } finally {

            classesCursor.close();

        }

        Collections.reverse(classNamesArrayList);

        DataSingleton.getInstance().setAllClassesArrayList(slimClassArrayList);
        DataSingleton.getInstance().setAllClassNamesArrayList(classNamesArrayList);

    }

}