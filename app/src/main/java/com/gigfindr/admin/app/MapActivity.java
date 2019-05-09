package com.gigfindr.admin.app;

import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gigfindr.admin.app.DrawerAdapter.OnItemSelectedListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import es.dmoral.toasty.Toasty;

import static android.os.Build.VERSION_CODES.M;
import static com.gigfindr.admin.app.R.id.map;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,
        ConnectionCallbacks, OnConnectionFailedListener,
        LocationListener, OnClickListener, OnItemSelectedListener, OnTouchListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private ShowDetails showDetails;
    HashMap<String, ShowDetails> showDetailsHashMap = new HashMap<>();

    private String currentLat, currentLong;
    protected Map<Location, String> tempNearbyLocation = new HashMap<>();
    protected Map<Location, String> nearbyLocation = new HashMap<>();
    private ArrayList<Marker> markerArray = new ArrayList<>();
    private Location currentLocation = new Location("currentLocation");

    private int counter = 0;
    private int markercounter = 0;
    private Boolean b = false;

    private String[] screenTitles;
    private Drawable[] screenIcons;

    private DrawerAdapter adapter;

    private RelativeLayout relativeLayout;
    private Button btnGetNearByShow;

    private Bundle savedInstanceState2;

    private Toolbar toolbar;
    private Window window;

    private SupportMapFragment mapFragment;

    private SlidingRootNav slidingRootNav;

    private RelativeLayout relativeLayoutMap;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedInstanceState2 = savedInstanceState;
        setContentView(R.layout.activity_map);

        init();
        checkLocationPermission();

