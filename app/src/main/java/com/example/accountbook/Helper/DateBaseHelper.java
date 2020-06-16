package com.example.accountbook.Helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.accountbook.Data.Account_info;

public class DateBaseHelper extends SQLiteOpenHelper {

    public static DateBaseHelper Instance;

    private static final String DBName = "Account.db";
    private static final int DATABASE_VERSION = 1;

    public DateBaseHelper(Context context)
    {
        super(context,DBName,null,DATABASE_VERSION);
        Instance =this;
    }

    //if database is not exist, it will be in oncreate
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("helper","oncreate");
        //create data table
        String CREATE_TABLE_STUDENT="CREATE TABLE "+ Account_info.TableName+"("
                +Account_info.Key_ID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"
                +Account_info.Key_Value+" FLOAT, "
                +Account_info.Key_OrderID+" TEXT, "
                +Account_info.Key_Time+" TEXT, "
                +Account_info.Key_Target+" TEXT, "
                +Account_info.Key_Note+" TEXT)";

        db.execSQL(CREATE_TABLE_STUDENT);
    }

    //upgrade the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("helper","onupgrade");
    }
}

