package com.tokko.recipesv2.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class GcmReceiver extends BroadcastReceiver {
    public GcmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Notification.Builder b = new Notification.Builder(context);
        b.setSmallIcon(android.R.drawable.ic_media_play);
        b.setContentText(intent.getStringExtra("message"));
        b.setContentTitle(intent.getStringExtra("message"));
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(0, b.build());
    }
}
