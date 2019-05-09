package com.gigfindr.admin.app;

import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreenActivity extends AppCompatActivity {

    private Class intentclass;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen); //This Your Layout For Customize

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        Window window = getWindow();
        window.setStatusBarColor(Color.BLACK);

        checkLocationPermission();

        //Declare Animation
        ImageView imageView = findViewById(R.id.imageview_logo); //Declare ImageView
        //set Animation to ImageView
        Glide.with(this).load(R.drawable.gigfindericonblack).into(imageView);

        //Create Thread for timing to LoginActivity
        Thread loadingsplash = new Thread(){
            public void run(){
                try{
                    sleep(2000); //set sleep 2 second
                }catch (Exception e){
                    e.printStackTrace(); //print if this thread is error
                }finally {
                    Intent intent = new Intent(getApplicationContext(), intentclass); //set Intent
                    startActivity(intent); //to start change activity
                }
            }
        };
        loadingsplash.start(); //run this Thread
    }

    //check for permission
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            intentclass = LocationCheckerActivity.class;

        } else{

            displayLocationSettingsRequest(SplashScreenActivity.this);
        }
    }


    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @SuppressLint("MissingPermission")
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:

                        if (firebaseAuth.getCurrentUser() == null) {
                            intentclass = MapActivity.class;
                        }
                        else{
                            intentclass = DecideActivity.class;
                        }
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        intentclass = LocationCheckerActivity.class;
                        break;

                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
