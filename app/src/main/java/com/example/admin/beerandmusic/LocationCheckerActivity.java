package com.example.admin.beerandmusic;

import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

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

import es.dmoral.toasty.Toasty;

public class LocationCheckerActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_checker);

        ActivityCompat.requestPermissions(this,
                new String[]{permission.ACCESS_FINE_LOCATION},
                2000);

        Window window = getWindow();
        window.setStatusBarColor(Color.BLACK);

        Button button = findViewById(R.id.locationchecker_button);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                checkLocationPermission();

            }
        });
    }

    //check for permission
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(LocationCheckerActivity.this,
                                        new String[]{permission.ACCESS_FINE_LOCATION}, 1000);
                            }
                        })
                        .create()
                        .show();


            } else {

                // No explanation needed, we can request the permission.
                displayLocationSettingsRequest(LocationCheckerActivity.this);
            }
        } else {
            displayLocationSettingsRequest(LocationCheckerActivity.this);
        }
    }

    //this is the check to turn location on
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
                            Intent myIntent = new Intent(LocationCheckerActivity.this, MapActivity.class);
                            LocationCheckerActivity.this.startActivity(myIntent);
                        }
                        else{
                            Intent myIntent = new Intent(LocationCheckerActivity.this, DecideActivity.class);
                            LocationCheckerActivity.this.startActivity(myIntent);
                        }

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(LocationCheckerActivity.this, 2000);

                        } catch (IntentSender.SendIntentException e) {
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        try {
                            status.startResolutionForResult(LocationCheckerActivity.this, 2000);

                        } catch (IntentSender.SendIntentException e) {
                        }
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 2000){

            if (firebaseAuth.getCurrentUser() == null) {
                Intent myIntent = new Intent(LocationCheckerActivity.this, MapActivity.class);
                LocationCheckerActivity.this.startActivity(myIntent);
            }
            else{
                Intent myIntent = new Intent(LocationCheckerActivity.this, DecideActivity.class);
                LocationCheckerActivity.this.startActivity(myIntent);
            }
        }
    }
}
