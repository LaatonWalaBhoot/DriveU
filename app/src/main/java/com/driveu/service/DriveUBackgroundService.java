package com.driveu.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.driveu.R;
import com.driveu.activity.MapsActivity;
import com.driveu.api.DriveUApi;
import com.driveu.config.Constants;
import com.driveu.event.LocationUpdateEvent;
import com.driveu.manager.PreferencesManager;
import com.driveu.model.Location;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Aishwarya on 4/19/2018.
 */

public class DriveUBackgroundService extends Service implements DriveUApi.NextLocationListener{

    private Timer timer;
    private static final int NOTIFICATION_ID = 11;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        timer = new Timer();
        if(intent==null) {
            return START_NOT_STICKY;
        }
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                DriveUApi.getInstance().getNextLocation(DriveUBackgroundService.this);
            }
        }, 0, 15000);
        runAsForeground();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        stopForeground(true);
    }

    @Override
    public void onNextLocationSuccess(Location location) {
        if(PreferencesManager.getInstance().getState(getApplicationContext(),Constants.APP_STATE)==AppState.RESUMED) {
            EventBus.getDefault().postSticky(new LocationUpdateEvent(location));
        }
        else {
            PreferencesManager.getInstance().putValue(getApplicationContext(),Constants.LOCATION,new Gson().toJson(location));
        }

    }

    private void runAsForeground() {
        Intent notificationIntent = new Intent(this, MapsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_ONE_SHOT);

        Notification notification = new NotificationCompat.Builder(this,"Channel ID")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText("Working in Background")
                .setContentTitle("DriveU")
                .setContentIntent(pendingIntent).build();

        startForeground(NOTIFICATION_ID, notification);

    }

    @Override
    public void onNextLocationFailure(Throwable t) {
        Log.e("Error",t.getMessage());
    }

    /************************************
     * PUBLIC INTERFACES
     ************************************/
    public interface AppState {

        int RESUMED = 1;

        int PAUSED = 0;
    }
}
