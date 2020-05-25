package com.example.accountbook.Fragment;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.accountbook.Data.AccountData;
import com.example.accountbook.Data.Account_info;
import com.example.accountbook.Helper.DateBaseHelper;
import com.example.accountbook.MainActivity;
import com.example.accountbook.R;
import com.example.accountbook.layout.AccountMsgItemLayout;

import java.util.ArrayList;

public class HomePageFragment extends Fragment {

    private MainActivity mainActivity;
    private ScrollView scrollView;
    private LinearLayout root;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity)getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_page_fragment,container,false);
        scrollView = view.findViewById(R.id.scroll);
        root = view.findViewById(R.id.root);
        initData();
        init();
        return view;
    }

    private void init()
    {
        root.removeAllViews();
        for (Account_info info : AccountData.getInstance().infoList)
        {
            AccountMsgItemLayout itemLayout = new AccountMsgItemLayout(mainActivity,root,info);
        }
    }

    private void initData()
    {
        //查询所有数据
        SQLiteDatabase db= DateBaseHelper.Instance.getWritableDatabase();//创建 or 打开 可读/写的数据库
        //第一个参数为sql语句，第二个当语句中有占位符时使用
        Cursor cursor = db.rawQuery(String.format("select * from '%s' order by time desc", Account_info.TableName),null );
        if(AccountData.getInstance().infoList==null) AccountData.getInstance().infoList = new ArrayList<>();
        else  AccountData.getInstance().infoList.clear();
        while (cursor.moveToNext())
        {
            Account_info info = new Account_info();
            info.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Account_info.Key_ID)));
            info.target = cursor.getString(cursor.getColumnIndex(Account_info.Key_Target));
            info.note = cursor.getString(cursor.getColumnIndex(Account_info.Key_Note));
            info.orderID = cursor.getString(cursor.getColumnIndex(Account_info.Key_OrderID));
            info.time = cursor.getString(cursor.getColumnIndex(Account_info.Key_Time));
            info.value = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Account_info.Key_Value)));
            AccountData.getInstance().infoList.add(info);
        }
        db.close();
        Log.d("InitData","有"+AccountData.getInstance().infoList.size()+"条数据");
    }
}
