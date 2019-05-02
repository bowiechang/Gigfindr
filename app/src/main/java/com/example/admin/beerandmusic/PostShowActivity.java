package com.example.admin.beerandmusic;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class PostShowActivity extends AppCompatActivity implements OnConnectionFailedListener, OnClickListener {

    private Button btnPost, btnDelete;

    int PLACE_PICKER_REQUEST = 1;

    protected CharSequence[] genres = { "Alternative", "Rock", "Blues", "Electronic", "Hip-Hop", "Indie", "Jazz", "Metal", "Pop", "Punk", "Reggae", "Soul" };

    protected ArrayList<CharSequence> selectedGenres = new ArrayList<CharSequence>();

    private int day, month, year, hour, mins;
    private String uid;

    Calendar calendar = Calendar.getInstance();

    LatLng latlng;

    String startTime = "";
    String endTime = "";
    String locationName = "";
    String address = "";
    String entryFee = "";
    String date2 = "";
    String latLng = "";
    String editedLatLng = "";
    String genre = "";
    String placeid = "";

    String key = "";

    String bandName;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private LinearLayout llstarttime, llendtime, llplace, lldate, llfee, llgenre;
    private TextView tvStartTime, tvEndTime, tvLocation, tvDate, tvFee, tvGenre, tvAddress, tvToolbar;

    private ShowDetails showDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.redesign_activity_post_show2);

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
        btnDelete = findViewById(R.id.btnDeleteShow);
        btnPost.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Window window = getWindow();
        window.setStatusBarColor(Color.BLACK);

        btnDelete.setVisibility(View.GONE);


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();


        //get bandname
        databaseReference.child("User").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserDetails userDetails = dataSnapshot.getValue(UserDetails.class);
                if(userDetails == null){
//                    Toast.makeText(PostShowActivity.this, "Please fill up your account first", Toast.LENGTH_SHORT).show();
                    Toasty.Config.getInstance()
                            .setInfoColor(getResources().getColor(R.color.yellow1))
                            .apply(); // required
                    Toasty.info(PostShowActivity.this, "Please fill up your account first", Toast.LENGTH_LONG, true).show();
                    Intent intent = new Intent(getBaseContext(), AccountActivity.class);
                    startActivity(intent);
                }
                else {
                    bandName = userDetails.getName();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //bundle
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null) {
            btnPost.setText("Update");
            btnDelete.setVisibility(View.VISIBLE);
            tvToolbar.setText("Update Show");
            date2 = getIntent().getExtras().getString("date");
            startTime = getIntent().getExtras().getString("starttime");

            databaseReference.child("Show").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    showDetails = dataSnapshot.getValue(ShowDetails.class);
                    if (showDetails.getUserid().equals(uid) && showDetails.getDate().equals(date2) && showDetails.getStartTime().equals(startTime)) {

                        key = dataSnapshot.getKey();
                        String[] monthValue = {"January","February","March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
                        String [] split = showDetails.getDate().split("/");
                        int month = Integer.parseInt(split[1]);
                        String dateformated = split[0] + " " + monthValue[(month-1)] + " " + split[2];

                        tvDate.setText(dateformated);
                        tvStartTime.setText(showDetails.getStartTime());
                        tvEndTime.setText(showDetails.getEndTime());
                        tvLocation.setText(showDetails.getLocationName());
                        tvAddress.setVisibility(View.VISIBLE);
                        tvAddress.setText(showDetails.getAddress());
                        date2 = showDetails.getDate();
                        latLng = showDetails.getLatLng();
                        tvFee.setText(showDetails.getEntryFee());
                        tvGenre.setText(showDetails.getGenre());

                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
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
                if(etFee.getText().toString().equalsIgnoreCase("free")){
                    tvFee.setText(R.string.free);
                }
                else{
                    tvFee.setText(etFee.getText().toString());
                }
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                dialog.dismiss();
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

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        Intent intent;
        try {
            intent = builder.build(PostShowActivity.this);
            startActivityForResult(intent, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == PLACE_PICKER_REQUEST){
            if(resultCode == RESULT_OK){

                Place place = PlaceAutocomplete.getPlace(this, data);
                String locationName = String.format("%s", place.getName());
                String address = String.format("%s", place.getAddress());
                LatLng latLng = place.getLatLng();

                placeid = place.getId();
                latlng = latLng;
                tvLocation.setText(locationName);
                tvAddress.setText(address);
                tvAddress.setVisibility(View.VISIBLE);
            }
            else{
//                Toast.makeText(getApplicationContext(), "Error resultcode", Toast.LENGTH_SHORT).show();

                Toasty.error(PostShowActivity.this, "Error, please try again", Toast.LENGTH_LONG, true).show();
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        Toast.makeText(getApplicationContext(), "Error Occur", Toast.LENGTH_SHORT).show();
        Toasty.error(PostShowActivity.this, "Error, please try again", Toast.LENGTH_LONG, true).show();
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

                date2 = (day+"/"+(month+1)+"/"+(year));
                tvDate.setText(day+" "+monthValue[(month)]+" "+(year));
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

        startTime = tvStartTime.getText().toString();
        endTime = tvEndTime.getText().toString();
        locationName = tvLocation.getText().toString();
        address = tvAddress.getText().toString();
        entryFee = tvFee.getText().toString();
        if(entryFee.equalsIgnoreCase("fee")){
            entryFee = "";
        }
        String date = date2;
        if(latlng!=null) {
            latLng = latlng.toString();
        }
        genre = tvGenre.getText().toString();
        if(genre.equalsIgnoreCase("genre")){
            genre = "";
        }
        editedLatLng = "";

        if(startTime.equals("") || endTime.equals("") || locationName.equals("") || address.equals("") || entryFee.equals("") || date.equals("") || latLng.equals("") || genre.equals("")) {

//            Toast.makeText(PostShowActivity.this, "Empty fields please check", Toast.LENGTH_SHORT).show();
            Toasty.Config.getInstance()
                    .setInfoColor(getResources().getColor(R.color.yellow1));
            Toasty.info(PostShowActivity.this, "Empty fields please check", Toast.LENGTH_SHORT, true).show();

        }
        else {
            if (key == "") {

                if (entryFee.matches("\\d+(?:\\.\\d+)?") || entryFee.equalsIgnoreCase("free")){
                    String newll = latlng.toString().replace("lat/lng: (", "").replace(")", "");

                    ShowDetails showDetails = new ShowDetails(locationName, address, newll, date, startTime, endTime, entryFee, uid, bandName, genre, placeid);
                    databaseReference.child("Show").push().setValue(showDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent intent = new Intent(PostShowActivity.this, ViewMyShowsActivity.class);
                            startActivity(intent);
    //                        Toast.makeText(getApplicationContext(), "Post Successful!", Toast.LENGTH_SHORT).show();
                            Toasty.success(PostShowActivity.this, "Post Successful!", Toast.LENGTH_LONG, true).show();
                        }
                    });
                }
                else{
                    Toasty.info(PostShowActivity.this, "Entry Fee can only be 'Free' or Numeric Price!", Toast.LENGTH_SHORT, true).show();

                }
            }
            else{

                if(!showDetails.getPlaceid().isEmpty() || !showDetails.getPlaceid().equals("")){
                    placeid = showDetails.getPlaceid();
                }

                if (entryFee.equalsIgnoreCase("0") || entryFee.equalsIgnoreCase("free")) {


                    ShowDetails showDetails = new ShowDetails(locationName, address, latLng, date, startTime, endTime, entryFee, uid, bandName, genre, placeid);
                    databaseReference.child("Show").child(key).setValue(showDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent intent = new Intent(PostShowActivity.this, ViewMyShowsActivity.class);
                            startActivity(intent);
//                        Toast.makeText(getApplicationContext(), "Update Successful!", Toast.LENGTH_SHORT).show();
                            Toasty.success(PostShowActivity.this, "Update Successful!", Toast.LENGTH_LONG, true).show();
                        }
                    });
                }
                else{
                    Toasty.error(PostShowActivity.this, "Entry Fee can only be Free or Numeric Price!", Toast.LENGTH_LONG, true).show();

                }

            }
        }
    }

    public void deleteShow(){

        databaseReference.child("Show").child(key).removeValue();
        Intent intent = new Intent(PostShowActivity.this, ViewMyShowsActivity.class);
        startActivity(intent);
//        Toast.makeText(getApplicationContext(), "Delete Successful!", Toast.LENGTH_SHORT).show();
        Toasty.success(PostShowActivity.this, "Delete Successful!", Toast.LENGTH_LONG, true).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                onBackPressed();

                Intent myIntent = new Intent(PostShowActivity.this, ViewMyShowsActivity.class);
                PostShowActivity.this.startActivity(myIntent);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        if(view == llplace) {
            selectLocation();
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
        else if(view == btnDelete){

            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.deletedialog);

            // set the custom dialog components - text, image and button
            RelativeLayout btnYes = dialog.findViewById(R.id.btnYes);
            RelativeLayout btnNo = dialog.findViewById(R.id.btnNo);

            btnYes.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    deleteShow();

                    dialog.dismiss();
                }
            });

            btnNo.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            dialog.show();
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
}


