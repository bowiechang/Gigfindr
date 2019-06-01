package com.gigfindr.admin.app;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rtchagas.pingplacepicker.PingPlacePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;

//import com.rtchagas.pingplacepicker.PingPlacePicker;

public class AdminPostShowActivity extends AppCompatActivity implements OnConnectionFailedListener, OnClickListener {

    private Button btnPost;
    private Spinner userSpinner;
    private String uid;

    int PLACE_PICKER_REQUEST = 1;

    protected CharSequence[] genres = { "Alternative", "Rock", "Blues", "Electronic", "Hip-Hop", "Indie", "Jazz", "Metal", "Pop", "Punk", "Reggae", "Soul" };

    protected ArrayList<CharSequence> selectedGenres = new ArrayList<CharSequence>();

    private int day, month, year, hour, mins;
    private Boolean pppchecker = false;

    Calendar calendar = Calendar.getInstance();

    LatLng latlngFromPicker;

    String startTime = "";
    String endTime = "";
    String locationName = "";
    String address = "";
    String entryFee = "";
    String date2 = "";
    String latLngRetrieved = "";
    String editedLatLng = "";
    String genre = "";
    String placeid = "";

    String key = "";

    String bandName;
    String prevAct;

    Toolbar toolbar;



    String repeatuid;
    String repeatlocation;
    String repeatlatlng;
    String repeatstarttime;
    String repeatendtime;
    String repeatentryfee;
    String repeatbandname;
    String repeatgenre;
    String repeatplaceid;
    String repeataddress;
    Boolean checker = false;
    ArrayList<String> arrayListDate = new ArrayList<>();

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference databaseReferenceUser = FirebaseDatabase.getInstance().getReference();

    private LinearLayout llstarttime, llendtime, llplace, lldate, llfee, llgenre;
    private TextView tvStartTime, tvEndTime, tvLocation, tvDate, tvFee, tvGenre, tvAddress, tvToolbar;

    private ShowDetails showDetails;
    private ArrayList<String> userDetailsArrayList = new ArrayList<>();
    private HashMap<String, String> hashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_redesign_activity_post_show2);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null) {
            Toasty.normal(AdminPostShowActivity.this, "Repeating post mode", Toast.LENGTH_LONG).show();
             repeatuid = getIntent().getExtras().getString("uid");
             repeatlocation = getIntent().getExtras().getString("location");
             repeatlatlng = getIntent().getExtras().getString("latlng");
             repeatstarttime = getIntent().getExtras().getString("starttime");
             repeatendtime = getIntent().getExtras().getString("endtime");
             repeatentryfee = getIntent().getExtras().getString("entryfee");
             repeatbandname = getIntent().getExtras().getString("bandname");
             repeatgenre = getIntent().getExtras().getString("genre");
             repeatplaceid = getIntent().getExtras().getString("placeid");
             repeataddress = getIntent().getExtras().getString("address");
             checker = true;
        }

        //new
        llstarttime = findViewById(R.id.startTime);
        llendtime = findViewById(R.id.endTime);
        llplace = findViewById(R.id.location);
        lldate = findViewById(R.id.date);
        llfee = findViewById(R.id.fee);
        llgenre = findViewById(R.id.genre);

        llstarttime.setOnClickListener(this);
        llendtime.setOnClickListener(this);
        llplace.setOnClickListener(this);
        lldate.setOnClickListener(this);
        llfee.setOnClickListener(this);
        llgenre.setOnClickListener(this);

        tvStartTime = findViewById(R.id.tvStartTime);
        tvEndTime = findViewById(R.id.tvEndTime);
        tvLocation = findViewById(R.id.tvLocation);
        tvDate = findViewById(R.id.tvDate);
        tvFee = findViewById(R.id.tvFee);
        tvGenre = findViewById(R.id.tvGenre);
        tvAddress = findViewById(R.id.tvAddress);
        tvToolbar = findViewById(R.id.toolbar_title);

        btnPost = findViewById(R.id.btnAddShow);
        btnPost.setOnClickListener(this);

        userSpinner = findViewById(R.id.userSpinner);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminPostShowActivity.this, AdminViewReoccuringShowActivity.class);
                startActivity(intent);
            }
        });

        Intent mIntent = getIntent();
        prevAct = mIntent.getStringExtra("FROM_ACTIVITY");

        if(!Places.isInitialized()){
            Places.initialize(getApplicationContext(), getString(R.string.google_geo_api_key_new));
            System.out.println("Postshow:: places is init");
            System.out.println("Postshow:: places is init: " + Places.isInitialized());
        }

        Window window = getWindow();
        window.setStatusBarColor(Color.BLACK);

