package com.example.accountbook.Helper;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;

//提示帮助类
public class TipHelper {

    public static final String packageName = "com.example.accountbook";

    public static  String getTip(Resources resources, String tipKey)
    {
        String value = "";

        int id = resources.getIdentifier(tipKey,"string",packageName);

        try
        {
            value = resources.getString(id);
        }
        catch (Exception e)
        {
            Log.e("tipError",tipKey + "   not found");
        }
        return  value;
    }

    //id未知时，根据name获取string配置
    public static void showContentTipByKey(Context context,String key)
    {
        Toast.makeText(context,getTip(context.getResources(),key),Toast.LENGTH_SHORT).show();
    }


    public static String getContent(Resources resources,int resID)
    {
        String value = "";
        try
        {
            value = resources.getString(resID);
        }
        catch (Exception e)
        {
            Log.e("tipError",resID + "   not found");
        }
        return  value;
    }

    //根据id获取string配置，并显示
    public static void showContentTip(Context context,int resID)
    {
        Toast.makeText(context,getContent(context.getResources(),resID),Toast.LENGTH_SHORT).show();
    }





}
