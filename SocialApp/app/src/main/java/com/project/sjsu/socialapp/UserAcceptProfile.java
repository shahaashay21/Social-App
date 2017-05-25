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
import android.view.View;
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

public class UserAcceptProfile extends AppCompatActivity {

    private final String IP = "http://54.183.170.253:3000";
    private final String USER_INFO_ROUTE = "/user/info";
    private final String FRIEND_INFO_ROUTE = "/profile/friend/info";
    private final String FRIEND_REQUEST_SEND = "/friend/request/send";
    private final String FRIEND_REQUEST_ACCEPT = "/friend/request/accept";
    private final String FRIEND_REQUEST_CANCEL = "/friend/request/cancel";
    private final String FOLLOW_REQUEST = "/friend/follow";
    private final String UNFOLLOW_REQUEST = "/friend/unfollow";

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
        setContentView(R.layout.activity_poi);

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
        final String intent_User_id = i.getStringExtra("user_id");

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


        mAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String addFriendString = (String) mAddFriend.getText();
                if(addFriendString.equals("Accept")){
                    friendRequestHttpRequest(FRIEND_REQUEST_ACCEPT, idValue, intent_User_id);
                }else if(addFriendString.equals("Request Sent") || addFriendString.equals("Friends")){
                    friendRequestHttpRequest(FRIEND_REQUEST_CANCEL, idValue, intent_User_id);
                }
            }
        });

        mFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String addFollowString = (String) mFollow.getText();
                if(addFollowString.equals("Reject")){
                    friendRequestHttpRequest(FRIEND_REQUEST_CANCEL, idValue, intent_User_id);
                }else if(addFollowString.equals("Follow")){
                    friendRequestHttpRequest(FOLLOW_REQUEST, idValue, intent_User_id);
                }
            }
        });

        getFriendInformation(idValue, intent_User_id);


    }

    public void friendRequestHttpRequest(String urlPath, final String user_id, final String friend_id){
        final HttpRequest request = new HttpRequest(getApplicationContext());
        Map<String, String> sendPostRequestFriendData = new HashMap<>();
        sendPostRequestFriendData.put("user_id", user_id);
        sendPostRequestFriendData.put("friend_id", friend_id);

        request.sendPostRequest(IP + urlPath, sendPostRequestFriendData, new CallbackInterface() {
            @Override
            public void onCallBackComplete(String[] response) {
                if(response[0].toString().equals("success")){
                    if(response[1].toString().equals("success")){
                        getFriendInformation(user_id, friend_id);
                    }else {
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getFriendInformation(String user_id, String friend_id){
        final HttpRequest request = new HttpRequest(getApplicationContext());
        Map<String, String> sendPostRequestFriendData = new HashMap<>();
        sendPostRequestFriendData.put("user_id", user_id);
        sendPostRequestFriendData.put("friend_id", friend_id);

        request.sendPostRequest(IP + FRIEND_INFO_ROUTE, sendPostRequestFriendData, new CallbackInterface() {
            @Override
            public void onCallBackComplete(String[] response) {
                if(response[0].toString().equals("success")){
                    if(response[1].toString().equals("no friend")){
                    }else {
                        try {
                            JSONObject userData = new JSONObject(response[1].toString());
                            setFriendButtonData(userData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    public void setFriendButtonData(JSONObject userInfo) throws JSONException {
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(userInfo.getString("request").equals("null") || userInfo.getString("request").equals("")){
        }else{
            if(userInfo.getString("request").equals("1")){
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    mAddFriend.setBackgroundDrawable( getResources().getDrawable(R.drawable.button_submit_rounded));
                    mFollow.setBackgroundDrawable( getResources().getDrawable(R.drawable.button_danger_rounded));
                } else {
                    mAddFriend.setBackground( getResources().getDrawable(R.drawable.button_submit_rounded));
                    mFollow.setBackgroundDrawable( getResources().getDrawable(R.drawable.button_danger_rounded));
                }
                mAddFriend.setText("Accept");
                mFollow.setText("Reject");
            }else if(userInfo.getString("request").equals("2")){
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    mAddFriend.setBackgroundDrawable( getResources().getDrawable(R.drawable.button_blue_rounded));
                } else {
                    mAddFriend.setBackground( getResources().getDrawable(R.drawable.button_blue_rounded));
                }
                mAddFriend.setText("Friends");
                mFollow.setText("Accepted");
            }else if(userInfo.getString("request").equals("0")){
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    mAddFriend.setBackgroundDrawable( getResources().getDrawable(R.drawable.button_submit_rounded));
                } else {
                    mAddFriend.setBackground( getResources().getDrawable(R.drawable.button_submit_rounded));
                }
                mAddFriend.setText("Add Friend");
                mFollow.setText("Follow");
                mFollow.setBackgroundDrawable( getResources().getDrawable(R.drawable.button_submit_rounded));

            }
        }

        if(userInfo.getString("follow").equals("null") || userInfo.getString("follow").equals("")){
        }else {
            if(userInfo.getString("follow").equals("1")){
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                   // mFollow.setBackgroundDrawable( getResources().getDrawable(R.drawable.button_danger_rounded));
                } else {
                    //mFollow.setBackground( getResources().getDrawable(R.drawable.button_danger_rounded));
                }
                //mFollow.setText("Following");
            }else if(userInfo.getString("follow").equals("0")){
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                   // mFollow.setBackgroundDrawable( getResources().getDrawable(R.drawable.button_submit_rounded));
                } else {
                   // mFollow.setBackground( getResources().getDrawable(R.drawable.button_submit_rounded));
                }
                //mFollow.setText("Follow");
            }
        }

    }

    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }
}
