package com.example.neverendingservice;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;

import com.example.neverendingservice.Notification.ServiceNotification;

import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    protected static final int NOT_ID = 150;
    private static String TAG = "Service";

    private static Service currentService;
    private int time_counter = 0 ;
    private static Timer timer;
    private TimerTask timerTask;


    public MyService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            restartForeground();
        }
        currentService = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        Log.d(TAG , "Restarting service  ");

        if (intent == null){
            HelperLaunchServiceClass.launchService(this);
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            restartForeground();
        }

        startTimer();

        return START_STICKY;
    }

    private void startTimer() {
        Log.i(TAG , "Starting timer");

        if(timer != null){
            timer.cancel();
            timer = null;
        }

        timer = new Timer();

        initializeTimerTask();

        Log.i("SERVICE", "Scheduling...");
        timer.schedule(timerTask, 1000, 1000);
    }

    private void initializeTimerTask() {
        Log.i("SERVICE", "initialising TimerTask");
        timerTask = new TimerTask() {
            public void run() {
                Log.i("in timer", "in timer ++++  " + (time_counter++));
            }
        };
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        Log.i(TAG , "onTaskRemoved called");

        Intent broadcastIntent = new Intent("com.example.neverendingservice");
        sendBroadcast(broadcastIntent);
    }

    private void restartForeground() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            try {
                ServiceNotification notification = new ServiceNotification();
                startForeground(NOT_ID,notification.setNotification(this, "Service notification", "This is the service's notification",R.drawable.ic_android));
                Log.i(TAG,"Restarting foreground successful");
                startTimer();
            }catch (Exception e){
                Log.e(TAG , "Error in notification " + e.getMessage());
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"Service destroyed");

        Intent broadcastIntent = new Intent("com.example.neverendingservice");
        sendBroadcast(broadcastIntent);

        if(timer != null){
            timer.cancel();
            timer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
         return null;
    }
}
