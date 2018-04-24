package com.driveu.dependency.module;

import com.driveu.api.DriveUApi;
import com.driveu.dependency.scope.DriveUApplicationScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Aishwarya on 4/23/2018.
 */
@Module
public class DriveUApiModule {

    @Provides
    @DriveUApplicationScope
    public DriveUApi driveUApi() {
        return DriveUApi.getInstance();
    }
}
