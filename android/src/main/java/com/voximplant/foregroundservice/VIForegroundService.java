/*
 * Copyright (c) 2011-2019, Zingaya, Inc. All rights reserved.
 */

package com.voximplant.foregroundservice;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static com.voximplant.foregroundservice.Constants.NOTIFICATION_CONFIG;

public class VIForegroundService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent = new Intent(Constants.FOREGROUND_SERVICE_RECEIVER);
        intent.putExtra(Constants.LIFE_CYCLE_ACTION, LifecycleType.ON_CREATED);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(Constants.FOREGROUND_SERVICE_RECEIVER);
        intent.putExtra(Constants.LIFE_CYCLE_ACTION, LifecycleType.ON_DESTROYED);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (action != null) {
            if (action.equals(Constants.ACTION_FOREGROUND_SERVICE_START)) {
                if (intent.getExtras() != null && intent.getExtras().containsKey(NOTIFICATION_CONFIG)) {
                    Bundle notificationConfig = intent.getExtras().getBundle(NOTIFICATION_CONFIG);
                    if (notificationConfig != null && notificationConfig.containsKey("id")) {
                        Notification notification = NotificationHelper.getInstance(getApplicationContext())
                                .buildNotification(getApplicationContext(), notificationConfig);

                        startForeground((int)notificationConfig.getDouble("id"), notification);
                    }
                }
            } else if (action.equals(Constants.ACTION_FOREGROUND_SERVICE_STOP)) {
                stopSelf();
            }
        }
        return START_NOT_STICKY;

    }
}