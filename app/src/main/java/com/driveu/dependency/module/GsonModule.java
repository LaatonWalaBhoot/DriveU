package com.driveu.dependency.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Aishwarya on 4/23/2018.
 */

@Module
public class GsonModule {

    @Provides
    public Gson gson() {
        return new GsonBuilder().create();
    }
}
