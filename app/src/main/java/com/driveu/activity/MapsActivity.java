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

import com.driveu.DriveUApplication;
import com.driveu.dependency.component.DaggerMapsActivityComponent;
import com.driveu.dependency.component.MapsActivityComponent;
import com.driveu.config.Constants;
import com.driveu.dependency.module.PresenterImplModule;
import com.driveu.manager.PreferencesManager;
import com.driveu.object.Location;
import com.driveu.dependency.module.ContextModule;
import com.driveu.model.PresenterImpl;
import com.driveu.presenter.Presenter;
import com.driveu.service.DriveUBackgroundService;
import com.driveu.R;
import com.driveu.event.LocationUpdateEvent;
import com.driveu.view.DriveUView;
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

import java.net.ProxySelector;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        DriveUView{

    @BindView(R.id.poll_location_tab)
    FloatingActionButton mFabButton;

    @Inject
    LocationManager locationManager;

    @Inject
    MarkerOptions markerOptions;

    @Inject
    Gson gson;

    private Intent intent;
    private Presenter presenter;
    private GoogleMap mMap;
    private int pollingState = 0;
    private EventBus eventBus;
    private PreferencesManager preferencesManager;
    private android.location.Location myLocation;
    private MapsActivityComponent component;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        component = DaggerMapsActivityComponent.builder()
                .presenterImplModule(new PresenterImplModule(this))
                .contextModule(new ContextModule(getApplicationContext()))
                .build();

        component.injectMapsActivity(this);
        presenter = component.getPresenterImpl();
        eventBus = DriveUApplication.getForActivity(this).getEventBus();
        preferencesManager = DriveUApplication.getForActivity(this).getPreferencesManager();
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
        presenter.togglePolling(pollingState);
    }

    @Override
    public void onStartLocationSuccess() {
        LatLng currentLocation = new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
        mMap.addMarker(markerOptions.position(currentLocation).title("Your Current Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
    }

    @Override
    public void onStartLocationFail() {
        LatLng currentLocation = new LatLng(-33.852, 151.211);
        mMap.addMarker(markerOptions.position(currentLocation).title("Default Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
    }

    @Override
    public void onStartPolling() {
        pollingState = PollingState.STATE_STARTED;
        mFabButton.setImageResource(R.drawable.ic_stop);
        startService(intent);
    }

    @Override
    public void onStopPolling() {
        pollingState = PollingState.STATE_STOPPED;
        mFabButton.setImageResource(R.drawable.ic_start);
        stopService(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        eventBus.unregister(this);
        preferencesManager.putState(getApplicationContext(),Constants.APP_STATE,0);
        if(pollingState == PollingState.STATE_STOPPED) {
            preferencesManager.clearData(this);
        }
    }

    @Override
    protected void onDestroy() {
        preferencesManager.clearData(getApplicationContext());
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!preferencesManager.getValue(getApplicationContext(), Constants.LOCATION).equals("DEFAULT")) {
            String fields = preferencesManager.getValue(getApplicationContext(),Constants.LOCATION);
            onUpdateFields(new LocationUpdateEvent(gson.fromJson(fields,Location.class)));
            preferencesManager.removeData(getApplicationContext(),Constants.LOCATION);
        }
        eventBus.register(this);
        preferencesManager.putState(getApplicationContext(),Constants.APP_STATE,1);
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
        myLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        presenter.getStartLocation(myLocation);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateFields(LocationUpdateEvent locationUpdateEvent) {
        LatLng latLng = new LatLng(locationUpdateEvent.getUpdatedLocation().getLatitude(), locationUpdateEvent.getUpdatedLocation().getLongitude());
        mMap.addMarker(markerOptions.position(latLng).title("Next Position"));
        CameraPosition cameraPosition = CameraPosition.builder().zoom(10.0f).target(latLng).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    /************************************
     * PUBLIC INTERFACES
     ************************************/
    public interface PollingState {

        int STATE_STARTED = 1;

        int STATE_STOPPED = 0;
    }
}
