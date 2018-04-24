package com.driveu.presenter;

import android.location.Location;

/**
 * Created by Aishwarya on 4/24/2018.
 */

public interface Presenter {

    void getStartLocation(Location location);

    void togglePolling(int pollingState);
}
