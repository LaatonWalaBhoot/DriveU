package com.driveu.model;

import android.location.Location;

import com.driveu.presenter.Presenter;
import com.driveu.view.DriveUView;

/**
 * Created by Aishwarya on 4/24/2018.
 */

public class PresenterImpl implements Presenter {

    private DriveUView driveUView;

    public PresenterImpl(DriveUView driveUView) {
        this.driveUView = driveUView;
    }

    @Override
    public void getStartLocation(Location location) {
        if(location==null) {
            driveUView.onStartLocationSuccess();
        }
        else {
            driveUView.onStartLocationFail();
        }
    }

    @Override
    public void togglePolling(int pollingState) {
        if(pollingState == PollingState.STATE_STARTED) {
            driveUView.onStopPolling();
        }
        else if(pollingState == PollingState.STATE_STOPPED){
            driveUView.onStartPolling();
        }
    }

    public interface PollingState {

        int STATE_STARTED = 1;

        int STATE_STOPPED = 0;
    }
}
