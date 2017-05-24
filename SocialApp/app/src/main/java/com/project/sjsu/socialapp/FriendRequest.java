package com.project.sjsu.socialapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class FriendRequest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);

        //Generate List

        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();
        ArrayList<String> Requestlist = new ArrayList<String>();

        try{
            JSONArray jsonArray = new JSONArray(bundle.getString("userData"));

            String first_name = null;
            String last_name = null;

            if(jsonArray.length() == 0) {
                Toast.makeText(getApplicationContext(), "No result Found " +
                        "'", Toast.LENGTH_LONG).show();
            }
            for(int i=0; i < jsonArray.length(); i++){
                //JSONObject temp = jsonArray.getJSONObject(i);
                first_name = jsonArray.getJSONObject(i).getString("first_name");
                last_name = jsonArray.getJSONObject(i).getString("last_name");

                Log.d("user_id: ", first_name);
                Requestlist.add(first_name +" " + last_name);

                //searchList.add(user_id);
                //searchList2.add(last_name);
            }

        }catch (JSONException e){
            e.printStackTrace();
        }

        //instantiate custom adapter
        FriendRequestAdapter adapter = new FriendRequestAdapter(Requestlist,this);

        //handle listView and assign adapter
        ListView lView = (ListView)findViewById(R.id.friend_request_list);
        lView.setAdapter(adapter);


    }
}
