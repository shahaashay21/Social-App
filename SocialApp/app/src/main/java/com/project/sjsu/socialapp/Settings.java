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
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hoang on 5/4/2017.
 */

public class Settings  extends Fragment {

    private final String IP = "http://54.183.170.253:3000";
    private final String USER_SETTINGS_INFO_ROUTE = "/user/settings/info";
    private final String USER_PRIVACY_UPDATE_ROUTE = "/user/privacy/update";
    private final String USER_ISEMAIL_UPDATE_ROUTE = "/user/isemail/update";

    private Button mSignOut;
    private SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "SocialApp";
    private Switch isPublic;
    private Switch isEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings, container, false);

        //Shared Preferences
        sharedPreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String idValue = sharedPreferences.getString("userId", null);

        mSignOut = (Button) rootView.findViewById(R.id.signOut);
        mSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userId", null);
                editor.commit();

                Intent forwardLoginIntent = new Intent(getActivity(), LoginActivity.class);
                startActivity(forwardLoginIntent);
                getActivity().finish();
            }
        });



        isPublic = (Switch) rootView.findViewById(R.id.publicSwitch);
        isEmail = (Switch) rootView.findViewById(R.id.emailSwitch);


        isPublic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(isPublic.isPressed()) {
                    final HttpRequest request = new HttpRequest(getContext());
                    Map<String, String> sendPostRequestData = new HashMap<>();
                    if (b) {
                        sendPostRequestData.put("privacy", "1");
                    } else {
                        sendPostRequestData.put("privacy", "0");
                    }
                    sendPostRequestData.put("user_id", idValue);

                    request.sendPostRequest(IP + USER_PRIVACY_UPDATE_ROUTE, sendPostRequestData, new CallbackInterface() {
                        @Override
                        public void onCallBackComplete(String[] response) {
                            if (response[0].toString().equals("success")) {
                                if (response[1].toString().equals("success")) {
                                    Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        isEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(isEmail.isPressed()) {
                    final HttpRequest request = new HttpRequest(getContext());
                    Map<String, String> sendPostRequestData = new HashMap<>();
                    if (b) {
                        sendPostRequestData.put("isEmail", "1");
                    } else {
                        sendPostRequestData.put("isEmail", "0");
                    }
                    sendPostRequestData.put("user_id", idValue);

                    request.sendPostRequest(IP + USER_ISEMAIL_UPDATE_ROUTE, sendPostRequestData, new CallbackInterface() {
                        @Override
                        public void onCallBackComplete(String[] response) {
                            if (response[0].toString().equals("success")) {
                                if (response[1].toString().equals("success")) {
                                    Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


        if(idValue == null){
            Intent forwardLoginIntent = new Intent(getActivity(), LoginActivity.class);
            startActivity(forwardLoginIntent);
            getActivity().finish();
        }else {

            final HttpRequest request = new HttpRequest(getContext());
            Map<String, String> sendPostRequestData = new HashMap<>();
            sendPostRequestData.put("id", idValue);


            request.sendPostRequest(IP+USER_SETTINGS_INFO_ROUTE, sendPostRequestData, new CallbackInterface() {
                @Override
                public void onCallBackComplete(String[] response) {
                    Log.e("Settings", response[1].toString());
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


//        Button mTempButton = (Button) rootView.findViewById(R.id.tempClick);
//        mTempButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(getContext(), UserProfile.class);
//                i.putExtra("user_id", "50");
//                startActivity(i);
//            }
//        });


        return rootView;
    }

    public void setUserData(JSONObject userData) throws JSONException {
        if(userData.getString("privacy").equals("1")){
            isPublic.setChecked(true);
        }else {
            isPublic.setChecked(false);
        }

        if(userData.getString("isEmail").equals("1")){
            isEmail.setChecked(true);
        }else {
            isEmail.setChecked(false);
        }
    }
}

