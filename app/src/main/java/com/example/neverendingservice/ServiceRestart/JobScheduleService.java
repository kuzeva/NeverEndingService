package com.example.neverendingservice.ServiceRestart;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.neverendingservice.HelperLaunchServiceClass;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JobScheduleService extends JobService {

    private static String TAG = "JOB_SERVICE";
    public static ServiceBroadcastReceiver serviceBR;
    private static JobService jobService;
    private static JobParameters jobParameters;

    @Override
    public boolean onStartJob(JobParameters params) {

        HelperLaunchServiceClass.launchService(this);
        registerReceiverRestarter();
        jobService = this;
        JobScheduleService.jobParameters = params;
        return false;
    }

    private void registerReceiverRestarter() {

        if (serviceBR == null)
            serviceBR = new ServiceBroadcastReceiver();
        else try{
            unregisterReceiver(serviceBR);
        } catch (Exception e){
            // not registered
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                IntentFilter filter = new IntentFilter();
                filter.addAction("com.example.neverendingservice");
                try {
                    registerReceiver(serviceBR, filter);
                } catch (Exception e) {
                    try {
                        getApplicationContext().registerReceiver(serviceBR, filter);
                    } catch (Exception ex) {

                    }
                }
            }
        }, 1000);

    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i(TAG,"Stopping job");
        Intent broadcastIntent = new Intent("com.example.neverendingservice");
        sendBroadcast(broadcastIntent);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                unregisterReceiver(serviceBR);
            }
        }, 1000);
        return false;
    }
}