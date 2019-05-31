package com.example.marcus.mappy;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private Model model;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private Context context;
    private String chosenLocalName;
    private Timer timer;
    private Timer timer2;
    private boolean doOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doOnce = true;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            chosenLocalName = extras.getString("login_variable_name");
        }

        context = super.getApplicationContext();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        setContentView(R.layout.activity_maps);
        createLocationRequest();
        timer = new Timer();
        timer2 = new Timer();

    }


    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mGoogleApiClient.isConnected())
            startLocationUpdates();
    }

    @Override
    protected void onPause() {
        Log.d("lifecycle","onPause");
        super.onPause();
        timer.cancel();
        timer2.cancel();
        stopLocationUpdates();

        if(model != null) {
            model.saveUserInfo();
        }

    }

    @Override
    protected void onStop() {
        Log.d("lifecycle", "onStop");
        mGoogleApiClient.disconnect();
        super.onStop();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        Log.d("location", "yo" + mLastLocation);
        if(chosenLocalName != null)
            model = new Model(mLastLocation, context, chosenLocalName);
        else
            model = new Model(mLastLocation, context);

        model.init(mMap);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                model.mapClicked(mMap, latLng);
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                model.markerClicked(mMap, marker);
                setUpDialog(marker);
                return false;
            }
        });

        if(doOnce){
            doOnce = false;
            syncTaskSendLocation(); //announce your location
            syncFriendsLocation(); //get friends location
        }

    }

    private void setUpDialog(Marker marker){
        String markerinfo = model.getMarkerInfo(marker);
        final MarkDialog d = new MarkDialog(MapsActivity.this, model.getUser().getName() + ": " + markerinfo);

        d.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                model.modifyMarkerDescription(d.getDescChanges());
            }
        });
        d.show();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("location","onConnected");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        startLocationUpdates();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button button = (Button) findViewById(R.id.buttonfrag);

        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {

                Fragment frag = FriendFrag.newInstance(model);

                getFragmentManager().beginTransaction()
                        .replace(android.R.id.content, frag)
                        .addToBackStack("settings")
                        .commit();
            }
        });

//        Log.d("location", "Yo " + mLastLocation.toString());
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("location", "failed to connect to Google services");
    }

    protected  void startLocationUpdates()
    {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }

    protected void stopLocationUpdates()
    {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    protected void createLocationRequest()
    {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(20000);
        mLocationRequest.setFastestInterval(10000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if(mLastLocation != null && mMap != null){
            model.updateMap(mMap, mLastLocation);

        }

    }

    /*kod inspirerad av stackoverflow*/
    public void syncTaskSendLocation() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doSendSyncTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            InternetManager backTask = new InternetManager(model);
                            backTask.execute("sendLocation");
                        } catch (Exception e) {
                        }
                    }
                });
            }
        };
        timer.schedule(doSendSyncTask, 0, 4000); //execute in every 4000 ms
    }

    /*kod inspirerad av stackoverflow*/ //uppdatera data med vännernas position
    public void syncFriendsLocation() {
        final Handler handler = new Handler();
        TimerTask doSyncFriends = new TimerTask() {
            @Override
            public void run() {
                final InternetManager backTask = new InternetManager(model);
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            backTask.execute("getLocation");
                        } catch (Exception e) {
                        }
                    }
                });

            }
        };
        timer2.schedule(doSyncFriends, 0, 4000); //execute in every 4000 ms
    }

}
