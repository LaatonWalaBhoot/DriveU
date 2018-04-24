package com.driveu.dependency.module;

import android.content.Context;
import android.location.LocationManager;

import com.driveu.dependency.scope.MapsActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Aishwarya on 4/23/2018.
 */
@Module (includes = ContextModule.class)
public class LocationManagerModule {

    @Provides
    @MapsActivityScope
    public LocationManager locationManager(Context context) {
        return (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
    }
}
