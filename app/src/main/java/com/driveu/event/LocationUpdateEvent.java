package com.driveu.event;

import com.driveu.object.Location;

/**
 * Created by Aishwarya on 4/19/2018.
 */

public class LocationUpdateEvent {

    private Location location;

    public LocationUpdateEvent(Location location) {
        this.location = location;
    }

    public Location getUpdatedLocation() {
        return location;
    }
}
