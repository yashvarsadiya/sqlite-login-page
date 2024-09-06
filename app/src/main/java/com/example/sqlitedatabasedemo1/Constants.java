package com.example.sqlitedatabasedemo1;

public class Constants {

    //Db name
    public static final String DB_NAME = "MY_RECORDS_DB";

    // Db version
    public static final int DB_VERSION = 1;

    //Table name
    public static final String TABLE_NAME = "MY_RECORDS_TABLE";


    //Colums / fields of table
    public static final String C_ID = "ID";
    public static final String C_NAME = "NAME";
    public static final String C_IAMGE = "IMAGE";
    public static final String C_EMAIL = "EMAIL";
    public static final String C_PASSWORD = "PASSWORD";
    public static final String C_GENDER = "GENDER";


    //Create TABLE query
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + C_NAME + " TEXT,"
            + C_IAMGE + " BLOB,"
            + C_EMAIL + " TEXT,"
            + C_PASSWORD + " TEXT,"
            + C_GENDER + " TEXT"
            + ")";
}
