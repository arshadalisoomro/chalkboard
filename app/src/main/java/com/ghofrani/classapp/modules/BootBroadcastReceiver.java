package com.ghofrani.classapp.modules;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ghofrani.classapp.service.TimeService;

public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        context.startService(new Intent(context, TimeService.class));

    }

}