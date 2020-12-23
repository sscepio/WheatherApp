package com.example.wheatherapp;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class Services extends Service {
    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Activity2.onRefresh();
        return super.onStartCommand(intent, flags, startId);

    }




    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }





    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
