package com.gigfindr.admin.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yayandroid.parallaxrecyclerview.ParallaxRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdminViewReoccuringShowActivity extends AppCompatActivity {

    private ParallaxRecyclerView recyclerView;
    private List<ShowDetails> list;
    private ShowDetails showDetails;

    private RelativeLayout relativeLayout;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Show");

    private Window window;
    private Toolbar toolbar;
    private ImageView imageViewAdd;
    private String genre;

    private ChildEventListener childEventListener1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_post_show);

        window = getWindow();
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        //new

        recyclerView = findViewById(R.id.rv);
        imageViewAdd = findViewById(R.id.ivAddShow);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        imageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminViewReoccuringShowActivity.this, AdminPostShowActivity.class);
                startActivity(intent);
            }
        });

        window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.black));
        list = new ArrayList<>();
        startChildEventListener();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminViewReoccuringShowActivity.this, AboutAppActivity.class);
                startActivity(intent);
            }
        });

    }

    private void getAllShowList(){
        AdminViewReoccuringShowAdapter adminViewReoccuringShowAdapter = new AdminViewReoccuringShowAdapter(AdminViewReoccuringShowActivity.this, list);
        recyclerView.setAdapter(adminViewReoccuringShowAdapter);
    }

    private void startChildEventListener(){

        childEventListener1 = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                showDetails = dataSnapshot.getValue(ShowDetails.class);
                list.add(showDetails);
            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                getAllShowList();
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

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<ShowDetails> arrayListNew = new ArrayList<>();
                for(ShowDetails element: list){
                    ShowDetails showDetailsNow = (ShowDetails) element;
                    Log.d("adminpostshowact::", "showdetailsnow details:: " + showDetailsNow.getBandName() + "," + showDetailsNow.getAddress() + "," +showDetailsNow.getStartTime());
                    for(ShowDetails element2 : arrayListNew){
                        ShowDetails showDetailsNext = element2;
                        Log.d("adminpostshowact::", "showDetailsNext details:: " + showDetailsNext.getBandName() + "," + showDetailsNext.getAddress() + "," +showDetailsNext.getStartTime());

                        if(!showDetailsNext.getUserid().equalsIgnoreCase(showDetailsNow.getUserid())){
                            if(!showDetailsNext.getAddress().equalsIgnoreCase(showDetailsNow.getAddress())){
                                if(!showDetailsNext.getStartTime().equalsIgnoreCase(showDetailsNow.getStartTime())){
                                    arrayListNew.add(showDetailsNow);
                                }
                            }
                        }
                    }
                }
                getAllShowList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
        Intent intent = new Intent(AdminViewReoccuringShowActivity.this, AboutAppActivity.class);
        startActivity(intent);
    }
}

