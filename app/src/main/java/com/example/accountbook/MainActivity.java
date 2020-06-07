package com.example.accountbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
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
import com.example.accountbook.Helper.DateBaseHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private HomePageFragment homePageFragment;
    private AddFragment addFragment;
    private RadioButton homeBtn;
    private RadioButton addBtn;
    //private RadioButton statisticsBtn;

    //当前打开的fragment，重复打开，return
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new DateBaseHelper(this);//初始化数据库

        homeBtn = findViewById(R.id.homeBtn);
        addBtn = findViewById(R.id.addBtn);

        homeBtn.setOnClickListener(btnClicked);
        addBtn.setOnClickListener(btnClicked);

        //显示首页
        initFragmnet(R.id.homeBtn);
    }

    View.OnClickListener btnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            initFragmnet(v.getId());
        }
    };

    //根据id显示fragment
    private void initFragmnet(int id)
    {
        switch (id)
        {
            case R.id.homeBtn:
                showHomeFragment();
                break;
            case R.id.addBtn:
                showAddFragment(null);
                break;
        }
    }

    //主页fragment
    public void showHomeFragment()
    {
        if(homePageFragment!=null && currentFragment == homePageFragment) return;;
        //调用系统方法，切换fragment
        FragmentManager fm =  getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if(homePageFragment==null)
            homePageFragment = new HomePageFragment();
        transaction.replace(R.id.fragmentLayout,homePageFragment);
        transaction.commit();
        homeBtn.setChecked(true);
        currentFragment = homePageFragment;
    }

    //记账fragment，传入null表示为新增，否则为修改
    public void showAddFragment(Account_info info)
    {
        if(addFragment!=null && currentFragment == addFragment) return;;
        FragmentManager fm =  getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if(addFragment==null)
            addFragment = new AddFragment();

        transaction.replace(R.id.fragmentLayout,addFragment);
        transaction.commit();
        addFragment.init(info);
        addBtn.setChecked(true);
        currentFragment = addFragment;
    }

}
