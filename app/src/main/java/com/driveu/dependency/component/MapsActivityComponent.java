package com.driveu.dependency.component;

import com.driveu.activity.MapsActivity;
import com.driveu.dependency.module.ContextModule;
import com.driveu.dependency.module.GsonModule;
import com.driveu.dependency.module.LocationManagerModule;
import com.driveu.dependency.module.MarkerOptionsModule;
import com.driveu.dependency.module.PresenterImplModule;
import com.driveu.dependency.scope.MapsActivityScope;
import com.driveu.model.PresenterImpl;

import dagger.Component;

/**
 * Created by Aishwarya on 4/23/2018.
 */

@MapsActivityScope
@Component (modules = {MarkerOptionsModule.class, LocationManagerModule.class, GsonModule.class, PresenterImplModule.class, ContextModule.class})
public interface MapsActivityComponent {

    void injectMapsActivity(MapsActivity mapsActivity);

    PresenterImpl getPresenterImpl();
}
