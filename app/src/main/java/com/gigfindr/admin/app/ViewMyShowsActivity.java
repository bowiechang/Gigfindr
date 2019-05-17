package com.gigfindr.admin.app;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class ViewMyShowsActivity extends AppCompatActivity implements OnClickListener {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private RecyclerView recyclerView;
    private ImageView ivAddShow;
    private List<ShowDetails> list;
    private ShowDetails showDetails;
    private String uid = user.getUid();
    private String prevAct;
    private int switcher = 0;

    private LinearLayout llNoShow;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_shows);


        Intent mIntent = getIntent();
        prevAct = mIntent.getStringExtra("FROM_ACTIVITY");


        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView title = findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        recyclerView.setVisibility(View.INVISIBLE);

        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(prevAct == null){
                    Intent myIntent = new Intent(ViewMyShowsActivity.this, MapActivity.class);
                    ViewMyShowsActivity.this.startActivity(myIntent);
                }
                else if(prevAct.equalsIgnoreCase("mapact")){
                    finish();
                }

            }
        });

        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.black));

        ivAddShow = findViewById(R.id.ivAddShow);
        ivAddShow.setOnClickListener(this);
        llNoShow = findViewById(R.id.llNoShow);

        final RelativeLayout relativeLayout = findViewById(R.id.spinnerkitloader);
        relativeLayout.setVisibility(View.VISIBLE);

        list = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Show");

        //check if there is an intent coming in to change uid or not
        // FOR NOW THERE IS NO NEED TO SWITCH, IDEA WAS SCRAPPED BUT MIGHT REUSE
        //bundle
//        Intent intent = getIntent();
//        Bundle extras = intent.getExtras();
//        if (extras != null) {
//            //their show
//            uid = getIntent().getExtras().getString("uid");
//            switcher = 1;
//        }
//        else{
//            uid = user.getUid();
//        }

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                showDetails = dataSnapshot.getValue(ShowDetails.class);
                if (showDetails != null) {

                    if (showDetails.getUserid().equals(uid)) {

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
                        if (DateshowDate.after(DatetodayDate) || DateshowDate.equals(DatetodayDate)) {
                            list.add(showDetails);
                            recyclerView.setVisibility(View.VISIBLE);
                            llNoShow.setVisibility(View.INVISIBLE);
                        }
                    }
                }

                if (switcher == 0) {
                    getMyShowsList(dataSnapshot);
                } else {
                    getTheirShowsList(dataSnapshot);
                }

                relativeLayout.setVisibility(View.GONE);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (switcher == 0) {
                    showDetails = dataSnapshot.getValue(ShowDetails.class);
                    list.add(showDetails);
                    getMyShowsList(dataSnapshot);
                } else {
                    getTheirShowsList(dataSnapshot);
                }
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

    private void getMyShowsList(DataSnapshot dataSnapshot){

        dateSorter((ArrayList) list);
        ViewMyShowsAdapter viewMyShowsAdapter = new ViewMyShowsAdapter(ViewMyShowsActivity.this, list);
        recyclerView.setAdapter(viewMyShowsAdapter);

    }

    private void getTheirShowsList(DataSnapshot dataSnapshot){

        dateSorter((ArrayList) list);
        ViewTheirShowsAdapter viewTheirShowsAdapter = new ViewTheirShowsAdapter(ViewMyShowsActivity.this, list);
        recyclerView.setAdapter(viewTheirShowsAdapter);

    }

    private void dateSorter(ArrayList list){
        Collections.sort(list, new Comparator<ShowDetails>() {
            public int compare(ShowDetails showDetails, ShowDetails showDetails2) {
                if (showDetails.getDate() == null || showDetails2.getDate() == null)
                    return 0;

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                Date date1 = null;
                Date date2 = null;

                try {
                    date1 = sdf.parse(showDetails.getDate());
                    date2 = sdf.parse(showDetails2.getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return date1.compareTo(date2);
            }
        });
    }

    @Override
    public void onClick(View view) {
//        if(view.equals(constraintLayoutAddShow)) {
        if(view.equals(ivAddShow)) {
            Intent intent = new Intent(ViewMyShowsActivity.this, PostShowActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(prevAct == null){
            Intent myIntent = new Intent(ViewMyShowsActivity.this, MapActivity.class);
            ViewMyShowsActivity.this.startActivity(myIntent);
        }
        else if(prevAct.equalsIgnoreCase("mapact")){
            finish();
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
