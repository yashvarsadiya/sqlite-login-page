package com.example.sqlitedatabasedemo1;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Profile extends AppCompatActivity {

    private static final String TAG = Profile.class.getSimpleName();
    TextView profile_name,profile_email,profile_gender;
    Button logout_user;
    String email1;
    ImageView updateProfile,deleteProfile,profile_image;
    UserModal userModal;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profile_name = (TextView) findViewById(R.id.profile_name);
        profile_email = (TextView) findViewById(R.id.profile_email);
        profile_gender = (TextView) findViewById(R.id.profile_gender);
        profile_image = (ImageView) findViewById(R.id.profile_image);
        logout_user = (Button) findViewById(R.id.logout_user);
        updateProfile = (ImageView) findViewById(R.id.updateProfile);
        deleteProfile = (ImageView) findViewById(R.id.deleteProfile);

        email1 = getIntent().getStringExtra("key_email");
        Log.e(TAG,"MyAdapter thi Intent malyu and open profile "+email1);

        getUserDetails();

        logout_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(Profile.this,Login.class));
            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(Profile.this,UpdateProfile.class));
                Intent intent = new Intent(Profile.this,UpdateProfile.class);
                intent.putExtra("key_usermodal",userModal);
                startActivityForResult(intent,1);
            }
        });

        deleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
                builder.setTitle("Delete Profile");
                builder.setMessage("Are You Sure You Want To Delete Your Profile ?");

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DbHelper dbHelper = new DbHelper(Profile.this);
                        boolean b = dbHelper.deleteProfileHelper(userModal.getEmail());
                        if (b)
                        {
                            Toast.makeText(Profile.this, "Profile Delete Successfully..", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Profile.this,MainActivity.class));
                        }
                        else
                        {
                            Toast.makeText(Profile.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.show();
            }
        });
    }

    public void onResume(){
        // aa data update mate get karavyu
        super.onResume();
        getUserDetails();
        Log.e(TAG," profile Data Update succesfully ");
    }
    public void getUserDetails(){

        DbHelper dbHelper = new DbHelper(this);
        ArrayList<UserModal> al = dbHelper.getLoggedinUserDetails(email1);
        userModal = al.get(0);

        profile_name.setText(userModal.getName());
        profile_email.setText(userModal.getEmail());
        profile_gender.setText(userModal.getGender());

        if (profile_image == null){
            profile_image.setImageResource(R.drawable.user_img);
            Log.e(TAG,"get Profile blank image: "+profile_image);
        }else {
            profile_image.setImageURI(Uri.parse(userModal.getImage()));
            Log.e(TAG,"get camera & gallery image: "+userModal.getImage());
        }
    }
}