package com.driveu.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.driveu.config.Constants;
import com.driveu.manager.PreferencesManager;
import com.driveu.model.Location;
import com.driveu.service.DriveUBackgroundService;
import com.driveu.R;
import com.driveu.event.LocationUpdateEvent;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback{

    @BindView(R.id.poll_location_tab)
    FloatingActionButton mFabButton;

    private Intent intent;
    private GoogleMap mMap;
    private int pollingState = 0;
    private LocationManager locationManager;
    private android.location.Location myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        intent = new Intent(MapsActivity.this,DriveUBackgroundService.class);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getStartLocation();
    }

    @OnClick(R.id.poll_location_tab)
    public void togglePolling() {
        if (pollingState == PollingState.STATE_STOPPED) {
            pollingState = PollingState.STATE_STARTED;
            mFabButton.setImageResource(R.drawable.ic_stop);
            startService(intent);

        } else if (pollingState == PollingState.STATE_STARTED) {
            pollingState = PollingState.STATE_STOPPED;
            mFabButton.setImageResource(R.drawable.ic_start);
            stopService(intent);
        }
    }

    /************************************
     * PRIVATE METHODS
     ************************************/

    private void getStartLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (myLocation==null) {
            //If Location not provided, default location is set to Sydney.

            LatLng currentLocation = new LatLng(-33.852, 151.211);
            mMap.addMarker(new MarkerOptions().position(currentLocation).title("Default Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        }
        else {
            LatLng currentLocation = new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(currentLocation).title("Your Current Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateFields(LocationUpdateEvent locationUpdateEvent) {
        LatLng latLng = new LatLng(locationUpdateEvent.getUpdatedLocation().getLatitude(), locationUpdateEvent.getUpdatedLocation().getLongitude());
        mMap.addMarker(new MarkerOptions().position(latLng).title("Next Position"));
        CameraPosition cameraPosition = CameraPosition.builder().zoom(10.0f).target(latLng).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        PreferencesManager.getInstance().putState(getApplicationContext(),Constants.APP_STATE,0);

    }

    @Override
    protected void onDestroy() {
        PreferencesManager.getInstance().clearData(getApplicationContext());
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!PreferencesManager.getInstance().getValue(getApplicationContext(), Constants.LOCATION).equals("DEFAULT")) {
            String fields = PreferencesManager.getInstance().getValue(getApplicationContext(),Constants.LOCATION);
            onUpdateFields(new LocationUpdateEvent(new Gson().fromJson(fields,Location.class)));
            PreferencesManager.getInstance().removeData(getApplicationContext(),Constants.LOCATION);
        }
        EventBus.getDefault().register(this);
        PreferencesManager.getInstance().putState(getApplicationContext(),Constants.APP_STATE,1);
    }

    /************************************
     * PUBLIC INTERFACES
     ************************************/
    public interface PollingState {

        int STATE_STARTED = 1;

        int STATE_STOPPED = 0;
    }
}
