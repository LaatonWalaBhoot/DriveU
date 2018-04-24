package com.driveu.api;

import android.support.annotation.NonNull;
import android.util.Log;

import com.driveu.dependency.component.DaggerDriveUApiComponent;
import com.driveu.dependency.component.DriveUApiComponent;
import com.driveu.object.Location;
import java.io.IOException;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * Created by Aishwarya on 4/19/2018.
 */

public class DriveUApi {

    /************************************
     * PRIVATE STATIC FIELDS
     ************************************/
    private static volatile DriveUApi instance;

    /************************************
     * PRIVATE FIELDS
     ************************************/
    @Inject
    Retrofit retrofit;

    private DriveUApiInterface service;
    private DriveUApiComponent component;

    /************************************
     * PRIVATE METHODS
     ************************************/
    private DriveUApi() {
    }

    /************************************
     * PUBLIC STATIC METHODS
     ************************************/
    public static DriveUApi getInstance() {
        if (instance == null) {
            synchronized (DriveUApi.class) {
                if (instance == null) {
                    instance = new DriveUApi();
                    instance.init();
                }
            }
        }
        return instance;
    }

    public void getNextLocation(@NonNull final NextLocationListener nextLocationListener) {
        service.getNextLocation().enqueue(new Callback<Location>() {
            @Override
            public void onResponse(Call<Location> call, retrofit2.Response<Location> response) {
                if (response.errorBody() != null) {
                    try {
                        nextLocationListener.onNextLocationFailure(new NextLocationException(response.errorBody().string()));
                    } catch (IOException e) {
                        nextLocationListener.onNextLocationFailure(new NextLocationException("IOException when parsing error status"));
                        Log.d("MESSAGE: ", "onResponse - IOException - " + Log.getStackTraceString(e));
                        e.printStackTrace();
                    }
                    return;
                }
                nextLocationListener.onNextLocationSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Location> call, Throwable t) {
                nextLocationListener.onNextLocationFailure(new NextLocationException(t.getMessage()));
            }
        });
    }

    /************************************
     * PUBLIC INTERFACES
     ************************************/

    public interface NextLocationListener {

        void onNextLocationSuccess(Location location);

        void onNextLocationFailure(Throwable t);
    }

    /************************************
     * EXCEPTIONS
     ************************************/
    public static class NextLocationException extends Exception {
        private NextLocationException(String response) {
            super(response);
        }
    }

    /************************************
     * PUBLIC METHODS
     ************************************/
    private void init() {

        component = DaggerDriveUApiComponent.builder().build();
        component.injectDriveUApi(this);
        service = retrofit.create(DriveUApiInterface.class);
    }
}
