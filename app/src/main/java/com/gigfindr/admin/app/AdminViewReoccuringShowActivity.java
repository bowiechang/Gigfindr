package com.gigfindr.admin.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
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

public class AdminViewReoccuringShowActivity extends AppCompatActivity implements TextWatcher {

    private ParallaxRecyclerView recyclerView;
    private ArrayList<ShowDetails> list;
    private ArrayList<ShowDetails> arrayListNew;
    private ArrayList<ShowDetails> arrayListClone;
    private ShowDetails showDetails;

    private RelativeLayout relativeLayout;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Show");

    private Window window;
    private Toolbar toolbar;
    private ImageView imageViewAdd;
    private String genre;

    private ChildEventListener childEventListener1;
    private EditText editTextSearch;

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
        editTextSearch = findViewById(R.id.etSearchBar);
        editTextSearch.addTextChangedListener(this);
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

    private void getAllShowList(ArrayList<ShowDetails> list){
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
                arrayListNew = new ArrayList<>();
                for(ShowDetails element: list){
                    ShowDetails showDetailsNow = element;
                    for(ShowDetails element2 : arrayListNew){
                        ShowDetails showDetailsNext = element2;

                        if(!showDetailsNext.getUserid().equalsIgnoreCase(showDetailsNow.getUserid())){
                            if(!showDetailsNext.getAddress().equalsIgnoreCase(showDetailsNow.getAddress())){
                                if(!showDetailsNext.getStartTime().equalsIgnoreCase(showDetailsNow.getStartTime())){
                                    arrayListNew.add(showDetailsNow);
                                }
                            }
                        }
                    }
                }
                getAllShowList(list);
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        arrayListClone = new ArrayList<>();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(s.toString().equalsIgnoreCase("")){
            getAllShowList(list);
        }
        else{
            Log.d("aftertext:: ", s.toString());
            for(ShowDetails element: list) {
                Log.d("bandname aftertext:: ", element.getBandName());

                if(element.getBandName().contains((s.toString().toUpperCase()))){
                    arrayListClone.add(element);
                }
            }
            getAllShowList(arrayListClone);
        }
    }
}

