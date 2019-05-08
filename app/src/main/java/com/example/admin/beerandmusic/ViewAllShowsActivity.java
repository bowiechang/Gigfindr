package com.example.admin.beerandmusic;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
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

import lib.kingja.switchbutton.SwitchMultiButton;

public class ViewAllShowsActivity extends AppCompatActivity implements
        AnimatorUpdateListener, OnClickListener {

    private ParallaxRecyclerView recyclerView;
    private List<ShowDetails> list;
    private ShowDetails showDetails;
    private TextView tvNoShow, title;

    private RelativeLayout relativeLayout;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Show");

    private ValueAnimator statusBarAnimator;
    private Interpolator contentInInterpolator;
    private Interpolator contentOutInterpolator;

    private Window window;
    private Toolbar toolbar;
    private static final int DURATION_COLOR_CHANGE_MS = 400;

    private FrameLayout frameLayout;

    private int switcher;

    private ChildEventListener childEventListener1;

    private LinearLayout llNoShow;

    private SwitchMultiButton mSwitchMultiButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_shows);

        //setup buttons
        iButtonSetup();

        mSwitchMultiButton = findViewById(R.id.testerswitchbtn);
        mSwitchMultiButton.setVisibility(View.INVISIBLE);

        list = new ArrayList<>();

        switchlistener();

        initAnimationRelatedFields();

        toolbar = findViewById(R.id.toolbar);
        frameLayout = findViewById(R.id.genre_container);
        title = findViewById(R.id.toolbar_title);

        switcher = 0;

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });


        //new

        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        recyclerView.setVisibility(View.INVISIBLE);
        frameLayout.setVisibility(View.INVISIBLE);

        window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.black));

        tvNoShow = findViewById(R.id.tvNoShow);
        llNoShow = findViewById(R.id.llNoShow);

        relativeLayout = findViewById(R.id.spinnerkitloader);
        relativeLayout.setVisibility(View.VISIBLE);



        startChildEventListener();
    }

    private void switchlistener(){

        final int[] targetTranslation = {0};
        final Interpolator[] interpolator = {null};

        mSwitchMultiButton.setText("SHOWS", "FILTER").setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
            @Override
            public void onSwitch(int position, String tabText) {
                if(position == 0){

                    targetTranslation[0] = 0;
                    interpolator[0] = contentInInterpolator;
                    title.setText("Upcoming gigs");
                    if(list.isEmpty()){
//                        tvNoShow.setVisibility(View.VISIBLE);
                        llNoShow.setVisibility(View.VISIBLE);
                        mSwitchMultiButton.setVisibility(View.INVISIBLE);
                        frameLayout.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }

                    recyclerView.animate().cancel();
                    recyclerView.animate()
                            .translationY(targetTranslation[0])
                            .setInterpolator(interpolator[0])
                            .setDuration(DURATION_COLOR_CHANGE_MS)
                            .start();

                }
                else if(position == 1){

                    targetTranslation[0] = recyclerView.getHeight();
                    interpolator[0] = contentOutInterpolator;
                    title.setText(R.string.sort_by_genre);
                    frameLayout.setVisibility(View.VISIBLE);
                    llNoShow.setVisibility(View.INVISIBLE);
                    mSwitchMultiButton.setVisibility(View.VISIBLE);

                    if(list.isEmpty()){
                        recyclerView.setVisibility(View.GONE);
                        mSwitchMultiButton.setVisibility(View.INVISIBLE);

                    }

                    recyclerView.animate().cancel();
                    recyclerView.animate()
                            .translationY(targetTranslation[0] + 100)
                            .setInterpolator(interpolator[0])
                            .setDuration(DURATION_COLOR_CHANGE_MS)
                            .start();
                }
            }
        });



    }

    private void getAllShowList(DataSnapshot dataSnapshot){

        dateSorter((ArrayList) list);
        ViewAllShowsAdapter viewAllShowsAdapter = new ViewAllShowsAdapter(ViewAllShowsActivity.this, list);
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

    private void startChildEventListener() {

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
//                        setemptylisttrue(switcher);
                        list.add(showDetails);
                        getAllShowList(dataSnapshot);
                        recyclerView.setVisibility(View.VISIBLE);
                        mSwitchMultiButton.setVisibility(View.VISIBLE);
//                        tvNoShow.setVisibility(View.INVISIBLE);
                        llNoShow.setVisibility(View.INVISIBLE);
                        mSwitchMultiButton.setVisibility(View.VISIBLE);

                    }

                }


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
                                relativeLayout.setVisibility(View.GONE);
                            }
                        });
                    }
                }).start();
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
    public void onAnimationUpdate(ValueAnimator animator) {
        if (animator == statusBarAnimator) {
            int color = (Integer) animator.getAnimatedValue();
        }
    }

    private void initAnimationRelatedFields() {
        Point revealCenter = new Point();
        contentInInterpolator = new OvershootInterpolator(0.5f);
        contentOutInterpolator = new DecelerateInterpolator();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {}


    //ibSetup
    private void iButtonSetup(){

        ImageButton ibBlues = findViewById(R.id.imageButtonBlues);
        ImageButton ibRock = findViewById(R.id.imageButtonRock);
        ImageButton ibJazz = findViewById(R.id.imageButtonJazz);
        ImageButton ibIndie = findViewById(R.id.imageButtonIndie);
        ImageButton ibPop = findViewById(R.id.imageButtonPop);
        ImageButton ibHipHop = findViewById(R.id.imageButtonHipHop);
        ImageButton ibPunk = findViewById(R.id.imageButtonPunk);
        ImageButton ibReggae = findViewById(R.id.imageButtonReggae);
        ImageButton ibSoul = findViewById(R.id.imageButtonSoul);
        ImageButton ibElectronic = findViewById(R.id.imageButtonElectronic);
        ImageButton ibMetal = findViewById(R.id.imageButtonMetal);

        ibBlues.setOnClickListener(this);
        ibRock.setOnClickListener(this);
        ibJazz.setOnClickListener(this);
        ibIndie.setOnClickListener(this);
        ibPop.setOnClickListener(this);
        ibHipHop.setOnClickListener(this);
        ibPunk.setOnClickListener(this);
        ibReggae.setOnClickListener(this);
        ibSoul.setOnClickListener(this);
        ibElectronic.setOnClickListener(this);
        ibMetal.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(ViewAllShowsActivity.this, ViewByGenreActivity.class);
        Bundle extras = new Bundle();
        String genre;

        switch(v.getId()) {
            case R.id.imageButtonBlues:
                genre = "Blues";
                extras.putString("genre", genre);
                intent.putExtras(extras);
                startActivity(intent);
                break;
            case R.id.imageButtonRock:
                genre = "Rock";
                extras.putString("genre", genre);
                intent.putExtras(extras);
                startActivity(intent);
                break;
            case R.id.imageButtonJazz:
                genre = "Jazz";
                extras.putString("genre", genre);
                intent.putExtras(extras);
                startActivity(intent);
                break;
            case R.id.imageButtonIndie:
                genre = "Indie";
                extras.putString("genre", genre);
                intent.putExtras(extras);
                startActivity(intent);
                break;
            case R.id.imageButtonPop:
                genre = "Pop";
                extras.putString("genre", genre);
                intent.putExtras(extras);
                startActivity(intent);
                break;
            case R.id.imageButtonHipHop:
                genre = "Hip-Hop";
                extras.putString("genre", genre);
                intent.putExtras(extras);
                startActivity(intent);
                break;
            case R.id.imageButtonPunk:
                genre = "Punk";
                extras.putString("genre", genre);
                intent.putExtras(extras);
                startActivity(intent);
                break;
            case R.id.imageButtonReggae:
                genre = "Reggae";
                extras.putString("genre", genre);
                intent.putExtras(extras);
                startActivity(intent);
                break;
            case R.id.imageButtonSoul:
                genre = "Soul";
                extras.putString("genre", genre);
                intent.putExtras(extras);
                startActivity(intent);
                break;
            case R.id.imageButtonElectronic:
                genre = "Electronic";
                extras.putString("genre", genre);
                intent.putExtras(extras);
                startActivity(intent);
                break;
            case R.id.imageButtonMetal:
                genre = "Metal";
                extras.putString("genre", genre);
                intent.putExtras(extras);
                startActivity(intent);
                break;
        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
}
