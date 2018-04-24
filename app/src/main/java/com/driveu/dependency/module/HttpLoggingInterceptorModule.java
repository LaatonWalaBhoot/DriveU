package com.driveu.dependency.module;

import com.driveu.dependency.scope.DriveUApplicationScope;

import dagger.Module;
import dagger.Provides;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Aishwarya on 4/23/2018.
 */

@Module
public class HttpLoggingInterceptorModule {

    @Provides
    @DriveUApplicationScope
    public HttpLoggingInterceptor httpLoggingInterceptor() {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }
}
