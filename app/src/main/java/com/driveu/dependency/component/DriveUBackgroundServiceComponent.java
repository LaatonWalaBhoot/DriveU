package com.driveu.dependency.component;

import android.app.Notification;

import com.driveu.dependency.module.GsonModule;
import com.driveu.dependency.module.NotificationModule;
import com.driveu.dependency.module.TimerModule;
import com.driveu.dependency.module.TimerTaskModule;
import com.driveu.dependency.scope.DriveUApplicationScope;
import com.driveu.service.DriveUBackgroundService;
import com.google.gson.Gson;

import java.util.Timer;
import java.util.TimerTask;

import dagger.Component;

/**
 * Created by Aishwarya on 4/23/2018.
 */

@DriveUApplicationScope
@Component (modules = {TimerModule.class, NotificationModule.class, TimerTaskModule.class, GsonModule.class})
public interface DriveUBackgroundServiceComponent {

    void injectDriveUBackgroundService(DriveUBackgroundService driveUBackgroundService);
}
