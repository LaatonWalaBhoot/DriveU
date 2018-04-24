package com.driveu.dependency.module;

import com.driveu.dependency.scope.MapsActivityScope;
import com.driveu.model.PresenterImpl;
import com.driveu.view.DriveUView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Aishwarya on 4/24/2018.
 */

@Module
public class PresenterImplModule {

    private DriveUView driveUView;

    public PresenterImplModule(DriveUView driveUView) {
        this.driveUView = driveUView;
    }

    @Provides
    @MapsActivityScope
    public PresenterImpl presenterImpl() {
        return new PresenterImpl(driveUView);
    }
}
