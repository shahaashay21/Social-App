package com.project.sjsu.socialapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class UserProfile extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "SocialApp";
    private boolean own_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String idValue = sharedPreferences.getString("userId", null);

        Intent i = getIntent();
        String intent_User_id = i.getStringExtra("user_id");

        if(idValue.equals(intent_User_id)){
            own_user = true;
        }else {
            own_user = false;
        }


    }
}
