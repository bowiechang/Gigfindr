package com.example.admin.beerandmusic;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

public class ThirdPartyActivity extends AppCompatActivity implements OnClickListener {

    TextView tvlib1, tvlib2, tvlib3, tvlib4, tvlib5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_party);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Window window = getWindow();
        window.setStatusBarColor(Color.BLACK);


        tvlib1 = findViewById(R.id.tvlib1);
        tvlib3 = findViewById(R.id.tvlib3);
        tvlib2 = findViewById(R.id.tvlib2);
        tvlib4 = findViewById(R.id.tvlib4);
        tvlib5 = findViewById(R.id.tvlib5);

        tvlib1.setOnClickListener(this);
        tvlib2.setOnClickListener(this);
        tvlib3.setOnClickListener(this);
        tvlib4.setOnClickListener(this);
        tvlib5.setOnClickListener(this);



        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(ThirdPartyActivity.this, AboutAppActivity.class);
                ThirdPartyActivity.this.startActivity(myIntent);

            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view == tvlib1){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/in-and-win/IWAHalfModalPresenter"));
            startActivity(browserIntent);
        }
        else if(view == tvlib2){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/yarolegovich/SlidingRootNav"));
            startActivity(browserIntent);
        }
        else if(view == tvlib3){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/ybq/Android-SpinKit"));
            startActivity(browserIntent);
        }
        else if(view == tvlib4){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/yayaa/ParallaxRecyclerView"));
            startActivity(browserIntent);
        }
        else if(view == tvlib5){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/polyak01/IconSwitch"));
            startActivity(browserIntent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ThirdPartyActivity.this, AboutAppActivity.class);
        startActivity(intent);

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
