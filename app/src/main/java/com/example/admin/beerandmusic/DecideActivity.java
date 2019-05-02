package com.example.admin.beerandmusic;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

public class DecideActivity extends AppCompatActivity implements OnClickListener {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decide);

        ConstraintLayout findShow = findViewById(R.id.layoutFindShow);
        ConstraintLayout postShow = findViewById(R.id.layoutPostShow);

        relativeLayout = findViewById(R.id.spinnerkitloader);
        relativeLayout.setVisibility(View.GONE);

        findShow.setOnClickListener(this);
        postShow.setOnClickListener(this);

        Window window = getWindow();
        window.setStatusBarColor(Color.BLACK);

        ImageView imageView = findViewById(R.id.imageview_logo); //Declare imageview logo
        Glide.with(this).load(R.drawable.yourlogo).into(imageView); //load image with glide

        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(imageView, "alpha",  1f, 0);
        fadeOut.setDuration(300);
        fadeOut.start();

    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.layoutFindShow:

                relativeLayout.setVisibility(View.VISIBLE);

                final Handler handler = new Handler();
                new Thread(new Runnable() {
                    public void run() {
                        try{
                            Thread.sleep(1);
                        }
                        catch (Exception e) { } // Just catch the InterruptedException

                        // Now we use the Handler to post back to the main thread
                        handler.post(new Runnable() {
                            public void run() {
                                // Set the View's visibility back on the main UI Thread

                                Intent intent = new Intent(DecideActivity.this, MapActivity.class);
                                startActivity(intent);

                            }
                        });
                    }
                }).start();

                break;

            case R.id.layoutPostShow:
                Intent intent2;
                if(firebaseAuth.getCurrentUser() != null){


                    intent2 = new Intent(this, PostShowActivity.class);
                    startActivity(intent2);
                }
                else{

                    intent2 = new Intent(this, SignInSignUpActivity.class);
                    startActivity(intent2);
                }
                break;
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
