package com.example.accountbook.Helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.accountbook.Data.Account_info;

//数据库管理基类
public class DateBaseHelper extends SQLiteOpenHelper {

    public static DateBaseHelper Instance;

    private static final String DBName = "Account.db";
    private static final int DATABASE_VERSION = 1;

    public DateBaseHelper(Context context)
    {
        super(context,DBName,null,DATABASE_VERSION);//父类构造
        Instance =this;
    }

    //如果数据库不存在，创建数据库会进入onCreate，同时，在oncreate里面创建需要的表
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("helper","oncreate");
        //创建数据表
        String CREATE_TABLE_STUDENT="CREATE TABLE "+ Account_info.TableName+"("
                +Account_info.Key_ID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"
                +Account_info.Key_Value+" FLOAT, "
                +Account_info.Key_OrderID+" TEXT, "
                +Account_info.Key_Time+" TEXT, "
                +Account_info.Key_Target+" TEXT, "
                +Account_info.Key_Note+" TEXT)";

        db.execSQL(CREATE_TABLE_STUDENT);
        //数据库实际上是没有被创建或者打开的，直到getWritableDatabase() 或者 getReadableDatabase() 方法中的一个被调用时才会进行创建或者打开
    }

    //数据库升级时调用
    //如果DATABASE_VERSION值被修改,系统发现现有数据库版本不同,即会调用onUpgrade（）方法
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("helper","onupgrade");
    }
}

