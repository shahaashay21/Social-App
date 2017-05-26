package com.project.sjsu.socialapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import  com.project.sjsu.socialapp.adapter.FeedListAdapter;
import com.project.sjsu.socialapp.data.FeedItem;
import com.project.sjsu.socialapp.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Hoang on 5/4/2017.
 */

public class Front  extends Fragment{

    String finalImage;
    Boolean image_flag = false;
    static String Global_ID;


    private static final String TAG = SocialActivity.class.getSimpleName();
    private ListView listView;
    //create list adpater
    private FeedListAdapter listAdapter;
    private List<FeedItem> feedItems;
    private String URL_FEED = "http://api.androidhive.info/feed/feed.json";
    //this will be the new feed url once we get it up and runnig
    //private String URL_FEED = "/feed/feed.json";

    private final String IP = "http://54.183.170.253:3000";
    private String GET_FEED;
    private final String UPLOAD_POST_ROUTE = "/feed/post";



    public static final int SELECTED_PICTURE = 1;

    private SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "SocialApp";

    EditText mPostText;
    Button mPhotoButton;
    Button mPostButton;
    ImageView mImage;


    public Front()
    {
        //empty constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.front, container, false);

//        Log.e("URL Called is: ", GET_FEED);

        image_flag = false;
        /*  Upload Post Stuff  */
        mPostText = (EditText) rootView.findViewById(R.id.post_text);
        mPhotoButton = (Button) rootView.findViewById(R.id.photo_button);
        mPostButton = (Button) rootView.findViewById(R.id.post_buttton);
        //mImage = (ImageView) rootView.findViewById(R.id.post_image);
        /*  Upload Post stufff  */

        feedItems = new ArrayList<FeedItem>();
        listAdapter = new FeedListAdapter(getActivity(), feedItems);
        listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(listAdapter);

        sharedPreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        final String idValue = sharedPreferences.getString("userId", null);
        Global_ID = idValue;
        GET_FEED = "/feed/get/"+ Global_ID;
        Log.e("Setting globalk id: ", Global_ID);
        if(idValue == null){
            Intent forwardLoginIntent = new Intent(getActivity(), LoginActivity.class);
            startActivity(forwardLoginIntent);
            getActivity().finish();
        }


        mPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String encodeImage = "0";
                if(image_flag) {
                    Bitmap bitmap = null;
                    try {
                        bitmap = BitmapFactory.decodeFile(finalImage);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] imageByteArray = baos.toByteArray();

                        encodeImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    encodeImage = "0";

                }

                final HttpRequest request = new HttpRequest(getContext());
                Map<String, String> sendData = new HashMap<String, String>();
                sendData.put("post_image", encodeImage);
                sendData.put("user_id", idValue);
                sendData.put("feed_text", mPostText.getText().toString());




                request.sendPostRequest(IP + UPLOAD_POST_ROUTE, sendData, new CallbackInterface() {
                    @Override
                    public void onCallBackComplete(String[] response) {
                        Log.e("Update First Respo", response[0].toString());
                        if(response[0].toString().equals("success"))
                        {
                            Toast.makeText(getContext(), "Your status has been posted", Toast.LENGTH_SHORT).show();
                            mPostText.setText("");
                        }
                        else
                        {
                            Toast.makeText(getContext(), "Your status was not posted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        //user will click button this calls upload image methd which calls intent and his reurns image hopefully
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage(getView());
            }
        });




/*Dont neeed to touch this for now it is used to generate feeddddd*/
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(URL_FEED);
        if (entry == null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    Log.d("Cache Data: ", data);
                    parseJsonFeed(new JSONObject(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            // making fresh volley request and getting json
//            HttpRequest request = new HttpRequest(getContext());
//            final Map<String, String> sendSearchData = new HashMap<>();
//            Log.e("IP + GET FEED:", IP+GET_FEED);
//
//            request.sendPostRequest(IP + GET_FEED, sendSearchData, new CallbackInterface() {
//                @Override
//                public void onCallBackComplete(String[] response) {
//
//                    Log.e("RESPONSE IS:", response[0]);
//                    Log.e("Response Length:", String.valueOf(response.length));
//                    Log.d("Debug Reponse[1]:", response[1].toString());
//
//                    if(response[0].toString().equals("success")){
//                        if(response[1].toString().equals("error")){
//
//
//                        }else{
//
//                        }
//                    }
//
//                }
//            });
//
            JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET,
                    URL_FEED, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {

                    Log.d("JSON RESPONSE Gandu:",response.toString() );
                    VolleyLog.d(TAG, "Response: " + response.toString());
                    if (response != null) {
                        Log.d("JSON RESPONSE:",response.toString() );
                        parseJsonFeed(response);
//                        Log.e("JSON RESPONSE:",response.toString() );
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                }
            });

            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(jsonReq);
        }
/*Dont neeed to touch this for now it is used to generate feeddddd*/


        return rootView;
    }


    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("feed");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                FeedItem item = new FeedItem();
                Log.d("Feed: ", feedObj.toString());
                Log.d("ID: ", String.valueOf(feedObj.getString("id")));
                if(feedObj.getString("id") == null || feedObj.getString("id") == "null") {
                    item.setId(0);
                } else{
                    int value = Integer.parseInt(feedObj.getString("id"));
                    Log.d("Id in integer: ", String.valueOf(value));
                    item.setId(value);
                }
                item.setName(feedObj.getString("name"));

                // Image might be null sometimes
                Log.d("Setting image for id:",feedObj.getString("id"));
                String image = feedObj.isNull("image") ? null : feedObj
                        .getString("image");
                item.setImge(image);
                Log.d("Setting status for id:",feedObj.getString("id"));
                item.setStatus(feedObj.getString("status"));
                Log.d("Setting pic for id:",feedObj.getString("id"));
                item.setProfilePic(feedObj.getString("profilePic"));
                Log.d("Setting timestamp id:",feedObj.getString("id"));
                if(feedObj.getString("timeStamp") == null || feedObj.getString("timeStamp") == "null") {
                    item.setTimeStamp("0");
                } else
                    item.setTimeStamp(feedObj.getString("timeStamp"));


                // url might be null sometimes
                Log.d("Setting feedurl for id:",feedObj.getString("id"));
                String feedUrl = feedObj.isNull("url") ? null : feedObj
                        .getString("url");
                item.setUrl(feedUrl);
                Log.d("add item for id:",feedObj.getString("id"));
                feedItems.add(item);
            }

            // notify data changes to list adapater
            listAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //this method is called when the user hits the photo button
    public void uploadImage(View v){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, SELECTED_PICTURE);
    }


    //This overriide will return the image that was selected when the user hit the Photo button
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == getActivity().RESULT_OK && requestCode == SELECTED_PICTURE && data != null){
            Uri uri = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getActivity().getContentResolver().query(uri, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            finalImage = cursor.getString(columnIndex);

            Toast.makeText(getContext(), "Image has been selected", Toast.LENGTH_SHORT).show();
            Log.e("ImageData", finalImage);
            image_flag = true;
        }

        else
        {
            //do something that will let other methhods know that no image was uploaded
            Toast.makeText(getActivity(), "You haven't picked Image",
                    Toast.LENGTH_LONG).show();
            image_flag = false;
        }

    }


}