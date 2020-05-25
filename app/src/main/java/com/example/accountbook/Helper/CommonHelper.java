package com.example.accountbook.Helper;

import android.content.Context;

public class CommonHelper {

    public static final String packageName = "com.example.accountbook";

    public static int GetColorId(String key, Context context)
    {
        return context.getResources().getIdentifier(key,"colors",packageName);
    }
}
