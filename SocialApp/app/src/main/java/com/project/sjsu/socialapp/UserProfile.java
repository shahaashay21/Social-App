package com.project.sjsu.socialapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class UserProfile extends AppCompatActivity {

    private final String IP = "http://54.183.170.253:3000";
    private final String USER_INFO_ROUTE = "/user/info";

    private SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "SocialApp";
    private boolean own_user;

    private ImageView mAvatar;

    private Button mAddFriend;
    private Button mFollow;

    private TextView mFirstLastName;
    private TextView mUserName;
    private TextView mEmail;
    private TextView mAddress;
    private TextView mProfession;
    private TextView mAboutMe;
    private TextView mInterests;

    private LinearLayout mUsernameLayout;
    private LinearLayout mEmailLayout;
    private LinearLayout mAddressLayout;
    private LinearLayout mProfessionLayout;
    private LinearLayout mAboutMeLayout;
    private LinearLayout mInterestsLayout;

    private LinearLayout linearLayoutOwn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mAvatar = (ImageView) findViewById(R.id.avatar);

        mAddFriend = (Button) findViewById(R.id.addFriend);
        mFollow = (Button) findViewById(R.id.follow);

        mFirstLastName = (TextView) findViewById(R.id.profile_first_last_name);
        mUserName = (TextView) findViewById(R.id.profile_user_name);
        mEmail = (TextView) findViewById(R.id.profile_email);
        mAddress = (TextView) findViewById(R.id.profile_address);
        mProfession = (TextView) findViewById(R.id.profile_profession);
        mAboutMe = (TextView) findViewById(R.id.profile_about);
        mInterests = (TextView) findViewById(R.id.profile_interests);

        mUsernameLayout = (LinearLayout) findViewById(R.id.linearLayout_profile_user_name);
        mEmailLayout = (LinearLayout) findViewById(R.id.linearLayout_profile_email);
        mAddressLayout = (LinearLayout) findViewById(R.id.linearLayout_profile_address);
        mProfessionLayout = (LinearLayout) findViewById(R.id.linearLayout_profile_profession);
        mAboutMeLayout = (LinearLayout) findViewById(R.id.linearLayout_profile_about_me);
        mInterestsLayout = (LinearLayout) findViewById(R.id.linearLayout_profile_interests);

        linearLayoutOwn = (LinearLayout) findViewById(R.id.linearLayout_own);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String idValue = sharedPreferences.getString("userId", null);

        Intent i = getIntent();
        String intent_User_id = i.getStringExtra("user_id");

        if(idValue.equals(intent_User_id)){
            own_user = true;
        }else {
            own_user = false;
        }


        final HttpRequest request = new HttpRequest(getApplicationContext());
        Map<String, String> sendPostRequestData = new HashMap<>();
        sendPostRequestData.put("id", intent_User_id);


        request.sendPostRequest(IP+USER_INFO_ROUTE, sendPostRequestData, new CallbackInterface() {
            @Override
            public void onCallBackComplete(String[] response) {
                if(response[0].toString().equals("success")){
                    if(response[1].toString().equals("error")){
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("userId", null);
                        editor.commit();

                        Intent forwardLoginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(forwardLoginIntent);
                        finish();
                    }else {
                        try {
                            JSONObject userData = new JSONObject(response[1].toString());
                            setUserData(userData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            };
        });

        if(own_user){
            linearLayoutOwn.setVisibility(LinearLayout.GONE);
        }




    }

    public void setUserData(JSONObject userInfo) throws JSONException {
        mFirstLastName.setText(capitalize(userInfo.getString("first_name"))+" "+capitalize(userInfo.getString("last_name")));
        mEmail.setText(userInfo.getString("email"));
        mUserName.setText(userInfo.getString("user_name"));


        if((userInfo.getString("city").equals("null") || userInfo.getString("city").equals("")) && (userInfo.getString("country").equals("null") || userInfo.getString("country").equals("")) && (userInfo.getString("state").equals("null") || userInfo.getString("state").equals(""))){
            mAddressLayout.setVisibility(LinearLayout.GONE);
        }else {
            mAddress.setText(capitalize(userInfo.getString("city"))+", "+capitalize(userInfo.getString("state"))+", "+capitalize(userInfo.getString("country")));
        }

        if(userInfo.getString("profession").equals("null") || userInfo.getString("profession").equals("")){
            mProfessionLayout.setVisibility(LinearLayout.GONE);
        }else {
            mProfession.setText(capitalize(userInfo.getString("profession")));
        }


        if(userInfo.getString("about_me").equals("null") || userInfo.getString("about_me").equals("")){
            mAboutMeLayout.setVisibility(LinearLayout.GONE);
        }else {
            mAboutMe.setText(userInfo.getString("about_me"));
        }

        if(userInfo.getString("interests").equals("null") || userInfo.getString("interests").equals("")){
            mInterestsLayout.setVisibility(LinearLayout.GONE);
        }else {
            mInterests.setText(userInfo.getString("interests"));
        }

        if(userInfo.getString("avatar").equals("null") || userInfo.getString("avatar").equals("")){
//            mAvatar.setBackground(getDrawable(R.drawable.default_user));
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                mAvatar.setBackgroundDrawable( getResources().getDrawable(R.drawable.default_user) );
            } else {
                mAvatar.setBackground( getResources().getDrawable(R.drawable.default_user));
            }
        }else {
            ShowImage show = new ShowImage(mAvatar, userInfo.getString("avatar"));
            show.execute();
        }
    }

    private class ShowImage extends AsyncTask<String, Void, Void> {
        ImageView image;
        String url = null;
        Bitmap bmp = null;
        public ShowImage(ImageView image, String url){
            this.image = image;
            this.url = url;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            image.setImageBitmap(bmp);
        }

        @Override
        protected Void doInBackground(String... strings) {
            URL urla = null;
            try {
                urla = new URL(url);
                bmp = BitmapFactory.decodeStream(urla.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }
}
