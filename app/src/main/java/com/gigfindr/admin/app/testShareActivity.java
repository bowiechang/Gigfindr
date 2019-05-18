package com.gigfindr.admin.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;

public class testShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_share);

        Button btnShare = findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "GIGFINDR GIG SHARING");
                    String string = "*GIGFINDR'S GIG SHARING*\n\n" +
                            "Band: The Dylan Movement\n" + "Location: The Halia (34 Cluny Link)\n" + "Time: 8:30pm to 11:30pm" +
                            "\n\nDownload GigFindr for more gigs now at https://play.google.com/store/apps" ;
                    intent.putExtra(Intent.EXTRA_TEXT, string);
                    startActivity(Intent.createChooser(intent, "Share with"));
            }
        });
    }
}
