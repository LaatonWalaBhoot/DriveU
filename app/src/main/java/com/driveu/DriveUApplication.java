package com.driveu;

import android.app.Activity;
import android.app.Application;
import android.app.Service;

import com.driveu.dependency.component.DaggerDriveUApplicationComponent;
import com.driveu.dependency.component.DriveUApplicationComponent;
import com.driveu.manager.PreferencesManager;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

/**
 * Created by Aishwarya on 4/23/2018.
 */

public class DriveUApplication extends Application {

    @Inject
    EventBus eventBus;

    @Inject
    PreferencesManager preferencesManager;

    public static DriveUApplication getForService(Service service) {
        return (DriveUApplication) service.getApplication();
    }

    public static DriveUApplication getForActivity(Activity activity) {
        return (DriveUApplication) activity.getApplication();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DriveUApplicationComponent component = DaggerDriveUApplicationComponent.builder().build();
        component.injectDriveUApplication(this);
    }

    public PreferencesManager getPreferencesManager() {
        return preferencesManager;
    }

    public EventBus getEventBus() {
        return eventBus;
    }
}
