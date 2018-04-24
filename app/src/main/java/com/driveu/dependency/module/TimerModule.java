package com.driveu.dependency.module;

import com.driveu.dependency.scope.DriveUApplicationScope;

import java.util.Timer;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Aishwarya on 4/23/2018.
 */
@Module
public class TimerModule {

    @Provides
    @DriveUApplicationScope
    public Timer timer() {
        return new Timer();
    }
}
