package com.example.sqlitedatabasedemo1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class Login extends AppCompatActivity {

    private static final String TAG = Login.class.getSimpleName();

    EditText log_email, log_pass;
    Button log_button, log_forgot;
    String email1;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        log_email = (EditText) findViewById(R.id.log_email);
        log_pass = (EditText) findViewById(R.id.log_pass);
        log_button = (Button) findViewById(R.id.log_button);
        log_forgot = (Button) findViewById(R.id.log_forgot);

        email1 = getIntent().getStringExtra("email");
        Log.e(TAG, "login page open thyu: "+email1);



        // Aa karvathi je user par click karshu teni email id autometic thai jay
        log_email.setText(email1);
        Log.e(TAG,"email login ma set thyu "+log_email);

        log_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email1 = log_email.getText().toString();
                String pass1 = log_pass.getText().toString();

                DbHelper dbHelper = new DbHelper(Login.this);
                boolean loggedin = dbHelper.login(email1, pass1);
                if (loggedin) {
                    if (loggedin) {
                        Intent intent = new Intent(Login.this, Profile.class);
                        intent.putExtra("key_email", email1);
                        startActivityForResult(intent, 1);
                    }
                } else {
                    if (email1.isEmpty() && pass1.isEmpty()) {
                        Toast.makeText(Login.this, "Please Enter All Fills", Toast.LENGTH_SHORT).show();
                        log_email.setError("Please enter email");
                        log_pass.setError("Please enter password");
                    }
                    if (email1.isEmpty()) {
                        log_email.setError("Please enter email");
                        Toast.makeText(Login.this, "Please enter email", Toast.LENGTH_SHORT).show();
                    }else {
//                        Toast.makeText(Login.this, "Email id and password didn't matched", Toast.LENGTH_SHORT).show();
                        if (Patterns.EMAIL_ADDRESS.matcher(email1).matches()){
                            Toast.makeText(Login.this, "validated email", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(Login.this, "invalid email", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        log_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
                startActivity(intent);
            }
        });
    }

}