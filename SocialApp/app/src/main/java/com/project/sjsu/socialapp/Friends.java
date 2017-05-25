package com.project.sjsu.socialapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.Fragment;
//import android.app.Fragment;
//import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vansh on 5/22/2017.
 */

public class Friends extends Fragment {
    private final String IP = "http://54.183.170.253:3000";
    private final String USER_FRIEND_REQUEST = "/friend/request/get";
    private final String USER_SENT_REQUEST = "/friend/request/sent";
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
    String USER_ID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.friends, container, false);



        sharedPreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        final String user_id = sharedPreferences.getString("userId", null);
        USER_ID = user_id;

        //Get UI Reference

        mSearchUser = (EditText) rootView.findViewById(R.id.search_user);
        mSearchButton = (ImageButton) rootView.findViewById(R.id.search_button);
        mMyRequest = (Button) rootView.findViewById(R.id.my_request);
        mSentRequest = (Button) rootView.findViewById(R.id.sent_request);
        mSearchButton.setEnabled(false);


        mMyRequest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){
                attemtMyRequest();

            }

        });

        mSentRequest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){
//                Intent forwardSentRequest = new Intent(getActivity(), SentRequest.class);
//                startActivity(forwardSentRequest);
//
                attemptSentRequest();

            }

        });


        mSearchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")){
                    mSearchButton.setEnabled(false);
//                    Toast.makeText(getContext(), "Please Type something" +
//                            "", Toast.LENGTH_SHORT).show();

                }else {
                    mSearchButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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




    public void attemptSentRequest(){
        if(USER_ID == null){
            Intent forwardLoginIntent = new Intent(getActivity(), LoginActivity.class);
            startActivity(forwardLoginIntent);
            getActivity().finish();
        }else{
            final HttpRequest request = new HttpRequest(getContext());
            Map<String, String> sendPostRequestData = new HashMap<>();
            sendPostRequestData.put("user_id", USER_ID);

            request.sendPostRequest(IP + USER_SENT_REQUEST, sendPostRequestData, new CallbackInterface() {
                @Override
                public void onCallBackComplete(String[] response) {

                    Log.e("First Response", response[0].toString());
                    Log.e("Second Response", response[1].toString());
                    if(response[0].toString().equals("success")){
                        if(response[0].toString().equals("error")){
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("userId", null);
                            editor.commit();

                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }else{

                            //JSONArray userData = new JSONArray(response[1].toString());
                            Intent i = new Intent(getActivity(), SentRequest.class);
                            i.putExtra("userData", response[1].toString() );
                            startActivity(i);
                        }
                    }


                }
            });

        }


    }

    public void attemtMyRequest(){


        if(USER_ID == null){
            Intent forwardLoginIntent = new Intent(getActivity(), LoginActivity.class);
            startActivity(forwardLoginIntent);
            getActivity().finish();
        }else{
            final HttpRequest request = new HttpRequest(getContext());
            Map<String, String> sendPostRequestData = new HashMap<>();
            sendPostRequestData.put("user_id", USER_ID);

            request.sendPostRequest(IP + USER_FRIEND_REQUEST, sendPostRequestData, new CallbackInterface() {
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
                        }else{

                            //JSONArray userData = new JSONArray(response[1].toString());
                            Intent i = new Intent(getActivity(), FriendRequest.class);
                            i.putExtra("userData", response[1].toString() );
                            startActivity(i);

                        }
                    }
                }
            });
        }
    }
    public void attemptSearch(){
        // Store values at the time of the login attempt.
        final String search = mSearchUser.getText().toString();
        HttpRequest request = new HttpRequest(getContext());
        final Map<String, String> sendSearchData = new HashMap<>();
        sendSearchData.put("search", mSearchUser.getText().toString());
        request.sendPostRequest(IP + USER_SEARCH, sendSearchData, new CallbackInterface() {
            @Override
            public void onCallBackComplete(String[] response) {

                Log.e("RESPONSE IS:", response[0]);
                Log.e("Response Length:", String.valueOf(response.length));
                Log.d("Debug Reponse[1]:", response[1].toString());

                if(response[0].toString().equals("success")){

                    if(response[1].toString().equals("error")){
                        Toast.makeText(getContext(), "Please try again", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getContext(), "Searched: ' " + search +
                                " '", Toast.LENGTH_SHORT).show();
                        Intent searchIntent = new Intent(getActivity(), SearchUsers.class);

                        String user_id = null;
                        try {

                            //JSONObject object = new JSONObject(response[1].toString());
                            //JSONArray jsonArray = new JSONArray(object.getJSONArray("userData"));
                            JSONArray jsonArray = new JSONArray(response[1].toString());

                            Log.d("JSONArray Length: ", String.valueOf(jsonArray.length()));
                            searchIntent.putExtra("JSONArray", response[1].toString());
                            searchIntent.putExtra("search", search);
                                    /*for(int i=0; i<jsonArray.length(); i++){
                                        JSONObject temp = jsonArray.getJSONObject(i);
                                        user_id = temp.getString("user_id");
                                        Log.d("user_id: ", user_id);
                                        searchIntent.putExtra("user_id", temp.getJSONObject("user_id").toString());

                                    }*/

                                    /*user_id = object.getString("user_id");
                                    Log.d("ANS:", user_id);*/

                            //searchIntent.putExtra("USER_DATA", json_object.toString());
                                    /*searchIntent.putExtra("user_id", object.getJSONObject("user_id").toString());
                                    searchIntent.putExtra("user_name", object.getJSONObject("user_name").toString());
                                    searchIntent.putExtra("email", object.getJSONObject("email").toString());
                                    searchIntent.putExtra("avatar", object.getJSONObject("avatar").toString());
                                    searchIntent.putExtra("city", object.getJSONObject("city").toString());
                                    searchIntent.putExtra("state", object.getJSONObject("state").toString());
                                    searchIntent.putExtra("country", object.getJSONObject("country").toString());
                                    searchIntent.putExtra("profession", object.getJSONObject("profession").toString());
                                    searchIntent.putExtra("about_me", object.getJSONObject("about_me").toString());
                                    searchIntent.putExtra("first_name", object.getJSONObject("first_name").toString());
                                    searchIntent.putExtra("last_name", object.getJSONObject("last_name").toString());*/



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //searchIntent.putStringArrayListExtra("userDat", jsonObj.toStringArrayListExtra);
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
