package com.example.accountbook.Data;

//表结构
public class Account_info {

    public static final String TableName = "account_info";
    public static final String Key_ID = "id";
    public static final String Key_Value = "value";
    public static final String Key_OrderID = "order_id";
    public static final String Key_Time = "time";
    public static final String Key_Target = "target";
    public static final String Key_Note ="note";

//create the data I need for the app
    public int id;
    public float value;
    public String orderID;
    public String time;
    public String target;
    public String note;
}
