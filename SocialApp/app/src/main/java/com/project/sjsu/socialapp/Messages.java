package com.project.sjsu.socialapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hoang on 5/4/2017.
 */

public class Messages extends Fragment {

    private final String IP = "http://54.183.170.253:3000";
    //this route will change oonce we create it in node
    //private final String MESSAGE_SEARCH = "/friend/search";
    private final String MESSAGE_SEARCH = "/message/search";
    private SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "SocialApp";

    TextView mMessageText;
    String USER_ID;
    JSONArray jsonArray;
    ListView messageList;
    final ArrayList<String> searchList = new ArrayList<>();
    final ArrayList<String> searchId = new ArrayList<>();
    String myJSONArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.message_list, container, false);

        sharedPreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        final String user_id = sharedPreferences.getString("userId", null);
        USER_ID = user_id;

        mMessageText = (TextView) rootView.findViewById(R.id.message_text);
        messageList = (ListView) rootView.findViewById(R.id.messageList);

        //intent didnt bring us here so we need to do post here//
//        Intent intent = getIntent();
//        final Bundle bundle = intent.getExtras();
//        final String userData = bundle.getString("JSONArray");
//        String search_text = bundle.getString("search");

        mMessageText.setText("Your Current Messages");
        mMessageText.setTextColor(this.getResources().getColor(R.color.colorAccent));

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            /*---------------First and foremost we need to create a post request to get back a JSONArray---------------------*/
            // Store values at the time of the login attempt.
            //final String search = mSearchUser.getText().toString();
            HttpRequest request = new HttpRequest(getContext());
            final Map<String, String> sendMessageReq = new HashMap<>();
            Log.e("User Id is:", USER_ID);
            sendMessageReq.put("user_id", USER_ID);
            //sendMessageReq.put("messages", "messages");
            request.sendPostRequest(IP + MESSAGE_SEARCH, sendMessageReq, new CallbackInterface() {
                @Override
                public void onCallBackComplete(String[] response) {

                    Log.e("MESSAGE RESPONSE IS:", response[0]);
                    Log.e("Messg Response Length:", String.valueOf(response.length));
                    Log.d("Debug Reponse[1]:", response[1].toString());

                    if(response[0].toString().equals("success"))
                    {

                        if(response[1].toString().equals("error"))
                        {
                            Toast.makeText(getContext(), "Please try again", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getContext(), "Message List", Toast.LENGTH_SHORT).show();
                            //dont need intent yet
                            //Intent searchIntent = new Intent(getActivity(), SearchUsers.class);

                            //String user_id = null;
                            try {

                                //JSONObject object = new JSONObject(response[1].toString());
                                //JSONArray jsonArray = new JSONArray(object.getJSONArray("userData"));

                                //we will use this JSON Array
                                jsonArray = new JSONArray(response[1].toString());
                                myJSONArray = response[1].toString();

                                Log.d("JSONArray Length: ", String.valueOf(jsonArray.length()));

                                /*------------This portion will create an array list from our JSONArray post request ---------------------------*/
                                // ArrayList<String> searchList2 = new ArrayList<>();
                                try {


                                    String first_name = null;
                                    String last_name = null;
                                    String user_id = null;
                                    if(jsonArray.length() == 0) {
                                        Toast.makeText(getContext(), "No result Found " +
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
                                        searchId.add(Integer.toString(i));
                                        //searchList.add(user_id);
                                        //searchList2.add(last_name);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                ArrayAdapter arrayAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_expandable_list_item_1, searchList);
                                messageList.setAdapter(arrayAdapter);
                                //messageList.setAdapter(arrayAdapter);
/*------------This portion will create an array list from our JSONArray post request ---------------------------*/



                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                            //searchIntent.putStringArrayListExtra("userDat", jsonObj.toStringArrayListExtra);
                            //dont need to start nuthin
                            //startActivity(searchIntent);
                        }

                    }
                    else
                    {
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }

                }


            });


//            Intent forwardSearchIntent = new Intent(getActivity(), SearchUsers.class);
//                startActivity(forwardSearchIntent);
/*---------------First and foremost we need to create a post request to get back a JSONArray---------------------*/





/*------------------This portion will eventually take us to activity with messages-------------------*/
        messageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String SelectedItem = searchList.get(position);//this is the name
                String SelectedId = searchId.get(position);//this is user id

                Intent messageIntent = new Intent(getContext(), PrivateMessage.class);
                messageIntent.putExtra("JSONArray", myJSONArray);
                messageIntent.putExtra("array_id", SelectedId);
                Log.d("Data sent ahead are: ",SelectedId);
                //Log.d("User id passed is: ", user_id);
                startActivity(messageIntent);
                //Toast.makeText(getContext(), SelectedItem, Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), SelectedId, Toast.LENGTH_SHORT).show();

            }
        });
/*------------------This portion will eventually take us to activity with messages-------------------*/

            Log.d("MyFragment", "Messages is visible.");
        }
        else
            Log.d("MyFragment", "Messages is not visible.");
    }
}