//
//        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        uid = user.getUid();


        databaseReference.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    UserDetails userDetails = child.getValue(UserDetails.class);
                    if (userDetails != null) {
                        userDetailsArrayList.add(userDetails.getName());
                        hashMap.put(child.getKey(), userDetails.getName());
                    }
                }

                ArrayAdapter<String> adapter =
                        new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, userDetailsArrayList);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                userSpinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    protected void showSelectGenreDialog() {

        boolean[] checkedGenres = new boolean[genres.length];

        int count = genres.length;

        for(int i = 0; i < count; i++)

            checkedGenres[i] = selectedGenres.contains(genres[i]);

        DialogInterface.OnMultiChoiceClickListener genresDialogListener = new DialogInterface.OnMultiChoiceClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                if(isChecked)

                    selectedGenres.add(genres[which]);

                else

                    selectedGenres.remove(genres[which]);

                onChangeSelectedGenres();

            }

        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Genres");
        builder.setMultiChoiceItems(genres, checkedGenres, genresDialogListener);
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    protected void onChangeSelectedGenres() {

        StringBuilder stringBuilder = new StringBuilder();

        for(int index = 0; index < selectedGenres.size(); index++){
            CharSequence currentitem = selectedGenres.get(index);
            if(index != selectedGenres.size() - 1) {
                stringBuilder.append(currentitem + ", ");
            }
            else{
                stringBuilder.append(currentitem);
            }
        }

        tvGenre.setText(stringBuilder.toString());

    }

    protected void getFee(){

        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.feedialogforpost);

        // set the custom dialog components - text, image and button
        final EditText etFee = dialog.findViewById(R.id.etFee);
        RelativeLayout tvOk = dialog.findViewById(R.id.tvconfirm);
        RelativeLayout tvCancel = dialog.findViewById(R.id.tvcancel);

        tvOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etFee.getText().toString().equalsIgnoreCase("0")) {
                    tvFee.setText(R.string.free);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    dialog.dismiss();
                }
                else{
                    tvFee.setText(etFee.getText().toString());
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    dialog.dismiss();
                }
            }
        });

        tvCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void selectLocation(){

        pppchecker = true;

        String geoApiKey = getString(R.string.google_geo_api_key_new);
        PingPlacePicker.IntentBuilder builder = new PingPlacePicker.IntentBuilder();
        builder.setAndroidApiKey(getString(R.string.google_api_key))
                .setGeolocationApiKey(geoApiKey);
        try{
            Intent placeIntent = builder.build(this);
            startActivityForResult(placeIntent, PLACE_PICKER_REQUEST);
        }
        catch(Exception e){
            System.out.println("keys issut : " +  e.toString());

        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
//        if(requestCode == PLACE_PICKER_REQUEST){
//            if(resultCode == RESULT_OK){
//
//                Place place = PlaceAutocomplete.getPlace(this, data);
//                String locationName = String.format("%s", place.getName());
//                String address = String.format("%s", place.getAddress());
//                LatLng latLngRetrieved = place.getLatLng();
//
//                placeid = place.getId();
//                latlngFromPicker = latLngRetrieved;
//                tvLocation.setText(locationName);
//                tvAddress.setText(address);
//                tvAddress.setVisibility(View.VISIBLE);
//            }
//            else{
////                Toast.makeText(getApplicationContext(), "Error resultcode", Toast.LENGTH_SHORT).show();
//
//                Toasty.error(PostShowActivity.this, "Error, please try again", Toast.LENGTH_LONG, true).show();
//            }
//        }

        if(requestCode == PLACE_PICKER_REQUEST){
            if(resultCode == RESULT_OK){

                Place place = PingPlacePicker.Companion.getPlace(data);
                if(place!=null){
                    String locationName = String.format("%s", place.getName());
                String address = String.format("%s", place.getAddress());
                LatLng latLng = place.getLatLng();

                placeid = place.getId();
                latlngFromPicker = latLng;
                tvLocation.setText(locationName);
                tvAddress.setText(address);
                tvAddress.setVisibility(View.VISIBLE);
                }
                else{
                    Toasty.error(AdminPostShowActivity.this, "Error, please try again", Toast.LENGTH_LONG, true).show();

                }

            }
            else{
//                Toast.makeText(getApplicationContext(), "Error resultcode", Toast.LENGTH_SHORT).show();

                Toasty.error(AdminPostShowActivity.this, "Error, please try again", Toast.LENGTH_LONG, true).show();
            }

            pppchecker = false;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        Toast.makeText(getApplicationContext(), "Error Occur", Toast.LENGTH_SHORT).show();
        Toasty.error(AdminPostShowActivity.this, "Error, please try again", Toast.LENGTH_LONG, true).show();
    }

    //date method
    public void getDate(){

        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                String[] monthValue = {"January","February","March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
                if(!checker) {
                    date2 = (day + "/" + (month + 1) + "/" + (year));
                    tvDate.setText(day + " " + monthValue[(month)] + " " + (year));
                }
                else{
                    arrayListDate.add((day + "/" + (month + 1) + "/" + (year)));
                }
            }
        }
        ,day,month,year);
        datePickerDialog.updateDate(year, month, day);
        datePickerDialog.show();
    }

    //start time
    public void getStartTime(){
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        mins = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener(){

            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {

                if(hour > 12){
                    hour = hour - 12;
                    tvStartTime.setText(String.format("%01d:%02d PM", hour, minute));
                }
                else{
                    tvStartTime.setText(String.format("%01d:%02d AM", hour, minute));
                }

            }
        },hour, mins, false);
        timePickerDialog.show();
    }

    //end time
    public void getEndTime(){
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        mins = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener(){

            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {

                if(hour > 12){
                    hour = hour - 12;
                    tvEndTime.setText(String.format("%01d:%02d PM", hour, minute));
                }
                else{
                    tvEndTime.setText(String.format("%01d:%02d AM", hour, minute));
                }

            }
        },hour, mins, false);
        timePickerDialog.show();
    }



    public void post(){
        if(!checker) {
            String name = userSpinner.getSelectedItem().toString();
            for (String key : hashMap.keySet()) {
                if (hashMap.get(key).equals(name)) {
                    uid = key;
                }
            }

            startTime = tvStartTime.getText().toString().trim();
            endTime = tvEndTime.getText().toString().trim();
            locationName = tvLocation.getText().toString();
            address = tvAddress.getText().toString();
            entryFee = tvFee.getText().toString().trim();
            if (entryFee.equalsIgnoreCase("fee")) {
                entryFee = "";
            }
            String date = date2;
            if (latlngFromPicker != null) {
                latLngRetrieved = latlngFromPicker.toString();
            }
            genre = tvGenre.getText().toString();
            if (genre.equalsIgnoreCase("genre")) {
                genre = "";
            }
            editedLatLng = "";

            if (startTime.equals("") || endTime.equals("") || locationName.equals("") || address.equals("") || entryFee.equals("") || date.equals("") || latLngRetrieved.equals("") || genre.equals("")) {

//            Toast.makeText(PostShowActivity.this, "Empty fields please check", Toast.LENGTH_SHORT).show();
//            Toasty.Config.getInstance()
//                    .setInfoColor(getResources().getColor(R.color.yellow1));
                Toasty.error(AdminPostShowActivity.this, "Empty fields please check", Toast.LENGTH_SHORT, true).show();

            } else {
                if (key == "") {

                    if (entryFee.matches("\\d+(?:\\.\\d+)?") || entryFee.equalsIgnoreCase("free")) {
                        String newll = latlngFromPicker.toString().replace("lat/lng: (", "").replace(")", "");

                        ShowDetails showDetails = new ShowDetails(locationName, address, newll, date, startTime, endTime, entryFee, uid, name, genre, placeid);
                        databaseReference.child("Show").push().setValue(showDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Intent intent = new Intent(AdminPostShowActivity.this, AdminViewReoccuringShowActivity.class);
                                startActivity(intent);
                                //                        Toast.makeText(getApplicationContext(), "Post Successful!", Toast.LENGTH_SHORT).show();
                                Toasty.success(AdminPostShowActivity.this, "Post Successful!", Toast.LENGTH_LONG, true).show();
                            }
                        });
                    } else {
                        Toasty.info(AdminPostShowActivity.this, "Entry Fee can only be 'Free' or Numeric Price!", Toast.LENGTH_SHORT, true).show();
                    }
                } else {

                    if (entryFee.matches("\\d+(?:\\.\\d+)?") || entryFee.equalsIgnoreCase("free")) {
                        String llTouse;
                        if (latlngFromPicker == null) {
                            llTouse = latLngRetrieved;
                            placeid = showDetails.getPlaceid();
                        } else {
                            llTouse = latlngFromPicker.toString().replace("lat/lng: (", "").replace(")", "");
                        }
                        ShowDetails showDetails = new ShowDetails(locationName, address, llTouse, date, startTime, endTime, entryFee, uid, bandName, genre, placeid);
                        databaseReference.child("Show").child(key).setValue(showDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Intent intent = new Intent(AdminPostShowActivity.this, AdminViewReoccuringShowActivity.class);
                                startActivity(intent);
//                        Toast.makeText(getApplicationContext(), "Update Successful!", Toast.LENGTH_SHORT).show();
                                Toasty.success(AdminPostShowActivity.this, "Update Successful!", Toast.LENGTH_LONG, true).show();
                            }
                        });
                    } else {
                        Toasty.error(AdminPostShowActivity.this, "Entry Fee can only be Free or Numeric Price!", Toast.LENGTH_LONG, true).show();

                    }
                }
            }
        }
        else{
            for (int i = 0; i < arrayListDate.size(); i++) {
                ShowDetails showDetails = new ShowDetails(repeatlocation, repeataddress, repeatlatlng, arrayListDate.get(i), repeatstarttime, repeatendtime,
                        repeatentryfee, repeatuid, repeatbandname, repeatgenre, repeatplaceid);
                databaseReference.child("Show").push().setValue(showDetails);

            }
            Toasty.success(AdminPostShowActivity.this, "ALL SHOW POSTED!", Toast.LENGTH_LONG, true).show();
            Intent intent = new Intent(AdminPostShowActivity.this, AdminViewReoccuringShowActivity.class);
            startActivity(intent);

        }
    }

    public void deleteShow(){

        databaseReference.child("Show").child(key).removeValue();
        Intent intent = new Intent(AdminPostShowActivity.this, ViewMyShowsActivity.class);
        startActivity(intent);
//        Toast.makeText(getApplicationContext(), "Delete Successful!", Toast.LENGTH_SHORT).show();
        Toasty.success(AdminPostShowActivity.this, "Delete Successful!", Toast.LENGTH_LONG, true).show();
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
////                onBackPressed();
//
//                Intent myIntent = new Intent(PostShowActivity.this, ViewMyShowsActivity.class);
//                PostShowActivity.this.startActivity(myIntent);
//
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onClick(View view) {

        if(view == llplace) {
            if(!pppchecker) {
                selectLocation();
            }
            else{
                Toasty.normal(AdminPostShowActivity.this, "Loading please wait", Toast.LENGTH_SHORT).show();
            }
        }
        else if(view == lldate){
            getDate();
        }
        else if(view == llfee){
            getFee();
        }
        else if(view == btnPost){
            post();
        }
        else if(view == llgenre){
            showSelectGenreDialog();
        }
        else if(view == llstarttime){
            getStartTime();
        }
        else if(view == llendtime){
            getEndTime();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AdminPostShowActivity.this, AdminViewReoccuringShowActivity.class);
        startActivity(intent);
    }
}


