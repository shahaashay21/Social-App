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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

public class PrivateMessage extends AppCompatActivity {

    TextView mName;
    TextView mSubject;
    TextView mBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.private_message);

        mName = (TextView) findViewById(R.id.user_name_view);
        mSubject = (TextView) findViewById(R.id.message_subject);
        mBody  = (TextView) findViewById(R.id.message_body);


        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();
        //final String messageData = bundle.getString("JSONArray");
        String array_id = bundle.getString("array_id");
        Integer i = Integer.valueOf(array_id);

        try {
            JSONArray jsonArray = new JSONArray(bundle.getString("JSONArray"));


            String first_name = null;
            String last_name = null;
            String subject = null;
            String body = null;

            if(jsonArray.length() == 0) {
                Toast.makeText(getApplicationContext(), "No result Found " +
                        "'", Toast.LENGTH_LONG).show();
            }

                //JSONObject temp = jsonArray.getJSONObject(i);
                first_name = jsonArray.getJSONObject(i).getString("first_name");
                last_name = jsonArray.getJSONObject(i).getString("last_name");
                body = jsonArray.getJSONObject(i).getString("body");
                subject = jsonArray.getJSONObject(i).getString("subject");
                Log.d("user id is: ", subject);
                Log.d("user_id: ", first_name);

            mName.setText(first_name+" "+last_name);
            mName.setTextColor(this.getResources().getColor(R.color.colorAccent));
            mSubject.setText(subject);
            mSubject.setTextColor(this.getResources().getColor(R.color.colorAccent));
            mBody.setText(body);
            mBody.setTextColor(this.getResources().getColor(R.color.colorAccent));



        } catch (JSONException e) {
            e.printStackTrace();
        }







    }
}
