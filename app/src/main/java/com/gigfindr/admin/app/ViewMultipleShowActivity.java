package com.gigfindr.admin.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import lib.kingja.switchbutton.SwitchMultiButton;

public class ViewMultipleShowActivity extends AppCompatActivity {

    private ParallaxRecyclerView recyclerView;
    private List<ShowDetails> list;
    private ShowDetails showDetails;

    private RelativeLayout relativeLayout;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Show");

    private Window window;
    private Toolbar toolbar;

    private FrameLayout frameLayout;
    private ChildEventListener childEventListener1;

    private ArrayList<ShowDetails> arrayListRetrieved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_multiple_show);

        arrayListRetrieved = (ArrayList<ShowDetails>) getIntent().getSerializableExtra("multipleShow");

        list = new ArrayList<>();
        toolbar = findViewById(R.id.toolbar);
        frameLayout = findViewById(R.id.genre_container);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        recyclerView.setVisibility(View.INVISIBLE);

        window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.black));

        relativeLayout = findViewById(R.id.spinnerkitloader);
        relativeLayout.setVisibility(View.VISIBLE);

        if(arrayListRetrieved.size() != 0){
            relativeLayout.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);

            getAllShowList(arrayListRetrieved);
        }
    }

    private void getAllShowList(ArrayList<ShowDetails> arrayList){

        dateSorter(arrayList);
        ViewAllShowsAdapter viewAllShowsAdapter = new ViewAllShowsAdapter(ViewMultipleShowActivity.this, arrayList);
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
        finish();

    }
}

