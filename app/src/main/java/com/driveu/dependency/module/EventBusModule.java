package com.driveu.dependency.module;

import com.driveu.dependency.scope.DriveUApplicationScope;

import org.greenrobot.eventbus.EventBus;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Aishwarya on 4/23/2018.
 */
@Module
public class EventBusModule {

    @Provides
    @DriveUApplicationScope
    public EventBus eventBus() {
        return EventBus.getDefault();
    }
}
