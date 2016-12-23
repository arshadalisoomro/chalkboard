package com.ghofrani.classapp.module;

import android.app.Application;
import android.content.Intent;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.ghofrani.classapp.service.Background;

import net.danlew.android.joda.JodaTimeAndroid;

import okhttp3.OkHttpClient;

public class Chalkboard extends Application {

    @Override
    public void onCreate() {

        super.onCreate();

        Stetho.initializeWithDefaults(this);

        new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        JodaTimeAndroid.init(this);

        DataSingleton.initInstance();
        startService(new Intent(this, Background.class));

    }

}