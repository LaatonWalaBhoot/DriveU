package com.driveu.dependency.module;

import com.driveu.manager.PreferencesManager;
import com.driveu.dependency.scope.DriveUApplicationScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Aishwarya on 4/23/2018.
 */
@Module (includes = ContextModule.class)
public class PreferencesManagerModule {

    @Provides
    @DriveUApplicationScope
    public PreferencesManager preferencesManager() {
        return PreferencesManager.getInstance();
    }
}
