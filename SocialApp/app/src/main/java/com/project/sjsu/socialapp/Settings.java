package com.project.sjsu.socialapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Hoang on 5/4/2017.
 */

public class Settings  extends Fragment {

    private Button mSignOut;
    private SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "SocialApp";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings, container, false);

        //Shared Preferences
        sharedPreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        mSignOut = (Button) rootView.findViewById(R.id.signOut);
        mSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userId", null);
                editor.commit();

                Intent forwardLoginIntent = new Intent(getActivity(), LoginActivity.class);
                startActivity(forwardLoginIntent);
                getActivity().finish();
            }
        });




        return rootView;
    }
}

