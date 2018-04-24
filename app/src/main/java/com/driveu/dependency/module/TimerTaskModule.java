package com.driveu.dependency.module;

import com.driveu.api.DriveUApi;
import com.driveu.dependency.scope.DriveUApplicationScope;
import com.driveu.service.DriveUBackgroundService;

import java.util.TimerTask;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Aishwarya on 4/23/2018.
 */
@Module (includes = DriveUApiModule.class)
public class TimerTaskModule {

    private DriveUBackgroundService service;

    public TimerTaskModule(DriveUBackgroundService service) {
        this.service = service;
    }

    @Provides
    @DriveUApplicationScope
    public TimerTask timerTask(final DriveUApi driveUApi) {
        return new TimerTask() {
            @Override
            public void run() {
                driveUApi.getNextLocation(service);
            }
        };
    }
}
