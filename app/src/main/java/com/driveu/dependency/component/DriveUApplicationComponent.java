package com.driveu.dependency.component;

import com.driveu.DriveUApplication;
import com.driveu.dependency.module.EventBusModule;
import com.driveu.dependency.module.PreferencesManagerModule;
import com.driveu.manager.PreferencesManager;
import com.driveu.dependency.scope.DriveUApplicationScope;
import com.driveu.service.DriveUBackgroundService;

import org.greenrobot.eventbus.EventBus;

import dagger.Component;

/**
 * Created by Aishwarya on 4/23/2018.
 */
@DriveUApplicationScope
@Component (modules = {EventBusModule.class, PreferencesManagerModule.class})
public interface DriveUApplicationComponent {

    void injectDriveUApplication(DriveUApplication driveUApplication);
}
