package com.project.sjsu.socialapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatCallback;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vansh on 5/24/2017.
 */

public class SentRequestAdapter extends BaseAdapter implements ListAdapter {

    private final String IP = "http://54.183.170.253:3000";
    private final String FRIEND_REQUEST_CANCEL = "/friend/request/cancel";
    private final String FRIEND_INFO_ROUTE = "/profile/friend/info";

    private ArrayList<String> list = new ArrayList<String>();
    private Context context;

    public SentRequestAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        Intent intent = ((Activity)context).getIntent();
        final Bundle bundle = intent.getExtras();
        final String user_id = bundle.getString("user_id");
        final String friend_id = bundle.getString("friend_id");
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.activity_sent_request_custom, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.user1_name);
        listItemText.setText(list.get(position));

        //Handle buttons and add onClickListeners
//        Button canBtn = (Button)view.findViewById(R.id.cancel_button);
//        //Button rejectBtn = (Button)view.findViewById(R.id.reject_button);

//        canBtn.setTag(friend_id);


//        canBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Log.d("View ID: ", String.valueOf(v.getTag()));
//            }
//        });


        return view;
    }



}
