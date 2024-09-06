package com.example.sqlitedatabasedemo1;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Userlist extends AppCompatActivity {

    private static final String TAG = Userlist.class.getSimpleName();
    RecyclerView recyclerView;
    String name,email,gender,image;
    DbHelper DB;
    MyAdapter adapter;
    ArrayList<UserModal> userModalList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlist);

    }


    private void displaydata()
    {
        Cursor cursor = DB.getData();
        Log.e(TAG,"CURSOR WORKED "+cursor);
        if(cursor.getCount()==0)
        {
            Toast.makeText(Userlist.this, "No Entery Exists ", Toast.LENGTH_SHORT).show();
            Log.e(TAG,"NO ENTERY WORKED ");
            return;
        }else
        {
            while (cursor.moveToNext())
            {
                 name = cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_NAME));
                 image = cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_IAMGE));
                 email = cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_EMAIL));
                 gender = cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_GENDER));
//               name.add(cursor.getString(1));
//               image.add(cursor.getString(2));
//               email.add(cursor.getString(3));
//               gender.add(cursor.getString(5));
                Log.e(TAG,"ELSE CURSOR MOVENEXT " + name + " " + image + " " + email + " " + gender + " " + cursor.getCount() + " ");
                UserModal userModal = new UserModal();
                userModal.setName(name);
                userModal.setImage(image);
                userModal.setEmail(email);
                userModal.setGender(gender);
                userModalList.add(userModal);
            }
        }
    }

    public void onResume(){
        // aa data update mate get karavyu
        super.onResume();
        Log.e(TAG,"Update Data succesfully ");
        userModalList.clear();

        DB = new DbHelper(this);
        displaydata();
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MyAdapter(this, userModalList, new MyAdapter.OnClick() {
            @Override
            public void onClick(String name, String email, String gender, String image) {

            }
        });
        recyclerView.setAdapter(adapter);
    }
}