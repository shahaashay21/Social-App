package com.project.sjsu.socialapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hoang on 5/4/2017.
 */

public class Profiles  extends Fragment {

    private final String IP = "http://54.183.170.253:3000";
    private final String USER_INFO_ROUTE = "/user/info";
    private final String UPDATE_INFO_ROUTE = "/user/info/update";

    private SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "SocialApp";

    EditText mUserName;
    EditText mEmail;
    EditText mCity;
    EditText mState;
    EditText mCountry;
    EditText mProfession;
    EditText mAboutMe;
    EditText mInterests;
    Button mUpdateButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profiles, container, false);


        //Get UI References
        mUserName = (EditText) rootView.findViewById(R.id.eUserName);
        mEmail = (EditText) rootView.findViewById(R.id.etEmailProfiles);
        mCity = (EditText) rootView.findViewById(R.id.etCity);
        mState = (EditText) rootView.findViewById(R.id.etState);
        mCountry = (EditText) rootView.findViewById(R.id.etCountry);
        mProfession = (EditText) rootView.findViewById(R.id.etProfession);
        mAboutMe = (EditText) rootView.findViewById(R.id.etAboutMe);
        mInterests = (EditText) rootView.findViewById(R.id.etInterested);
        mUpdateButton = (Button) rootView.findViewById(R.id.updateButton);


        //Shared Preferences
        sharedPreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        final String idValue = sharedPreferences.getString("userId", null);
        if(idValue == null){
            Intent forwardLoginIntent = new Intent(getActivity(), LoginActivity.class);
            startActivity(forwardLoginIntent);
            getActivity().finish();
        }else {

            final HttpRequest request = new HttpRequest(getContext());
            Map<String, String> sendPostRequestData = new HashMap<>();
            sendPostRequestData.put("id", idValue);


            request.sendPostRequest(IP+USER_INFO_ROUTE, sendPostRequestData, new CallbackInterface() {
                @Override
                public void onCallBackComplete(String[] response) {
                    Log.e("First Response", response[0].toString());
                    Log.e("Second Response", response[1].toString());
                    if(response[0].toString().equals("success")){
                        if(response[1].toString().equals("error")){
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("userId", null);
                            editor.commit();

                            Intent forwardLoginIntent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(forwardLoginIntent);
                            getActivity().finish();
                        }else {
                            try {
                                JSONObject userData = new JSONObject(response[1].toString());
                                setUserData(userData);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }else {
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                };
            });
        }

        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> sendData = new HashMap<String, String>();
                sendData.put("user_id", idValue);
                sendData.put("email", mEmail.getText().toString());
                sendData.put("city", mCity.getText().toString());
                sendData.put("state", mState.getText().toString());
                sendData.put("country", mCountry.getText().toString());
                sendData.put("profession", mProfession.getText().toString());
                sendData.put("about_me", mAboutMe.getText().toString());
                sendData.put("interests", mInterests.getText().toString());

                final HttpRequest request = new HttpRequest(getContext());
                request.sendPostRequest(IP + UPDATE_INFO_ROUTE, sendData, new CallbackInterface() {
                    @Override
                    public void onCallBackComplete(String[] response) {
                        Log.e("Update First Response", response[0].toString());
                        Log.e("Update Second Response", response[1].toString());
                        if(response[0].toString().equals("success")){
                            if(response[1].toString().equals("error")){
                                Toast.makeText(getContext(), "Please try again", Toast.LENGTH_SHORT).show();
                            }else if(response[1].toString().equals("success")){
                                Toast.makeText(getContext(), "Successfully Updated", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        return rootView;
    }

    public void setUserData(JSONObject userData){

        //UserData Variables
        try {
//            String uUserId = userData.getString("user_id");
//            String uUserName = userData.getString("user_name");
//            String uEmail = userData.getString("user_name");
//            String uFirstName = userData.getString("user_name");
//            String uLastName = userData.getString("user_name");
//            String uAvatar = userData.getString("user_name");
//            String uCity = userData.getString("user_name");
//            String uState = userData.getString("user_name");
//            String uCountry = userData.getString("user_name");
//            String uProfession = userData.getString("user_name");
//            String uAboutMe = userData.getString("user_name");
//            String uInterests = userData.getString("user_name");

            mUserName.setText(userData.getString("user_name"));
            mEmail.setText(userData.getString("email"));

            if(userData.getString("city").equals("null") || userData.getString("city").equals("")){
                mCity.setText(userData.getString(""));
            }else {
                mCity.setText(userData.getString("city"));
            }

            if(userData.getString("state").equals("null") || userData.getString("state").equals("")){
                mState.setText(userData.getString(""));
            }else {
                mState.setText(userData.getString("state"));
            }

            if(userData.getString("country").equals("null") || userData.getString("country").equals("")){
                mCountry.setText(userData.getString(""));
            }else {
                mCountry.setText(userData.getString("country"));
            }

            if(userData.getString("profession").equals("null") || userData.getString("profession").equals("")){
                mProfession.setText(userData.getString(""));
            }else {
                mProfession.setText(userData.getString("profession"));
            }


            if(userData.getString("about_me").equals("null") || userData.getString("about_me").equals("")){
                mAboutMe.setText(userData.getString(""));
            }else {
                mAboutMe.setText(userData.getString("about_me"));
            }

            if(userData.getString("interests").equals("null") || userData.getString("interests").equals("")){
                mInterests.setText(userData.getString(""));
            }else {
                mInterests.setText(userData.getString("interests"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

