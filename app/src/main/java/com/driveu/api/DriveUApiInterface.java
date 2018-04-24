package com.driveu.api;

import com.driveu.object.Location;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Aishwarya on 4/19/2018.
 */

public interface DriveUApiInterface  {

    /************************************
     * API METHODS
     ************************************/

    @GET("/")
    Call<Location> getNextLocation();
}
