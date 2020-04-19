package com.example.neverendingservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import com.example.neverendingservice.ServiceRestart.ServiceBroadcastReceiver;

public class MainActivity extends AppCompatActivity {
    Context mainContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainContext = this;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ServiceBroadcastReceiver.scheduleJob(getApplicationContext());
        } else {

            HelperLaunchServiceClass.launchService(getApplicationContext());
        }
        finish();
    }
}
