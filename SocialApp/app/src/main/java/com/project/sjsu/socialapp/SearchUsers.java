package com.project.sjsu.socialapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.Console;
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
        final Bundle bundle = intent.getExtras();
        final String userData = bundle.getString("JSONArray");
        String search_text = bundle.getString("search");

        mSearchText.setText("You Searched for: ' " + search_text + " '");
        mSearchText.setTextColor(this.getResources().getColor(R.color.colorAccent));



        final ArrayList<String> searchList = new ArrayList<>();
        final ArrayList<String> searchId = new ArrayList<>();
       // ArrayList<String> searchList2 = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(bundle.getString("JSONArray"));

            String first_name = null;
            String last_name = null;
            String user_id = null;
            if(jsonArray.length() == 0) {
                Toast.makeText(getApplicationContext(), "No result Found " +
                        "'", Toast.LENGTH_LONG).show();
            }
            for(int i=0; i < jsonArray.length(); i++){
                //JSONObject temp = jsonArray.getJSONObject(i);
                first_name = jsonArray.getJSONObject(i).getString("first_name");
                last_name = jsonArray.getJSONObject(i).getString("last_name");
                user_id = jsonArray.getJSONObject(i).getString("user_id");
                Log.d("user id is: ", user_id);
                Log.d("user_id: ", first_name);
                searchList.add(first_name +" " + last_name);
                searchId.add(user_id);
                //searchList.add(user_id);
                //searchList2.add(last_name);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(SearchUsers.this,android.R.layout.simple_expandable_list_item_1, searchList);
        userList.setAdapter(arrayAdapter);

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String SelectedItem = searchList.get(position);
                String SelectedId = searchId.get(position);

               Intent profileIntent = new Intent(getApplicationContext(), UserProfile.class);
                  profileIntent.putExtra("user_id", SelectedId);
                Log.d("Data sent ahead are: ",SelectedId);
                //Log.d("User id passed is: ", user_id);
                startActivity(profileIntent);
                //Toast.makeText(getApplicationContext(), SelectedItem, Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), SelectedId, Toast.LENGTH_SHORT).show();

            }
        });


    }
}
