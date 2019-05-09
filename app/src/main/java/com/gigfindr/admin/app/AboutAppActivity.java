package com.gigfindr.admin.app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AboutAppActivity extends AppCompatActivity implements OnClickListener {

    TextView tvPP, tvTC, tvTPS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(AboutAppActivity.this, MapActivity.class);
                AboutAppActivity.this.startActivity(myIntent);

            }
        });

        tvPP = findViewById(R.id.tvPP);
        tvTC = findViewById(R.id.tvTC);
        tvTPS = findViewById(R.id.tvTPS);

        tvPP.setOnClickListener(this);
        tvTC.setOnClickListener(this);
        tvTPS.setOnClickListener(this);


        Window window = getWindow();
        window.setStatusBarColor(Color.BLACK);


    }

    @Override
    public void onClick(View view) {

        if(view == tvPP){
            Intent myIntent = new Intent(AboutAppActivity.this, PrivacyPolicyActivity.class);
            AboutAppActivity.this.startActivity(myIntent);
        }
        else if(view == tvTC){
            Intent myIntent = new Intent(AboutAppActivity.this, TermsAndConditionActivity.class);
            AboutAppActivity.this.startActivity(myIntent);
        }
        else if(view == tvTPS){
            Intent myIntent = new Intent(AboutAppActivity.this, ThirdPartyActivity.class);
            AboutAppActivity.this.startActivity(myIntent);
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
        Intent intent = new Intent(AboutAppActivity.this, MapActivity.class);
        startActivity(intent);

    }
}
