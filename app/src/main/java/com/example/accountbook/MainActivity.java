package com.example.accountbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.example.accountbook.Data.AccountData;
import com.example.accountbook.Data.Account_info;
import com.example.accountbook.Fragment.AddFragment;
import com.example.accountbook.Fragment.HomePageFragment;
import com.example.accountbook.Fragment.StatisticsFragment;
import com.example.accountbook.Helper.DateBaseHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private HomePageFragment homePageFragment;
    private AddFragment addFragment;
    private StatisticsFragment statisticsFragment;

    private RadioButton homeBtn;
    private RadioButton addBtn;
    private RadioButton statisticsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new DateBaseHelper(this);

        homeBtn = findViewById(R.id.homeBtn);
        addBtn = findViewById(R.id.addBtn);
        statisticsBtn = findViewById(R.id.statisticsBtn);

        homeBtn.setOnClickListener(btnClicked);
        addBtn.setOnClickListener(btnClicked);
        statisticsBtn.setOnClickListener(btnClicked);

        homeBtn.setChecked(true);
        initFragmnet(R.id.homeBtn);
    }

    View.OnClickListener btnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            initFragmnet(v.getId());
        }
    };

    private void initFragmnet(int id)
    {
        FragmentManager fm =  getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (id)
        {
            case R.id.homeBtn:
                if(homePageFragment==null)
                    homePageFragment = new HomePageFragment();
                transaction.replace(R.id.fragmentLayout,homePageFragment);
                transaction.commit();
                break;
            case R.id.addBtn:
                if(addFragment==null)
                    addFragment = new AddFragment();
                transaction.replace(R.id.fragmentLayout,addFragment);
                transaction.commit();
                break;
            case R.id.statisticsBtn:
                if(statisticsFragment==null)
                    statisticsFragment = new StatisticsFragment();
                transaction.replace(R.id.fragmentLayout,statisticsFragment);
                transaction.commit();
                break;
        }
    }


    private void insertData()
    {
        //打开连接，写入数据
        Log.d("order","1");

        SQLiteDatabase db= DateBaseHelper.Instance.getWritableDatabase();//创建 or 打开 可读/写的数据库
        Log.d("order","2");
        ContentValues values=new ContentValues();
        values.put("age",10);
        values.put("email","#.cc");
        values.put("name","xiaoli");
        //
        Log.d("order","3");
        long student_Id=db.insert("student",null,values);
        db.close();
        Log.d("insertResult",String.valueOf(student_Id));
    }
}
