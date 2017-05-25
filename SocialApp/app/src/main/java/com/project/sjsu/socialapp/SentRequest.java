package com.project.sjsu.socialapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class SentRequest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_request);

        ArrayList<String> newCustomList = new ArrayList<>();

        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();
        final ArrayList<String> SentRequestlist = new ArrayList<String>();
        final ArrayList<String> SentId = new ArrayList<>();

        try{
            JSONArray jsonArray = new JSONArray(bundle.getString("userData"));

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

                Log.d("user_id: ", first_name);
                SentRequestlist.add(first_name +" " + last_name);
                SentId.add(user_id);

                //searchList.add(user_id);
                //searchList2.add(last_name);
            }

        }catch (JSONException e){
            e.printStackTrace();
        }


        //instantiate custom adapter
        SentRequestAdapter adapter = new SentRequestAdapter(SentRequestlist,this);



        //handle listView and assign adapter
        ListView lView = (ListView)findViewById(R.id.sent_request_list);
        lView.setAdapter(adapter);

        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                String selectedId = SentId.get(position);

                Intent profileIntent = new Intent(getApplicationContext(), UserProfile.class);
                profileIntent.putExtra("user_id", selectedId);
                Log.d("Data sent ahead are: ",selectedId);
                //Log.d("User id passed is: ", user_id);
                startActivity(profileIntent);
                //Toast.makeText(getApplicationContext(), SelectedItem, Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), selectedId, Toast.LENGTH_SHORT).show();

            }
        });


    }
}
