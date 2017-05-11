package com.project.sjsu.socialapp;

import android.app.DownloadManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aashayshah on 5/10/17.
 */


public class HttpRequest {

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private Context applicationContext;

    private int successRequest;
    private String requestResponseData;

    private String[] requestResponse = new String[2];

    private String[] postRequestResponse = new String[2];

    public HttpRequest(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    public String[] sendGetRequest(final String URL, final String data){

        new Handler(Looper.getMainLooper()).post(
                new Runnable() {
                    @Override
                    public void run() {


                        mRequestQueue = Volley.newRequestQueue(applicationContext);
                        mStringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("Done", "Done");
                                Log.d("response", response);
                                responseGenerator(successRequest, response);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Not Done", "Done");
                                Log.d("error", error.toString());
                                responseGenerator(successRequest, String.valueOf(error));
                            }
                        });

                        mRequestQueue.add(mStringRequest);
                    }
                }
        );

        return requestResponse;
    }

    public void responseGenerator(int successRequest, String requestResponseData){

        if(successRequest == 1){
            requestResponse[0] = "success";
        }else{
            requestResponse[0] = "error";
        }

        requestResponse[1] = requestResponseData;
    }

    public void sendPostRequest(final String URL, final Map data, final CallbackInterface listner){
        Log.d("INTO sendPostRequest", "HERE");
        new Handler(Looper.getMainLooper()).post(
            new Runnable() {
                @Override
                public void run() {
                    mRequestQueue = Volley.newRequestQueue(applicationContext);
                    StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                Log.d("Response", response);
                                postRequestResponse = responsePostGenerator(1, response);
                                listner.onCallBackComplete(postRequestResponse);
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Error.Response", error.toString());
                                postRequestResponse = responsePostGenerator(0, error.toString());
                                listner.onCallBackComplete(postRequestResponse);
                            }
                        }
                    ) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String>  params = new HashMap<String, String>();
                            params = data;
                            return  params;
                        }
                    };

                    mRequestQueue.add(postRequest);
                }
            }
        );

//        return postRequestResponse;
    }


    public String[] responsePostGenerator(int successRequest, String requestResponseData){

        String[] res = new String[2];
        if(successRequest == 1){
            res[0] = "success";
        }else{
            res[0] = "error";
        }

        res[1] = requestResponseData;
        return res;
    }

}

