package com.project.sjsu.socialapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Vansh on 5/22/2017.
 */

public class SearchUsers extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.searched_friends);

        ListView userList = (ListView) findViewById(R.id.userList);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String userData = bundle.getString("JSONArray");

        ArrayList<String> searchList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(bundle.getString("JSONArray"));

            String first_name = null;
            for(int i=0; i < jsonArray.length(); i++){
                //JSONObject temp = jsonArray.getJSONObject(i);
                first_name = jsonArray.getJSONObject(i).getString("first_name");
                Log.d("user_id: ", first_name);
                searchList.add(first_name);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,searchList);
        userList.setAdapter(arrayAdapter);


    }
    }
