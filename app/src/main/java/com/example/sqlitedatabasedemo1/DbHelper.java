package com.example.sqlitedatabasedemo1;

import static com.example.sqlitedatabasedemo1.Constants.DB_VERSION;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {

    private static final String TAG = DbHelper.class.getSimpleName();

//    private static String DATABASE_NAME = "demo_db";
//
//    private static int DATABASE_VERSION = 1;

    public DbHelper(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        //table create karyu
//        String CREATE_TABLE_QUERY = "CREATE TABLE register (id INTEGER PRIMARY KEY AUTOINCREMENT ,name TEXT,image BLOB,email TEXT, password TEXT ,gender TEXT)";
//        db.execSQL(CREATE_TABLE_QUERY);

        // Constants class mathi TAble Create Karyu
        db.execSQL(Constants.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {    //junu table delete thai jay and navu table create thay
        //upgrade database (if there is any stucture change the change db version)
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        //Create table again
        onCreate(db);    //on create atle call kari ke junu table delet thay atle biji war  table create thay
    }

    //insert recode to db
    public long insertRecord(String name1, String image1, String email1, String pass1, String gender1) {

        SQLiteDatabase db = this.getWritableDatabase();  // data insert,update,delete mate .getWritableDatabase(); use karvu

        ContentValues values = new ContentValues();
        // id will be inserted automatically as we set AUTOINCREMENT in Query

        //insert values
        values.put(Constants.C_NAME, name1);
        values.put(Constants.C_IAMGE, image1);
        values.put(Constants.C_EMAIL, email1);
        values.put(Constants.C_PASSWORD, pass1);
        values.put(Constants.C_GENDER, gender1);

        Log.e(TAG, "INSERT Name "+name1);
        Log.e(TAG, "INSERT Image "+image1);
        Log.e(TAG, "INSERT Email "+email1);
        Log.e(TAG, "INSERT Password "+pass1);
        Log.e(TAG, "INSERT Gender "+gender1);

        //insret row , it WIll return record id of the Saved Record
        long id = db.insert(Constants.TABLE_NAME, null, values);

        //close db connection
        db.close();

        //return id of inserted record
        return id;

    }

//    public boolean registerUserHelper(String name1,String email1, String pass1, String gender1)
//    {
//        // values insert
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();  // data insert,update,delete mate .getWritableDatabase(); use karvu
//
//        //insret data
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("name",name1);
////      contentValues.put("image",image1);
//        contentValues.put("email",email1);
//        contentValues.put("password",pass1);
//        contentValues.put("gender",gender1);
//
//        // insert Row , it WIll return the id of the Saved Record
//         long l = sqLiteDatabase.insert("register",null,contentValues);
//        sqLiteDatabase.close();
//
//        if (l>0)
//        {
//            return true;
//        }
//        else
//        {
//            return false;
//        }
//    }

    Boolean loggedin;

    public boolean login(String email1, String pass1) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + Constants.TABLE_NAME + " WHERE email = '" + email1 + "' AND password = '" + pass1 + "'", null);
        if (cursor.moveToFirst()) {
            loggedin = true;
        } else {
            loggedin = false;
        }
        return loggedin;
    }

    public ArrayList<UserModal> getLoggedinUserDetails(String email1) {
        ArrayList<UserModal> al = new ArrayList<>();    //ArrayList Object

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String query = "SELECT * FROM " + Constants.TABLE_NAME + " WHERE email= '" + email1 + "'";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            // 0 position ma id chhe    3 position ma password chhhe
            String name = cursor.getString(1);// 1 position ma name chhhe
            String image = cursor.getString(2); // 2 position ma image chhhe
            String email = cursor.getString(3);  // 3 position ma email chhe
            String gender = cursor.getString(5); // 5 position ma gender chhe

            UserModal userModal = new UserModal();
            userModal.setName(name);
            userModal.setImage(image);
            userModal.setEmail(email);
            userModal.setGender(gender);

            al.add(userModal);
        }

        return al;
    }

    public ArrayList getAllUserDetailsHelper() {
        ArrayList alUser = new ArrayList();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + Constants.TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {

                ArrayList al = new ArrayList<>();


                // 0 position ma id chhe 4 position ma password chhhe
                String name = cursor.getString(1);    // 1 position ma name chhhe
                String image = cursor.getString(2);  // 2 position ma image chhhe
                String email = cursor.getString(3);  // 3 position ma email chhe
                String gender = cursor.getString(5); // 5 position ma gender chhe

                al.add(name);
                al.add(image);
                al.add(email);
                al.add(gender);

                al.add(al);

            } while (cursor.moveToNext());
        }
        return alUser;
    }


    public boolean updateProfileHelper(String email1, String name1, String gender1, String image1) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();              // AA Object values update karva mate banavyo
        values.put("name", name1);
        values.put("gender", gender1);
        values.put("image", image1);

        int i = sqLiteDatabase.update(Constants.TABLE_NAME, values, "email=?", new String[]{email1});

        if (i > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteProfileHelper(String email1) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int i = sqLiteDatabase.delete(Constants.TABLE_NAME, "email=?", new String[]{email1});          // int value return kare int=i karyu chhhe.
        if (i > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkEmailIdExits(String email1) {
        Log.e(TAG, "check email " + email1);
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + Constants.TABLE_NAME + " WHERE email = '" + email1 + "'", null);
        if (cursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }
    }
    public int updatepass(String email1, String pass1) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", pass1);
        return sqLiteDatabase.update(Constants.TABLE_NAME, values, "email=?", new String[]{email1});
    }

    public Cursor getData() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(" Select * from " + Constants.TABLE_NAME, null);
        Log.e(TAG, "CURSOR WORKED" + cursor);
        return cursor;
    }
}
