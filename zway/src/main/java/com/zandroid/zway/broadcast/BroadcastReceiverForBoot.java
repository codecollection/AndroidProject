package com.zandroid.zway.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zandroid.zway.MyApplication;
import com.zandroid.zway.MyConfig;

public class BroadcastReceiverForBoot extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        MyConfig.log("BroadcastReceiverForBoot--onReceive");

        startAll(context);
    }

    private void startAll(Context context) {
        MyApplication application = (MyApplication) context.getApplicationContext();
        if (application != null) {
            application.startAll();
        }
    }
}