//         Create a Handler instance on the main thread
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(1500);
                } catch (Exception e) {
                } // Just catch the InterruptedException

                // Now we use the Handler to post back to the main thread
                handler.post(new Runnable() {
                    public void run() {
                    // Set the View's visibility back on the main UI Thread
                    relativeLayout.setVisibility(View.INVISIBLE);
                    btnGetNearByShow.setVisibility(View.VISIBLE);
                    }
                });
            }
        }).start();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        //zoom camera to singapore
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(1.290270, 103.851959)));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11.0f));
        mMap.getUiSettings().setMyLocationButtonEnabled(false);


        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
            else{
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    //check for permission
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapActivity.this,
                                        new String[]{permission.ACCESS_FINE_LOCATION}, 1000);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{permission.ACCESS_FINE_LOCATION},
                        1000);
                displayLocationSettingsRequest(MapActivity.this);
            }
        } else {
            displayLocationSettingsRequest(MapActivity.this);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1000: {

                displayLocationSettingsRequest(MapActivity.this);

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);

                    }
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toasty.error(MapActivity.this, "permission denied", Toast.LENGTH_LONG, true).show();
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void read() {
        mDatabase.child("Show").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    showDetails = child.getValue(ShowDetails.class);
                    if (showDetails != null) {

                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                        String todayDate = df.format(c.getTime());

                        String receivedDate = showDetails.getDate();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date DateshowDate = null;
                        Date DatetodayDate = null;

                        try {
                            DatetodayDate = sdf.parse(todayDate);
                            DateshowDate = sdf.parse(receivedDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (DateshowDate.equals(DatetodayDate)) {

                            //today's show
                            final String latlng = showDetails.getLatLng();
                            String lat = latlng.substring(0, latlng.indexOf(","));
                            String lng = latlng.substring(latlng.indexOf(","));
                            String editedlng = lng.replace(",", " ");

                            final Location showlocation = new Location("showLocation");
                            showlocation.setLatitude(Double.parseDouble(lat));
                            showlocation.setLongitude(Double.parseDouble(editedlng));

                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(new LatLng(Double.parseDouble(lat), Double.parseDouble(editedlng)));
                            markerOptions.title("");
                            markerOptions.snippet("");

                            Marker marker = mMap.addMarker(markerOptions);
                            showDetailsHashMap.put(marker.getId(), showDetails);

                            if (!b) {
                                if (currentLocation.distanceTo(showlocation) < 5000) {
                                    String markerId = marker.getId().substring(1);

                                    tempNearbyLocation.put(showlocation, markerId);
                                    markerArray.add(marker);
                                }
                            }


                            mMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter(getLayoutInflater(), showDetailsHashMap));
                            mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
                                @Override
                                public void onInfoWindowClick(Marker marker) {

                                    Intent intent = new Intent(getBaseContext(), ViewDetailedShowActivity.class);
                                    Bundle extras = new Bundle();
                                    extras.putString("name", showDetailsHashMap.get(marker.getId()).getBandName());
                                    extras.putString("date", showDetailsHashMap.get(marker.getId()).getDate());
                                    extras.putString("address", showDetailsHashMap.get(marker.getId()).getAddress());
                                    intent.putExtras(extras);
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                }

                if (!tempNearbyLocation.isEmpty()) {
                    nearbyLocation.putAll(tempNearbyLocation);
                    b = true;
                }

                mDatabase.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(30 * 1000);
        mLocationRequest.setFastestInterval(5 * 1000);

        setLocationListener();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

//        Log.d("onLocationChanged", currentLocation.toString());
//
//        mLastLocation = location;
//        Log.d("mLastLocation", mLastLocation.toString());
//
//        //Place current location
//        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//
//        //move map camera
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
//
//        //stop location updates
//        if (mGoogleApiClient != null) {
//            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
//        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void setLocationListener() {

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                currentLat = String.valueOf(location.getLatitude());
                currentLong = String.valueOf(location.getLongitude());
                currentLocation.setLatitude(Double.parseDouble(currentLat));
                currentLocation.setLongitude(Double.parseDouble(currentLong));


                counter++;

                if (tempNearbyLocation.isEmpty() && counter < 15) {
                    read();
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, locationListener);

    }

    @Override
    public void onClick(View view) {

        if(view == btnGetNearByShow) {

            //sorting and reodering
            Map<Location, String> newReorderedMap = reorderHashMap(nearbyLocation);

            List<Entry<Location, String>> list = new LinkedList<Entry<Location, String>>(newReorderedMap.entrySet());

            Collections.sort(list, new Comparator<Entry<Location, String>>() {
                public int compare(Entry<Location, String> o1,
                                   Entry<Location, String> o2) {
                    return o1.getValue().compareTo(o2.getValue());
                }
            });

            // Maintaining insertion order with the help of LinkedList
            Map<Location, String> sortedNearbyLocationMap = new LinkedHashMap<Location, String>();
            for (Entry<Location, String> entry : list) {
                sortedNearbyLocationMap.put(entry.getKey(), entry.getValue());
            }

            ArrayList<String> markerIDList = getMarkerId(markerArray);

            Iterator it = sortedNearbyLocationMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();

                if (pair.getValue().toString().equals(String.valueOf(markercounter))) {

                    //find the marker in the array with markercounter
                    for (Marker marker : markerArray) {
                        if (marker.getId().equalsIgnoreCase(markerIDList.get(markercounter))) {
                            marker.showInfoWindow();

                            String splitll = marker.getPosition().toString().replace("lat/lng: (", "").replace(")", "");
                            String[] split = splitll.split(",");

                            String lat = split[0];
                            String lat2 = lat.replace(".", ",");
                            String[] splitlat = lat2.split(",");

                            Long latlat = Long.parseLong(splitlat[1]);
                            Long newlat = latlat + 10000;

                            String newlat2 = splitlat[0].concat("." + String.valueOf(latlat));

//                            Double d1 = Double.parseDouble(newlat2) + (double) 90 / Math.pow(2, 16);
                            Double d1 = Double.parseDouble(newlat2);
                            Double d2 = (Double.parseDouble(split[1]));

//                            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(d1, d2), 16);
                            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(d1, d2), 16);

                            mMap.animateCamera(cu);

                            break;
                        }
                    }
                    break;
                }
            }

            if (!it.hasNext() && sortedNearbyLocationMap.size() > 0) {
                Toasty.normal(MapActivity.this, "That's all the gig found nearby", Toast.LENGTH_SHORT).show();
            }
            else if(sortedNearbyLocationMap.size() == 0){
                Toasty.info(MapActivity.this, "No show nearby now", Toast.LENGTH_SHORT, true).show();
            }

            markercounter++;
            if (markercounter >= newReorderedMap.size()) {
                markercounter = 0;
            }
        }
    }

    private Map<Location, String> reorderHashMap(Map<Location, String> hashmap) {

        int counter = 0;
        Map<Location, String> numFixNearbyLocation = new HashMap<>();

        for (Entry<Location, String> e1 : hashmap.entrySet()) {
            numFixNearbyLocation.put(e1.getKey(), String.valueOf(counter));
            counter = counter + 1;
        }

        return numFixNearbyLocation;
    }

    private ArrayList<String> getMarkerId(ArrayList<Marker> oldMarkerArray) {

        ArrayList<String> newMarkerArray = new ArrayList<>();
        for (Marker e1 : oldMarkerArray) {
            newMarkerArray.add(e1.getId());
        }

        return newMarkerArray;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000) {
            setLocationListener();
        }
    }

    @Override
    public void onItemSelected(int position) {
        if (firebaseAuth.getCurrentUser() == null) {
            switch (position) {
                case 0:
                    Intent intent = new Intent(MapActivity.this, ViewAllShowsActivity.class);
                    startActivity(intent);
                    break;
                case 1:
                    Intent intent2 = new Intent(MapActivity.this, SignInSignUpActivity.class);
                    startActivity(intent2);
                    break;
                case 2:
                    Intent intent3 = new Intent(MapActivity.this, AboutAppActivity.class);
                    startActivity(intent3);
                    break;
            }
        } else {
            switch (position) {
                case 0:
                    Intent intent = new Intent(MapActivity.this, ViewAllShowsActivity.class);
                    startActivity(intent);
                    break;
                case 1:
                    Intent intent2 = new Intent(MapActivity.this, AccountActivity.class);
                    intent2.putExtra("FROM_ACTIVITY", "mapact");
                    startActivity(intent2);
                    break;
                case 2:
                    Intent intent3 = new Intent(MapActivity.this, ViewMyShowsActivity.class);
                    intent3.putExtra("FROM_ACTIVITY", "mapact");
                    startActivity(intent3);
                    break;
                case 3:
                    Intent intent4 = new Intent(MapActivity.this, AboutAppActivity.class);
                    startActivity(intent4);
                    break;
            }
        }

    }

    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.black))
                .withTextTint(color(R.color.black))
                .withSelectedIconTint(color(R.color.yellow1))
                .withSelectedTextTint(color(R.color.yellow1));
    }

    private String[] loadScreenTitles() {
        if (firebaseAuth.getCurrentUser() == null) {
            return getResources().getStringArray(R.array.ld_activityScreenTitlesLoggedOut);
        } else {
            return getResources().getStringArray(R.array.ld_activityScreenTitlesLoggedIn);
        }
    }

    private Drawable[] loadScreenIcons() {

        TypedArray ta;

        if (firebaseAuth.getCurrentUser() == null) {
            ta = getResources().obtainTypedArray(R.array.ld_activityScreenIconsLoggedOut);
        } else {
            ta = getResources().obtainTypedArray(R.array.ld_activityScreenIconsLoggedIn);
        }

        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

    private void init() {

        btnGetNearByShow = findViewById(R.id.btnGetNearbyShow);
        btnGetNearByShow.setVisibility(View.INVISIBLE);
        btnGetNearByShow.setOnClickListener(this);

        relativeLayout = findViewById(R.id.spinnerkitloader);
        relativeLayout.setVisibility(View.VISIBLE);

        relativeLayoutMap = findViewById(R.id.relativeLayoutMap);
        relativeLayoutMap.setOnClickListener(this);

        frameLayout = findViewById(R.id.framelayout_maptouch);
        frameLayout.setOnTouchListener(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        window = getWindow();
        window.setStatusBarColor(Color.BLACK);

        createDrawer();

        //Custom Toast Set up
        Toasty.Config.getInstance()
                .setInfoColor(getResources().getColor(R.color.red2))
//                .setInfoColor(Color.RED)
                .apply(); // required

    }

    private void createDrawer() {

         slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withSavedState(savedInstanceState2)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();


        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        if (firebaseAuth.getCurrentUser() == null) {

            int FINDNEARBY = 0;
            int VIEWALLSHOW = 1;
            int SIGNUP = 2;
            int ABOUTAPP = 3;

            //show list for not logged in user
            adapter = new DrawerAdapter(Arrays.asList(
                    createItemFor(VIEWALLSHOW),
                    createItemFor(SIGNUP),
                    createItemFor(ABOUTAPP)));
        } else {
            //logged in user
            int FINDNEARBY = 0;
            int VIEWALLSHOW = 1;
            int ACCOUNT = 2;
            int MYSHOW = 3;
            int ABOUTAPP = 4;

            //show list for not logged in user
            adapter = new DrawerAdapter(Arrays.asList(
                    createItemFor(VIEWALLSHOW),
                    createItemFor(ACCOUNT),
                    createItemFor(MYSHOW),
                    createItemFor(ABOUTAPP)));
        }

        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

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

                        mMap.setMyLocationEnabled(true);

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(MapActivity.this, 2000);
                        } catch (IntentSender.SendIntentException e) {
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mGoogleApiClient.disconnect();
        mapFragment.onDestroy();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if(!slidingRootNav.isMenuHidden()){
            slidingRootNav.closeMenu();
            final Handler handler = new Handler();
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                    } // Just catch the InterruptedException

                    // Now we use the Handler to post back to the main thread
                    handler.post(new Runnable() {
                        public void run() {
                            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(currentLat), Double.parseDouble(currentLong)), 14);
                            mMap.animateCamera(cu);
                        }
                    });
                }
            }).start();
        }
        return false;
    }


}
