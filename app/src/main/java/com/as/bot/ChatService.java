package com.as.bot;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

public class ChatService extends Service {
    private PowerManager.WakeLock wakeLock;
    private NotificationDecorator notificationDecorator;
    private NotificationManager notificationManager;
    private static final String TAG = "ChatService";
    public static final String CMD = "message_command";
    public static final int CHAT_GENERATE_MESSAGE = 15;
    public static final int CHAT_STOP_SERVICE = 30;


    @Override
    public void onCreate() {
        Log.v(TAG, "onCreate()");
        super.onCreate();

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationDecorator = new NotificationDecorator(this, notificationManager);

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand()");
        super.onStartCommand(intent, flags, startId);
        if (intent != null) {
            Bundle data = intent.getExtras();
            handleData(data);
            if (!wakeLock.isHeld()) {
                Log.v(TAG, "acquiring wake lock");
                wakeLock.acquire();
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy()");
        Log.v(TAG, "Releasing wake lock");
        wakeLock.release();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int getResponseCode() {
        return 0;
    }


    private void handleData(Bundle data) {
        int command = data.getInt(CMD);
        Log.d(TAG, "-(<- received command data to service: command=" + command);
        //ChatBotService handle the command Generate Message
        if (command == CHAT_GENERATE_MESSAGE) {
            //sending notification to the user with a message
            notificationDecorator.displaySimpleNotification("Generate Message", "Generated a sample Greeting Message");
            sendBroadcastGenerateMessage();

            //ChatBotService handle the command Stop Service
        } else if (command == CHAT_STOP_SERVICE) {
            //sending notification to the user with a message
            notificationDecorator.displaySimpleNotification("Service Stopped: 62", "Service has been stopped with code : 62");
            sendBroadcastStopService();
        } else {
            Log.w(TAG, "Ignoring Unknown Command! id=" + command);
        }
    }

    //Broadcast generate message
    private void sendBroadcastGenerateMessage(){
        Log.d(TAG, "->(+)<- sending broadcast: BROADCAST_GENERATE_MESSAGE ");
        Intent intent = new Intent();
        intent.setAction(Constants.BROADCAST_GENERATE_MESSAGE);
        sendBroadcast(intent);
    }

    //Broadcast stop service
    private void sendBroadcastStopService(){
        Log.d(TAG, "->(+)<- sending broadcast: BROADCAST_STOP_SERVICE ");
        Intent intent = new Intent();
        intent.setAction(Constants.BROADCAST_STOP_SERVICE);
        sendBroadcast(intent);
    }
}
