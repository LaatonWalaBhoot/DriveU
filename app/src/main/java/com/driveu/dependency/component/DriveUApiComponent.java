package com.driveu.dependency.component;

import com.driveu.api.DriveUApi;
import com.driveu.dependency.module.GsonModule;
import com.driveu.dependency.module.HttpLoggingInterceptorModule;
import com.driveu.dependency.module.OkHttpClientModule;
import com.driveu.dependency.module.RetrofitModule;
import com.driveu.dependency.scope.DriveUApplicationScope;

import dagger.Component;

/**
 * Created by Aishwarya on 4/23/2018.
 */

@Component (modules = {RetrofitModule.class, OkHttpClientModule.class, GsonModule.class, HttpLoggingInterceptorModule.class})
@DriveUApplicationScope
public interface DriveUApiComponent {

    void injectDriveUApi(DriveUApi driveUApi);
}
