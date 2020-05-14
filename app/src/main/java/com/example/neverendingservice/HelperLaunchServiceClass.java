package com.example.neverendingservice;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class HelperLaunchServiceClass {

    private static String TAG ="HELPER_CLASS";



    public HelperLaunchServiceClass() {
    }


    public static void launchService(Context context) {
        if (context == null) {
            return;
        }
        Intent serviceIntent = new Intent(context,MyService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent);
        } else {
            context.startService(serviceIntent);
        }
        Log.d(TAG, "From HelperLaunchServiceClass : Service can start!");
    }
}

