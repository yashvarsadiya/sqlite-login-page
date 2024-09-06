package com.example.sqlitedatabasedemo1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void openLoginActivity(View view)
    {
        Intent intent = new Intent(MainActivity.this,Login.class);
        startActivity(intent);
    }

    public void openRegisterActivity(View view)
    {
        startActivity(new Intent(MainActivity.this, Register.class));
    }

    public void openSeeAllDataActivity(View view){
        startActivity(new Intent(MainActivity.this, Userlist.class));
    }

}