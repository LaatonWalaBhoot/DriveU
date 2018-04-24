package com.driveu.view;

/**
 * Created by Aishwarya on 4/24/2018.
 */

public interface DriveUView {

    void onStartLocationSuccess();

    void onStartLocationFail();

    void onStartPolling();

    void onStopPolling();
}
