//package com.project.sjsu.socialapp;
//
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
///**
// * Created by Hoang on 5/4/2017.
// */
//
//public class Message  extends Fragment {
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.message, container, false);
//
//        return rootView;
//    }
//}

package com.project.sjsu.socialapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Message extends AppCompatActivity {

    private final String IP = "http://54.183.170.253:3000";
    private final String MESSAGE_SEND = "/message/send";
    private final String CHECK_EMAIL = "/message/email";
    private SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "SocialApp";

    private Button mSend, mCheck;
    private Button mCancel;
    EditText mRecipient;
    EditText mSubject;
    EditText mBody;
    String USER_ID;
    String rec_id, first_name, last_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        sharedPreferences = this.getApplicationContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        final String user_id = sharedPreferences.getString("userId", null);
        USER_ID = user_id;

        mRecipient = (EditText) findViewById(R.id.message_user);
        mSubject = (EditText) findViewById(R.id.message_subject);
        mBody = (EditText) findViewById(R.id.message_body);

        mSend = (Button) findViewById(R.id.send_button);
        mCancel = (Button) findViewById(R.id.cancel_button);
        mSend.setEnabled(false);
       // mSend.setBackgroundDrawable( getResources().getDrawable(R.drawable.button_danger_rounded));
        mCheck = (Button) findViewById(R.id.check_email);

        mCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String search = mRecipient.getText().toString();
                HttpRequest request = new HttpRequest(getApplicationContext());
                final Map<String, String> sendSearchData = new HashMap<>();
                sendSearchData.put("search", mRecipient.getText().toString());

                request.sendPostRequest(IP + CHECK_EMAIL, sendSearchData, new CallbackInterface() {
                    @Override
                    public void onCallBackComplete(String[] response) {
//                        Log.e("RESPONSE IS:", response[0]);
//                        Log.e("Response Length:", String.valueOf(response.length));
//                        Log.d("Debug Reponse[1]:", response[1].toString());

                        if(response[0].toString().equals("success")){
                            if(response[1].toString().equals("error")){
                                Toast.makeText(getApplicationContext(), "Enter a valid entry", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(), "Correct Email Id: ' " + search +
                                        " '", Toast.LENGTH_SHORT).show();
                                //Intent searchIntent = new Intent(getActivity(), SearchUsers.class);
                                mSend.setEnabled(true);

                                mSend.setBackgroundDrawable( getResources().getDrawable(R.drawable.button_submit_rounded));
                                mSend.setText("Send");

                                String user_id = null;
                                try {

                                    //JSONObject object = new JSONObject(response[1].toString());
                                    //JSONArray jsonArray = new JSONArray(object.getJSONArray("userData"));
//                                    JSONArray jsonArray = new JSONArray(response[1].toString());
//                                    String rec_id = jsonArray.getString(0);

                                    JSONObject userData = new JSONObject(response[1].toString());

                                     rec_id = userData.getString("user_id");
                                     first_name = userData.getString("first_name");
                                     last_name = userData.getString("last_name");
                                    Log.e("Rec id", rec_id);



//                                    Log.e("JSONArray Length: ", String.valueOf(jsonArray.length()));
//                                    Log.e("Receipent Id", rec_id);


//                                    searchIntent.putExtra("JSONArray", response[1].toString());
//                                    searchIntent.putExtra("search", search);
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
                            }

                        }else{
                            Toast.makeText(getApplicationContext(), "Invalid Email Id", Toast.LENGTH_SHORT).show();
                            mSend.setEnabled(false);
                        }

                    }
                });

            }
        });


        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final HttpRequest request = new HttpRequest(getApplicationContext());
                Map<String, String> sendData = new HashMap<String, String>();
                sendData.put("user_id", user_id);
                sendData.put("rec_id", rec_id);
                sendData.put("subject", mSubject.getText().toString());
                sendData.put("body", mBody.getText().toString());
                sendData.put("first_name", first_name);
                sendData.put("last_name", last_name);
				// Set connection to database
                request.sendPostRequest(IP + MESSAGE_SEND, sendData, new CallbackInterface() {
                    @Override
                    public void onCallBackComplete(String[] response) {
                        Log.e("Update First Respo", response[0].toString());
                        if(response[0].toString().equals("success"))
                        {

                            Toast.makeText(getApplicationContext(), "Your message has been sent", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Your message failed to send", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });



    }


}
