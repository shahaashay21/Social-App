package com.project.sjsu.socialapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hoang on 5/4/2017.
 */

public class Friends  extends Fragment {

    private final String IP = "http://54.183.170.253:3000";
    private final String USER_INFO_ROUTE = "/user/info";
    private final String UPDATE_INFO_ROUTE = "/user/info/update";

    private SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "SocialApp";

    EditText mUserName;
    EditText mEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.friends, container, false);

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

        return rootView;
    }
    public void setUserData(JSONObject userData){

        //UserData Variables
        try {

            mUserName.setText(userData.getString("user_name"));
            mEmail.setText(userData.getString("email"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

