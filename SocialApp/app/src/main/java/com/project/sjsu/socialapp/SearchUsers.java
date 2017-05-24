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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Vansh on 5/22/2017.
 */

public class SearchUsers extends AppCompatActivity{

    TextView mSearchText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.searched_friends);

        mSearchText = (TextView) findViewById(R.id.search_text);

        ListView userList = (ListView) findViewById(R.id.userList);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String userData = bundle.getString("JSONArray");
        String search_text = bundle.getString("search");

        mSearchText.setText("You Searched for: ' " + search_text + " '");
        mSearchText.setTextColor(this.getResources().getColor(R.color.colorAccent));



        ArrayList<String> searchList = new ArrayList<>();
       // ArrayList<String> searchList2 = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(bundle.getString("JSONArray"));

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
                searchList.add(first_name +" " + last_name);
                //searchList2.add(last_name);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_expandable_list_item_1, searchList);
        userList.setAdapter(arrayAdapter);


    }
    }
