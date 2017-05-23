package com.project.sjsu.socialapp;

import android.database.Cursor;
import android.support.v4.app.Fragment;
//import android.app.Fragment;
//import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vansh on 5/22/2017.
 */

public class Friends extends Fragment {
    private final String IP = "http://54.183.170.253:3000";
    private final String USER_INFO_ROUTE = "/user/info";
    private final String UPDATE_INFO_ROUTE = "/user/info/update";
    private final String USER_SEARCH = "/friend/search";
    private final String GET_USERS = "/friend/search";
    private SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "SocialApp";

    EditText mUserName;
    EditText mEmail;
    EditText mSearchUser;
    ImageButton mSearchButton;
    Button mMyRequest;
    Button mSentRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.friends, container, false);


        //Get UI Reference

        mSearchUser = (EditText) rootView.findViewById(R.id.search_user);
        mSearchButton = (ImageButton) rootView.findViewById(R.id.search_button);
        mMyRequest = (Button) rootView.findViewById(R.id.my_request);
        mSentRequest = (Button) rootView.findViewById(R.id.sent_request);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
//                Intent forwardSearchIntent = new Intent(getActivity(), SearchUsers.class);
//                startActivity(forwardSearchIntent);
                attemptSearch();
            }
        });
        return rootView;
    }

        public void attemptSearch(){
            // Store values at the time of the login attempt.
            String search = mSearchUser.getText().toString();
            HttpRequest request = new HttpRequest(getContext());
            Map<String, String> sendSearchData = new HashMap<>();
            sendSearchData.put("search", mSearchUser.getText().toString());
            request.sendPostRequest(IP + USER_SEARCH, sendSearchData, new CallbackInterface() {
                @Override
                public void onCallBackComplete(String[] response) {

                        Log.e("RESPONSE IS?", response[0]);
                        Log.e("RESPONSE OF 1 IS?", response[1].toString());
                        if(response[0].toString().equals("success")){


                            if(response[1].toString().equals("error")){
                                Toast.makeText(getContext(), "Please try again", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getContext(), "Sucessfully Searched" +
                                        "", Toast.LENGTH_SHORT).show();
                                Intent searchIntent = new Intent(getActivity(), SearchUsers.class);
                                startActivity(searchIntent);
                            }

                        }else {
                            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }

                    }


            });

//            Intent forwardSearchIntent = new Intent(getActivity(), SearchUsers.class);
//                startActivity(forwardSearchIntent);
    }
}
