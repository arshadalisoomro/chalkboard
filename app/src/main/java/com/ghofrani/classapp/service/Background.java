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
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
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

    private SharedPreferences sharedPreferences;
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

        LocalBroadcastManager.getInstance(this).registerReceiver(updateData, new IntentFilter("update_data"));
        LocalBroadcastManager.getInstance(this).registerReceiver(updateClasses, new IntentFilter("update_classes"));

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        progressBarId = R.id.view_notification_progress_bar_red;
        textId = R.id.view_notification_text;
        headerId = R.id.view_notification_header;
        progressTextId = R.id.view_notification_progress_text;

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
        getTimetable();
        getClasses();

    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(updateData);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(updateClasses);

        if (backgroundBroadcastReceiver != null) {

            unregisterReceiver(backgroundBroadcastReceiver);

        }

        if (handler != null) {

            handler.removeCallbacksAndMessages(null);
            handler = null;

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

                        break;

                    case Intent.ACTION_TIMEZONE_CHANGED:

                        getData();
                        getTimetable();

                        break;

                    case Intent.ACTION_TIME_CHANGED:

                        getData();
                        getTimetable();

                        break;

                    case Intent.ACTION_DATE_CHANGED:

                        getData();
                        getTimetable();

                        break;

                    default:

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

        boolean isCurrentClass = false;
        boolean isNextClasses = false;

        if (sharedPreferences == null)
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        Cursor todayCursor = databaseHelper.getClasses(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));

        final ArrayList<StandardClass> nextClassesArrayList = new ArrayList<>();
        boolean nextClassDefined = false;

        final LocalTime currentTime = new LocalTime().now();

        while (todayCursor.moveToNext()) {

            StandardClass standardClass = new StandardClass(this, todayCursor.getString(1), todayCursor.getString(2), todayCursor.getString(3));

            if (standardClass.getStartTime().isAfter(currentTime)) {

                nextClassesArrayList.add(standardClass);
                isNextClasses = true;
                DataStore.isNextClasses = true;

                if (!nextClassDefined) {

                    DataStore.nextClass = standardClass;
                    nextClassDefined = true;

                }

            } else if (standardClass.getEndTime().isAfter(currentTime) && standardClass.getStartTime().isBefore(currentTime)) {

                isCurrentClass = true;
                currentClass = standardClass;
                DataStore.currentClass = standardClass;

            }

        }

        DataStore.isNextClasses = isNextClasses;

        if (isNextClasses)
            DataStore.nextClassesArrayList = nextClassesArrayList;

        DataStore.isCurrentClass = isCurrentClass;

        if (isCurrentClass) {

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

                    if (remoteViews == null)
                        remoteViews = new RemoteViews(getPackageName(), R.layout.view_notification);

                    remoteViews.setOnClickPendingIntent(R.id.view_notification_button, addHomeworkActivityIntent);

                    remoteViews.setInt(progressBarId, "setVisibility", View.GONE);
                    remoteViews.setInt(textId, "setVisibility", View.GONE);
                    remoteViews.setInt(progressTextId, "setVisibility", View.GONE);
                    remoteViews.setInt(headerId, "setVisibility", View.GONE);

                    if (sharedPreferences.getBoolean("flip_colors", false)) {

                        remoteViews.setInt(R.id.view_notification_button, "setColorFilter", Color.WHITE);
                        remoteViews.setInt(R.id.view_notification_layout, "setBackgroundColor", currentClass.getColor());

                        progressBarId = R.id.view_notification_progress_bar_white;
                        textId = R.id.view_notification_text_white;
                        progressTextId = R.id.view_notification_progress_text_white;
                        headerId = R.id.view_notification_header_white;

                    } else {

                        remoteViews.setInt(R.id.view_notification_button, "setColorFilter", currentClass.getColor());
                        remoteViews.setInt(R.id.view_notification_layout, "setBackgroundColor", Color.TRANSPARENT);

                        if (currentClass.getColor() == red) {

                            progressBarId = R.id.view_notification_progress_bar_red;

                        } else if (currentClass.getColor() == pink) {

                            progressBarId = R.id.view_notification_progress_bar_pink;

                        } else if (currentClass.getColor() == purple) {

                            progressBarId = R.id.view_notification_progress_bar_purple;

                        } else if (currentClass.getColor() == deepPurple) {

                            progressBarId = R.id.view_notification_progress_bar_deep_purple;

                        } else if (currentClass.getColor() == indigo) {

                            progressBarId = R.id.view_notification_progress_bar_indigo;

                        } else if (currentClass.getColor() == blue) {

                            progressBarId = R.id.view_notification_progress_bar_blue;

                        } else if (currentClass.getColor() == lightBlue) {

                            progressBarId = R.id.view_notification_progress_bar_light_blue;

                        } else if (currentClass.getColor() == cyan) {

                            progressBarId = R.id.view_notification_progress_bar_cyan;

                        } else if (currentClass.getColor() == teal) {

                            progressBarId = R.id.view_notification_progress_bar_teal;

                        } else if (currentClass.getColor() == green) {

                            progressBarId = R.id.view_notification_progress_bar_green;

                        } else if (currentClass.getColor() == lightGreen) {

                            progressBarId = R.id.view_notification_progress_bar_light_green;

                        } else if (currentClass.getColor() == lime) {

                            progressBarId = R.id.view_notification_progress_bar_lime;

                        } else if (currentClass.getColor() == yellow) {

                            progressBarId = R.id.view_notification_progress_bar_yellow;

                        } else if (currentClass.getColor() == amber) {

                            progressBarId = R.id.view_notification_progress_bar_amber;

                        } else if (currentClass.getColor() == orange) {

                            progressBarId = R.id.view_notification_progress_bar_orange;

                        } else if (currentClass.getColor() == deepOrange) {

                            progressBarId = R.id.view_notification_progress_bar_deep_orange;

                        } else if (currentClass.getColor() == black) {

                            progressBarId = R.id.view_notification_progress_bar_black;

                        } else if (currentClass.getColor() == blueGrey) {

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

                            final long currentClassTotal = new Interval(currentClass.getStartTime().toDateTimeToday(), currentClass.getEndTime().toDateTimeToday()).toDurationMillis();
                            final long currentClassProgress = new Interval(currentClass.getStartTime().toDateTimeToday(), currentTime).toDurationMillis();

                            final int percentageValueInt = (int) (currentClassProgress * 100 / currentClassTotal);

                            final int minutesRemaining = currentClass.getEndTime().toDateTimeToday().getMinuteOfDay() - currentTime.getMinuteOfDay() - 1;
                            String remainingTitleText;

                            if (minutesRemaining == 1)
                                remainingTitleText = "1 min. left";
                            else
                                remainingTitleText = minutesRemaining + " mins. left";

                            remoteViews.setTextViewText(headerId, currentClass.getName() + " • " + remainingTitleText);

                            if (DataStore.isNextClasses)
                                remoteViews.setTextViewText(textId, "Next: " + DataStore.nextClass.getName() + " at " + DataStore.nextClass.getLocation());
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

                            DataStore.progressBarText = progressBarText;
                            DataStore.progressBarProgress = progressBarProgress;

                            notificationManager.notify(0, notificationCompatBuilder.build());

                            LocalBroadcastManager.getInstance(Background.this).sendBroadcast(new Intent("update_progress_bar"));

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
                            .setColor(currentClass.getColor())
                            .setContentIntent(addHomeActivityIntent)
                            .setPriority(Notification.PRIORITY_MAX)
                            .setWhen(0);

                    notificationRunnable = new Runnable() {

                        public void run() {

                            final DateTime currentTime = new DateTime();

                            final long currentClassTotal = new Interval(currentClass.getStartTime().toDateTimeToday(), currentClass.getEndTime().toDateTimeToday()).toDurationMillis();
                            final long currentClassProgress = new Interval(currentClass.getStartTime().toDateTimeToday(), currentTime).toDurationMillis();

                            final int percentageValueInt = (int) (currentClassProgress * 100 / currentClassTotal);

                            final int minutesRemaining = currentClass.getEndTime().toDateTimeToday().getMinuteOfDay() - currentTime.getMinuteOfDay() - 1;
                            String remainingText;

                            if (minutesRemaining == 1)
                                remainingText = "1 min. left";
                            else
                                remainingText = minutesRemaining + " mins. left";

                            if (DataStore.isNextClasses)
                                remainingText += " • Next: " + DataStore.nextClass.getName();
                            else
                                remainingText += " • No further classes";

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

                            DataStore.progressBarText = progressBarText;
                            DataStore.progressBarProgress = progressBarProgress;

                            notificationManager.notify(0, notificationCompatBuilder.build());

                            LocalBroadcastManager.getInstance(Background.this).sendBroadcast(new Intent("update_progress_bar"));

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

                noNotificationRunnable = new Runnable() {

                    @Override
                    public void run() {

                        final DateTime currentTime = new DateTime();

                        final long currentClassTotal = new Interval(currentClass.getStartTime().toDateTimeToday(), currentClass.getEndTime().toDateTimeToday()).toDurationMillis();
                        final long currentClassProgress = new Interval(currentClass.getStartTime().toDateTimeToday(), currentTime).toDurationMillis();

                        final int percentageValueInt = (int) (currentClassProgress * 100 / currentClassTotal);

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

                        DataStore.progressBarText = progressBarText;
                        DataStore.progressBarProgress = progressBarProgress;

                        LocalBroadcastManager.getInstance(Background.this).sendBroadcast(new Intent("update_progress_bar"));

                        handler.postDelayed(this, 15000);

                    }

                };

                handler = new Handler();

                handler.post(noNotificationRunnable);

            }

        } else if (DataStore.isNextClasses) {

            nextToCurrentTransition = true;

            if (currentToNextTransition) {

                notificationManager.cancelAll();
                currentToNextTransition = false;

            }

            final DateTime currentTimeNow = new DateTime();

            final int minutesLeft = Minutes.minutesBetween(currentTimeNow, DataStore.nextClass.getStartTime().toDateTimeToday()).getMinutes();

            if (minutesLeft <= Integer.parseInt(sharedPreferences.getString("next_class_notification_minutes", "30"))) {

                final Intent homeActivityIntent = new Intent(this, Main.class);
                final PendingIntent addHomeActivityIntent = PendingIntent.getActivity(this, 0, homeActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                notificationCompatBuilder = new NotificationCompat.Builder(this)
                        .setOngoing(true)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(addHomeActivityIntent)
                        .setWhen(0)
                        .setColor(DataStore.nextClass.getColor())
                        .setVisibility(Notification.VISIBILITY_PUBLIC)
                        .setPriority(Notification.PRIORITY_MAX);

                notificationCompatBuilder.setContentTitle("Next: " + DataStore.nextClass.getName());

                if (minutesLeft >= 60)
                    notificationCompatBuilder.setContentText("In 60 minutes at " + DataStore.nextClass.getLocation());
                else if (minutesLeft <= 0)
                    notificationCompatBuilder.setContentText("In 0 minutes at " + DataStore.nextClass.getLocation());
                else if (minutesLeft == 1)
                    notificationCompatBuilder.setContentText("In 1 minute at " + DataStore.nextClass.getLocation());
                else
                    notificationCompatBuilder.setContentText("In " + minutesLeft + " minutes at " + DataStore.nextClass.getLocation());

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

        if (sharedPreferences.getBoolean("tomorrow_classes", true)) {

            int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

            if (day > 0 && day < 7)
                day++;
            else
                day = 1;

            Cursor tomorrowCursor = databaseHelper.getClasses(day);

            final ArrayList<StandardClass> tomorrowClassesArrayList = new ArrayList<>();

            while (tomorrowCursor.moveToNext()) {

                tomorrowClassesArrayList.add(new StandardClass(this, tomorrowCursor.getString(1), tomorrowCursor.getString(2), tomorrowCursor.getString(3)));

            }

            if (!tomorrowClassesArrayList.isEmpty()) {

                DataStore.isTomorrowClasses = true;
                DataStore.tomorrowClassesArrayList = tomorrowClassesArrayList;

            } else {

                DataStore.isTomorrowClasses = false;

            }

        }

        databaseHelper.close();

        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("update_UI"));

    }

    private void setClassesArrayListOfDay(int day, ArrayList<StandardClass> standardClassArrayList) {

        switch (day) {

            case 1:

                DataStore.sundayClasses = standardClassArrayList;

                break;

            case 2:

                DataStore.mondayClasses = standardClassArrayList;

                break;

            case 3:

                DataStore.tuesdayClasses = standardClassArrayList;

                break;

            case 4:

                DataStore.wednesdayClasses = standardClassArrayList;

                break;

            case 5:

                DataStore.thursdayClasses = standardClassArrayList;

                break;

            case 6:

                DataStore.fridayClasses = standardClassArrayList;

                break;

            case 7:

                DataStore.saturdayClasses = standardClassArrayList;

                break;

        }

    }

    private void getTimetable() {

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        Cursor cursor;

        for (int i = 1; i < Calendar.SATURDAY + 1; i++) {

            final ArrayList<StandardClass> classesArrayList = new ArrayList<>();

            cursor = databaseHelper.getClasses(i);

            while (cursor.moveToNext()) {

                classesArrayList.add(new StandardClass(this, cursor.getString(1), cursor.getString(2), cursor.getString(3)));

            }

            if (!classesArrayList.isEmpty())
                setClassesArrayListOfDay(i, classesArrayList);
            else
                setClassesArrayListOfDay(i, null);

        }

        databaseHelper.close();

    }

    private void getClasses() {

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        Cursor cursor = databaseHelper.getClasses();

        final ArrayList<SlimClass> slimClassArrayList = new ArrayList<>();
        final ArrayList<String> classNamesArrayList = new ArrayList<>();

        while (cursor.moveToNext()) {

            slimClassArrayList.add(new SlimClass(this, cursor.getString(1), cursor.getString(3), cursor.getString(2)));
            classNamesArrayList.add(cursor.getString(1));

        }

        Collections.reverse(classNamesArrayList);

        DataStore.allClassesArrayList = slimClassArrayList;
        DataStore.allClassNamesArrayList = classNamesArrayList;

        databaseHelper.close();

    }

}