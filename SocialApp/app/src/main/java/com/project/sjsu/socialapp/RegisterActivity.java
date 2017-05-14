package com.project.sjsu.socialapp;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private final String IP = "http://54.183.170.253:3000";
    private final String REGISTER_ROUTE = "/user/register";

    //UI references
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mUserName;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mConfirmPassword;
    private Button mRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirstName = (EditText) findViewById(R.id.firstName);
        mLastName = (EditText) findViewById(R.id.lastName);
        mUserName = (EditText) findViewById(R.id.userName);
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mConfirmPassword = (EditText) findViewById(R.id.confirmPassword);

        mRegister = (Button) findViewById(R.id.register);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearErrors();
                if(checkConfirmPassword()){
                    if(isEmptyField()){
                        if(checkEmail(mEmail.getText().toString())) {
                            Map<String, String> registerData = new HashMap();
                            registerData.put("first_name", mFirstName.getText().toString());
                            registerData.put("last_name", mLastName.getText().toString());
                            registerData.put("user_name", mUserName.getText().toString());
                            registerData.put("email", mEmail.getText().toString());
                            registerData.put("password", mPassword.getText().toString());

                            HttpRequest request = new HttpRequest(getApplicationContext());
                            request.sendPostRequest(IP + REGISTER_ROUTE, registerData, new CallbackInterface() {
                                @Override
                                public void onCallBackComplete(String[] response) {
                                    if (response[0].toString().equals("success")) {
                                        if (response[1].toString().equals("available")) {
                                            Toast.makeText(getApplicationContext(), "User name or email is already available", Toast.LENGTH_LONG).show();
                                        } else if (response[1].toString().equals("registered")) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                            builder.setMessage("Successfully Registered. Email has been sent to your email id.")
                                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            finish();
                                                        }
                                                    });
                                            builder.create().show();
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else {
                            mEmail.setError("Invalid email id");
                            mEmail.requestFocus();
                        }
                    }
                }else {
                    mConfirmPassword.setError("Oops, that's not the same password");
                    mConfirmPassword.requestFocus();
                }
            }
        });

    }

    //Check empty field
    private boolean isEmptyField() {
        Boolean emptyField = false;
        View focusView = null;
        if(mConfirmPassword.getText().toString().equals("") || mConfirmPassword.getText().toString().isEmpty()){
            mConfirmPassword.setError("Please fill out this field");
            focusView = mConfirmPassword;
            emptyField = true;
        }
        if(mPassword.getText().toString().equals("") || mPassword.getText().toString().isEmpty()){
            mPassword.setError("Please fill out this field");
            focusView = mPassword;
            emptyField = true;
        }
        if(mEmail.getText().toString().equals("") || mEmail.getText().toString().isEmpty()){
            mEmail.setError("Please fill out this field");
            focusView = mEmail;
            emptyField = true;
        }
        if(mUserName.getText().toString().equals("") || mUserName.getText().toString().isEmpty()){
            mUserName.setError("Please fill out this field");
            focusView = mUserName;
            emptyField = true;
        }
        if(mLastName.getText().toString().equals("") || mLastName.getText().toString().isEmpty()){
            mLastName.setError("Please fill out this field");
            focusView = mLastName;
            emptyField = true;
        }
        if(mFirstName.getText().toString().equals("") || mFirstName.getText().toString().isEmpty()){
            mFirstName.setError("Please fill out this field");
            focusView = mFirstName;
            emptyField = true;
        }

        if(emptyField && focusView != null){
            focusView.requestFocus();
            return false;
        }else {
            return true;
        }
    }

    //Check confirm password as password
    private boolean checkConfirmPassword() {
        if(mPassword.getText().toString().equals(mConfirmPassword.getText().toString())){
            return true;
        }else {
            return false;
        }
    }

    private boolean checkEmail(String emailCheck){
        String regex = "^(.+)@(.+)(\\..+.+)$";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(emailCheck);
        return matcher.matches();
    }

    private void clearErrors(){
        mFirstName.setError(null);
        mLastName.setError(null);
        mUserName.setError(null);
        mEmail.setError(null);
        mPassword.setError(null);
        mConfirmPassword.setError(null);
    }
}
