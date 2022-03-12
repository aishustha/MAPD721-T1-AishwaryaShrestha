package com.as.bot;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

public class NotificationDecorator {
    private static final String TAG = "NotificationDecorator";
    public static final String CHANNEL_ID = "Chat Bot";
    private final Context context;
    private final NotificationManager notificationManager;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public NotificationDecorator(Context context, NotificationManager notificationManager) {
        this.context = context;
        this.notificationManager = notificationManager;
        createChannel(notificationManager, CHANNEL_ID);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel(NotificationManager notificationManager, String channelId){
        NotificationChannel notificationChannel = new NotificationChannel(channelId, "Chat Bot",
                NotificationManager.IMPORTANCE_LOW);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.GREEN);
        notificationChannel.enableVibration(true);
        notificationManager.createNotificationChannel(notificationChannel);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void displaySimpleNotification(String title, String contentText) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // notification message
        try {
            Notification notify = new Notification.Builder(context)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle(title)
                    .setContentText(contentText)
                    .setContentIntent(contentIntent)
                    .setAutoCancel(true)
                    .setChannelId(CHANNEL_ID)
                    .build();

            notify.flags |= Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify(0, notify);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
