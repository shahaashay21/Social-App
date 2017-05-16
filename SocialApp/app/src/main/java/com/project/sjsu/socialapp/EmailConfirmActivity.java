package com.project.sjsu.socialapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EmailConfirmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_confirm);

        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent forwardMainActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(forwardMainActivity);
                finish();
            }
        }, 4000);
    }
}
