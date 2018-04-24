package com.driveu.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.driveu.DriveUApplication;
import com.driveu.dependency.component.DaggerDriveUBackgroundServiceComponent;
import com.driveu.dependency.component.DriveUBackgroundServiceComponent;
import com.driveu.dependency.module.ContextModule;
import com.driveu.dependency.module.TimerTaskModule;
import com.driveu.api.DriveUApi;
import com.driveu.config.Constants;
import com.driveu.event.LocationUpdateEvent;
import com.driveu.manager.PreferencesManager;
import com.driveu.object.Location;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

/**
 * Created by Aishwarya on 4/19/2018.
 */

public class DriveUBackgroundService extends Service implements DriveUApi.NextLocationListener{

    private static final int NOTIFICATION_ID = 11;

    @Inject
    Timer timer;

    @Inject
    Notification notification;

    @Inject
    TimerTask timerTask;

    @Inject
    Gson gson;

    private EventBus eventBus;
    private PreferencesManager preferencesManager;
    private DriveUBackgroundServiceComponent component;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        eventBus = DriveUApplication.getForService(this).getEventBus();
        preferencesManager = DriveUApplication.getForService(this).getPreferencesManager();

        component = DaggerDriveUBackgroundServiceComponent.builder()
                .contextModule(new ContextModule(this))
                .timerTaskModule(new TimerTaskModule(this))
                .build();

        component.injectDriveUBackgroundService(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent==null) {
            return START_NOT_STICKY;
        }
        timer.scheduleAtFixedRate(timerTask, 0, 15000);
        startForeground(NOTIFICATION_ID, notification);
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
        if(preferencesManager.getState(getApplicationContext(),Constants.APP_STATE)==AppState.RESUMED) {
            eventBus.postSticky(new LocationUpdateEvent(location));
        }
        else {
            preferencesManager.putValue(getApplicationContext(),Constants.LOCATION,gson.toJson(location));
        }

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
