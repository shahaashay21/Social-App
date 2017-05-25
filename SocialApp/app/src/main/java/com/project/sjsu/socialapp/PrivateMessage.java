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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class PrivateMessage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.private_message);

        
    }
}
