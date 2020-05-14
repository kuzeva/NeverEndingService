package com.example.neverendingservice.ServiceRestart;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.neverendingservice.HelperLaunchServiceClass;

public class ServiceBroadcastReceiver extends BroadcastReceiver {


    private static JobScheduler jobScheduler;
    private ServiceBroadcastReceiver serviceBR;
    public static final int JOB_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("BROADCAST_RECEIVER","the timer will start "+ context.toString());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            scheduleJob(context);

        }else{
            registerRestarterReceiver(context);
            HelperLaunchServiceClass.launchService(context);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void scheduleJob(Context context) {
        if(jobScheduler == null){
            jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        }
        ComponentName componentName = new ComponentName(context, JobScheduleService.class);
        JobInfo jobInfo = new JobInfo.Builder(JOB_ID,componentName)
                .setOverrideDeadline(0)
                .setPersisted(true)
                .build();
        jobScheduler.schedule(jobInfo);
    }

    private void registerRestarterReceiver(final Context context) {

        if (serviceBR == null)
            serviceBR = new ServiceBroadcastReceiver();
        else try{
            context.unregisterReceiver(serviceBR);
        } catch (Exception e){
            // not registered
        }
        // give the time to run
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                IntentFilter filter = new IntentFilter();
                filter.addAction("com.example.neverendingservice");
                try {
                    context.registerReceiver(serviceBR, filter);
                } catch (Exception e) {
                    try {
                        context.getApplicationContext().registerReceiver(serviceBR, filter);
                    } catch (Exception ex) {

                    }
                }
            }
        }, 1000);

    }


}

