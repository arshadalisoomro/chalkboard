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
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.activity.AddHomeworkActivity;
import com.ghofrani.classapp.activity.HomeActivity;
import com.ghofrani.classapp.modules.DataStore;
import com.ghofrani.classapp.modules.DatabaseHelper;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TimeService extends Service {

    private static final IntentFilter timeIntentFilter;
    private static BroadcastReceiver timeReceiver;

    static {

        timeIntentFilter = new IntentFilter();
        timeIntentFilter.addAction(Intent.ACTION_TIME_TICK);
        timeIntentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        timeIntentFilter.addAction(Intent.ACTION_TIME_CHANGED);
        timeIntentFilter.addAction(Intent.ACTION_DATE_CHANGED);

    }

    private Handler handler;
    private NotificationCompat.Builder notificationCompatBuilder;
    private RemoteViews remoteViews;
    private NotificationManager notificationManager;

    private String currentClassName;
    private DateTime currentClassStartTime;
    private DateTime currentClassEndTime;

    private boolean currentToNextTransition = false;
    private boolean nextToCurrentTransition = false;

    private final BroadcastReceiver updateData = new BroadcastReceiver() {

        @Override

        public void onReceive(Context context, Intent intent) {

            getData();
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

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(updateData, new IntentFilter("updateData"));

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        getData();

        getClasses();

    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(updateData);

        if (timeReceiver != null) {

            getApplicationContext().unregisterReceiver(timeReceiver);

        }

        if (handler != null) {

            handler.removeCallbacksAndMessages(null);
            handler = null;

        }

        notificationManager.cancelAll();

    }

    private void registerBroadcastReceiver() {

        timeReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                switch (intent.getAction()) {

                    case Intent.ACTION_TIME_TICK:

                        getData();

                        break;

                    case Intent.ACTION_TIMEZONE_CHANGED:

                        getData();

                        getClasses();

                        break;

                    case Intent.ACTION_TIME_CHANGED:

                        getData();

                        getClasses();

                        break;

                    case Intent.ACTION_DATE_CHANGED:

                        getData();

                        getClasses();

                        break;

                    default:

                        break;

                }

            }

        };

        getApplicationContext().registerReceiver(timeReceiver, timeIntentFilter);

    }

    private void getData() {

        boolean currentClass = false;
        boolean nextClasses = false;
        boolean tomorrowClasses = false;

        HashMap<String, List<String>> nextClassesHM;
        HashMap<String, List<String>> tomorrowClassesHM = null;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());

        Cursor result = db.getClasses(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));

        Map<String, String> resultMap = new TreeMap<>();

        while (result.moveToNext()) {

            resultMap.put(result.getString(2), result.getString(1) + "," + result.getString(3)
                    + "," + db.getClassLocation(result.getString(1)));

        }

        List<String> nextClassesVoid = new ArrayList<>();

        boolean nextClassDefined = false;

        for (Map.Entry<String, String> entry : resultMap.entrySet()) {

            DateTimeFormatter formatter = DateTimeFormat.forPattern("HHmm");

            LocalTime startTime = formatter.parseLocalTime(entry.getKey());
            String startTimeString = startTime.toString().substring(0, 5);

            String[] valueArray = entry.getValue().split(",");
            String name = valueArray[0];
            String location = valueArray[2];

            LocalTime endTime = formatter.parseLocalTime(valueArray[1]);
            String endTimeString = endTime.toString().substring(0, 5);

            LocalTime currentTime = new LocalTime().now();

            if (startTime.isAfter(currentTime)) {

                nextClassesVoid.add(name + "," + startTimeString + " - " + endTimeString + "," + location);
                nextClasses = true;

                if (!nextClassDefined) {

                    DataStore.setNextClassString(name);
                    DataStore.setNextClassLocation(db.getClassLocation(name));
                    DataStore.setNextClassStartTime(entry.getKey());

                    nextClassDefined = true;

                }

            } else if (endTime.isAfter(currentTime) && startTime.isBefore(currentTime)) {

                currentClassStartTime = startTime.toDateTimeToday();
                currentClassEndTime = endTime.toDateTimeToday();
                currentClassName = name;

                String[] currentClassInfo = new String[4];

                currentClassInfo[0] = name;
                currentClassInfo[1] = location;
                currentClassInfo[2] = startTimeString;
                currentClassInfo[3] = endTimeString;

                DataStore.setCurrentClassInfo(currentClassInfo);

                currentClass = true;

            }

        }

        nextClassesHM = new HashMap<>();
        nextClassesHM.put("Next Classes", nextClassesVoid);

        if (currentClass) {

            currentToNextTransition = true;

            if (nextToCurrentTransition) {

                notificationManager.cancelAll();

                if (handler != null) {

                    handler.removeCallbacksAndMessages(null);
                    handler = null;

                }

                nextToCurrentTransition = false;

            }

            if (handler == null) {

                Intent homeActivityIntent = new Intent(getApplicationContext(), HomeActivity.class);
                PendingIntent addHomeActivityIntent = PendingIntent.getActivity(getApplicationContext(), 0, homeActivityIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                Intent homeworkActivityIntent = new Intent(getApplicationContext(), AddHomeworkActivity.class).putExtra("originNotification", true);
                PendingIntent addHomeworkActivityIntent = PendingIntent.getActivity(getApplicationContext(), 0, homeworkActivityIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                remoteViews = new RemoteViews(getPackageName(), R.layout.view_notification);

                remoteViews.setOnClickPendingIntent(R.id.notification_button, addHomeworkActivityIntent);

                notificationCompatBuilder = new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setOngoing(true)
                        .setContentIntent(addHomeActivityIntent)
                        .setContent(remoteViews)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setWhen(0);

                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                Runnable notificationRunnable = new Runnable() {

                    public void run() {

                        DateTime currentTime = new DateTime();

                        long currentClassTotal = new Interval(currentClassStartTime, currentClassEndTime).toDurationMillis();
                        long currentClassProgress = new Interval(currentClassStartTime, currentTime).toDurationMillis();

                        int percentageValueInt = (int) (currentClassProgress * 100 / currentClassTotal);

                        int minutesRemaining = currentClassEndTime.getMinuteOfDay() - currentTime.getMinuteOfDay() - 1;
                        String remainingText = "";

                        if (minutesRemaining == 1)
                            remainingText = "1 min. left";
                        else
                            remainingText = minutesRemaining + " mins. left";

                        remoteViews.setTextViewText(R.id.notification_header, currentClassName + " - " + remainingText);

                        if(DataStore.getNextClasses())
                            remoteViews.setTextViewText(R.id.notification_text, "Next: " + DataStore.getNextClassString() + " at " + DataStore.getNextClassLocation());
                        else
                            remoteViews.setTextViewText(R.id.notification_text, "No further classes");

                        String text = "";
                        int progressBarProgress = 0;

                        if (percentageValueInt >= 0 && percentageValueInt <= 100) {

                            text = String.valueOf(percentageValueInt) + "%";
                            progressBarProgress = percentageValueInt;

                            remoteViews.setTextViewText(R.id.notification_progress_text, String.valueOf(percentageValueInt));
                            remoteViews.setProgressBar(R.id.notification_progress_bar, 100, percentageValueInt, false);

                        } else if (percentageValueInt < 0) {

                            text = "0%";
                            progressBarProgress = 0;

                            remoteViews.setTextViewText(R.id.notification_progress_text, "0");
                            remoteViews.setProgressBar(R.id.notification_progress_bar, 100, 0, false);

                        } else if (percentageValueInt > 100) {

                            text = "100%";
                            progressBarProgress = 100;

                            remoteViews.setTextViewText(R.id.notification_progress_text, "100");
                            remoteViews.setProgressBar(R.id.notification_progress_bar, 100, 100, false);

                        }

                        DataStore.setProgressBarText(text);
                        DataStore.setProgressBarProgress(progressBarProgress);

                        notificationManager.notify(0, notificationCompatBuilder.build());

                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent("updateProgressBar"));

                        handler.postDelayed(this, 5000);

                    }

                };

                handler = new Handler();

                handler.post(notificationRunnable);

            }

        } else if (nextClasses) {

            nextToCurrentTransition = true;

            if (currentToNextTransition) {

                notificationManager.cancelAll();

                if (handler != null) {

                    handler.removeCallbacksAndMessages(null);
                    handler = null;

                }

                currentToNextTransition = false;

            }

            Intent homeActivityIntent = new Intent(getApplicationContext(), HomeActivity.class);
            PendingIntent addHomeActivityIntent = PendingIntent.getActivity(getApplicationContext(), 0, homeActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            notificationCompatBuilder = new NotificationCompat.Builder(getApplicationContext());
            notificationCompatBuilder.setOngoing(true);
            notificationCompatBuilder.setSmallIcon(R.mipmap.ic_launcher);
            notificationCompatBuilder.setContentIntent(addHomeActivityIntent);
            notificationCompatBuilder.setWhen(0);
            notificationCompatBuilder.setVisibility(Notification.VISIBILITY_PUBLIC);
            notificationCompatBuilder.setPriority(Notification.PRIORITY_MAX);

            notificationCompatBuilder.setContentTitle("Next: " + DataStore.getNextClassString());

            DateTime currentTime = new DateTime();

            int minutesLeft = Minutes.minutesBetween(currentTime, DateTimeFormat.forPattern("HHmm").parseLocalTime(DataStore.getNextClassStartTime()).toDateTimeToday()).getMinutes();

            if (minutesLeft >= 60) {

                notificationCompatBuilder.setContentText("In 60 minutes at " + DataStore.getNextClassLocation());

            } else if (minutesLeft <= 0) {

                notificationCompatBuilder.setContentText("In less than a minute at " + DataStore.getNextClassLocation());

            } else {

                notificationCompatBuilder.setContentText("In " + minutesLeft + " minutes at " + DataStore.getNextClassLocation());

            }

            notificationManager.notify(1, notificationCompatBuilder.build());

        } else {

            currentToNextTransition = false;
            nextToCurrentTransition = false;

            notificationManager.cancelAll();

            if (handler != null) {

                handler.removeCallbacksAndMessages(null);
                handler = null;

            }

        }

        if (sharedPreferences.getBoolean("tomorrowClasses", true)) {

            int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

            if (day > 0 && day < 7)
                day++;
            else
                day = 1;

            Cursor resultTomorrow = db.getClasses(day);

            Map<String, String> resultTomorrowMap = new TreeMap<>();

            while (resultTomorrow.moveToNext()) {

                resultTomorrowMap.put(resultTomorrow.getString(2), resultTomorrow.getString(1) + ","
                        + resultTomorrow.getString(3) + ","
                        + db.getClassLocation(resultTomorrow.getString(1)));

            }

            if (!resultTomorrowMap.isEmpty()) {

                tomorrowClasses = true;

                List<String> tomorrowClassesVoid = new ArrayList<>();

                for (Map.Entry<String, String> entry : resultTomorrowMap.entrySet()) {

                    String startTimeBase = entry.getKey();
                    String startTime = startTimeBase.substring(0, 2) + ":" + startTimeBase.substring(2, 4);

                    String[] valueArray = entry.getValue().split(",");
                    String name = valueArray[0];
                    String endTimeBase = valueArray[1];
                    String endTime = endTimeBase.substring(0, 2) + ":" + endTimeBase.substring(2, 4);
                    String location = valueArray[2];

                    tomorrowClassesVoid.add(name + "," + startTime + " - " + endTime + "," + location);

                }

                tomorrowClassesHM = new HashMap<>();
                tomorrowClassesHM.put("Tomorrow's Classes", tomorrowClassesVoid);

            }

        }

        DataStore.setCurrentClass(currentClass);

        DataStore.setNextClasses(nextClasses);

        if (nextClasses)
            DataStore.setNextClassesHM(nextClassesHM);

        DataStore.setTomorrowClasses(tomorrowClasses);

        if (tomorrowClasses)
            DataStore.setTomorrowClassesHM(tomorrowClassesHM);

        db.close();

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent("updateUI"));

    }

    private void getClasses() {

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        Cursor result;
        TreeMap<String, String> resultMap;
        String[] finalClasses;

        for (int i = 1; i < Calendar.SATURDAY + 1; i++) {

            resultMap = new TreeMap<>();

            result = db.getClasses(i);

            while (result.moveToNext()) {

                resultMap.put(result.getString(2), result.getString(1) + "," + result.getString(3)
                        + "," + db.getClassLocation(result.getString(1)));

            }

            if (!resultMap.isEmpty()) {

                int count = 0;
                finalClasses = new String[resultMap.size()];

                for (Map.Entry<String, String> entry : resultMap.entrySet()) {

                    String startTimeBase = entry.getKey();
                    String startTime = startTimeBase.substring(0, 2) + ":" + startTimeBase.substring(2, 4);

                    String[] valueArray = entry.getValue().split(",");
                    String name = valueArray[0];
                    String endTimeBase = valueArray[1];
                    String endTime = endTimeBase.substring(0, 2) + ":" + endTimeBase.substring(2, 4);
                    String location = valueArray[2];

                    finalClasses[count] = name + "," + startTime + " - " + endTime + "," + location;

                    count++;

                }

                DataStore.setClassesArray(finalClasses, i);

            } else {

                DataStore.setClassesArray(null, i);

            }

        }

        db.close();

    }

}