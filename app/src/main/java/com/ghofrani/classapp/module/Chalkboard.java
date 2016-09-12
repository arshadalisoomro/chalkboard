package com.ghofrani.classapp.module;

import android.app.Application;
import android.content.Intent;

import com.ghofrani.classapp.service.Background;

import net.danlew.android.joda.JodaTimeAndroid;

public class Chalkboard extends Application {

    @Override
    public void onCreate() {

        super.onCreate();

        DataSingleton.initInstance();

        JodaTimeAndroid.init(this);
        startService(new Intent(this, Background.class));

    }

}