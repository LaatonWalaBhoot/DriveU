package com.driveu.dependency.module;

import com.driveu.dependency.scope.MapsActivityScope;
import com.google.android.gms.maps.model.MarkerOptions;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Aishwarya on 4/23/2018.
 */
@Module
public class MarkerOptionsModule {

    @Provides
    @MapsActivityScope
    public MarkerOptions markerOptions() {
        return  new MarkerOptions();
    }
}
