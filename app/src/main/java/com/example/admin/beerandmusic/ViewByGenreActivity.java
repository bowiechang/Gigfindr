package com.example.admin.beerandmusic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yayandroid.parallaxrecyclerview.ParallaxRecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ViewByGenreActivity extends AppCompatActivity {

    private ParallaxRecyclerView recyclerView;
    private List<ShowDetails> list;
    private ShowDetails showDetails;
    private TextView tvNoShow, title;

    private RelativeLayout relativeLayout;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Show");

    private Window window;
    private Toolbar toolbar;
    private String genre;

    private ChildEventListener childEventListener1;

    private LinearLayout llNoShow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_by_genre);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null) {
            genre = getIntent().getExtras().getString("genre");
        }

        //new

        window = getWindow();

        toolbar = findViewById(R.id.toolbar);

        title = findViewById(R.id.toolbar_title);
        title.setText("Upcoming " + genre + " gigs");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        //new

        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.black));

        tvNoShow = findViewById(R.id.tvNoShow);
        llNoShow = findViewById(R.id.llNoShow);

        relativeLayout = findViewById(R.id.spinnerkitloader);
        relativeLayout.setVisibility(View.VISIBLE);
        tvNoShow.setVisibility(View.GONE);
        llNoShow.setVisibility(View.GONE);

        list = new ArrayList<>();

        startChildEventListener();

        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void getAllShowList(DataSnapshot dataSnapshot){
        dateSorter((ArrayList) list);
        ViewAllShowsAdapter viewAllShowsAdapter = new ViewAllShowsAdapter(ViewByGenreActivity.this, list);
        recyclerView.setAdapter(viewAllShowsAdapter);

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

    private void startChildEventListener(){

        childEventListener1 = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                showDetails = dataSnapshot.getValue(ShowDetails.class);
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

                    if (DateshowDate.after(DatetodayDate) || DateshowDate.equals(DatetodayDate)) {
                        if(showDetails.getGenre().contains(genre)) {
                            list.add(showDetails);
                            getAllShowList(dataSnapshot);
                        }
                    }
                }

                relativeLayout.setVisibility(View.GONE);
                if(list.isEmpty()){
                    tvNoShow.setVisibility(View.VISIBLE);
                    llNoShow.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                else{
                    tvNoShow.setVisibility(View.GONE);
                    llNoShow.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                getAllShowList(dataSnapshot);
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
        };

        databaseReference.addChildEventListener(childEventListener1);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(childEventListener1);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
