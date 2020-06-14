package com.example.accountbook.Data;

//表结构
public class Account_info {

    public static final String TableName = "account_info";//表名
    public static final String Key_ID = "id";//键名
    public static final String Key_Value = "value";//键名
    public static final String Key_OrderID = "order_id";//键名
    public static final String Key_Time = "time";//键名
    public static final String Key_Target = "target";//键名
    public static final String Key_Note ="note";//键名


    public int id;//id
    public float value;//金额>0为收入 ，<0 为消费
    public String orderID;//订单号
    public String time;//时间
    public String target;//交易方
    public String note;//备注
}
