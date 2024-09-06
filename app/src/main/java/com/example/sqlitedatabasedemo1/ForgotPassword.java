package com.example.sqlitedatabasedemo1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ForgotPassword extends AppCompatActivity {

    private static final String TAG = Register.class.getSimpleName();
    EditText forget_email,forget_pass,forget_ConfirmPass;
    Button reset_button;
    DbHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        forget_email = (EditText) findViewById(R.id.forget_email);
        forget_pass = (EditText) findViewById(R.id.forget_pass);
        forget_ConfirmPass = (EditText) findViewById(R.id.forget_ConfirmPass);
        reset_button = (Button) findViewById(R.id.reset_button);

        dbHelper = new DbHelper(this);

        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email,pass,rePass;
                try {
                    email = forget_email.getText().toString();
                    pass = forget_pass.getText().toString();
                    rePass = forget_ConfirmPass.getText().toString();

                    if (email.equals("") || pass.equals("") || rePass.equals(""))
                    {      // jyare amuk data lakhvana baki hoy tyare aa condition uge thay
                        Toast.makeText(ForgotPassword.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if (pass.equals(rePass))            //password update thay chhhe
                        {
                            int updatepass = dbHelper.updatepass(email,pass);
                            Log.e(TAG,"updatepass "+updatepass);
                            if(updatepass == 1)
                            {
                                forget_email.setText("");
                                forget_ConfirmPass.setText("");
//                                forget_pass.setText("");
                                Toast.makeText(ForgotPassword.this, "Password Updated", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ForgotPassword.this,Login.class);
                                startActivity(intent);
                                finish();
                            }
                            else                // password update nathi thato
                            {
                                Toast.makeText(ForgotPassword.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else        //jyare passw0rd not matched
                        {
                            Toast.makeText(ForgotPassword.this, "Password and Confirm Password not matched", Toast.LENGTH_SHORT).show();
                        }

                    }

                }
                catch (Exception e){
                    Toast.makeText(ForgotPassword.this, "Out of Bound "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}