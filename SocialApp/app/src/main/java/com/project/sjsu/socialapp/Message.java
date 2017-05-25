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

import java.util.HashMap;
import java.util.Map;


public class Message extends AppCompatActivity {

    private final String IP = "http://54.183.170.253:3000";
    private final String MESSAGE_SEND = "/message/send";
    private SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "SocialApp";

    private Button mSend;
    private Button mCancel;
    EditText mRecipient;
    EditText mSubject;
    EditText mBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        //Shared Preferences
        sharedPreferences = getApplicationContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String user_id = sharedPreferences.getString("userId", null);


        mRecipient = (EditText) findViewById(R.id.message_user);
        mSubject = (EditText) findViewById(R.id.message_subject);
        mBody = (EditText) findViewById(R.id.message_body);

        mSend = (Button) findViewById(R.id.send_button);
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final HttpRequest request = new HttpRequest(getApplicationContext());
                Map<String, String> sendData = new HashMap<String, String>();
                sendData.put("user_id", user_id);
                sendData.put("post_image", mRecipient.getText().toString());
                sendData.put("user_id", mSubject.getText().toString());
                sendData.put("feed_text", mBody.getText().toString());

                request.sendPostRequest(IP + MESSAGE_SEND, sendData, new CallbackInterface() {
                    @Override
                    public void onCallBackComplete(String[] response) {
                        Log.e("Update First Respo", response[0].toString());
                        if(response[0].toString().equals("success"))
                        {
                            Toast.makeText(getApplicationContext(), "Your message has been sent", Toast.LENGTH_SHORT).show();
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
