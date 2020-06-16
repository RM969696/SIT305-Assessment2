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

    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new DateBaseHelper(this);//initialize the database

        homeBtn = findViewById(R.id.homeBtn);
        addBtn = findViewById(R.id.addBtn);

        homeBtn.setOnClickListener(btnClicked);
        addBtn.setOnClickListener(btnClicked);

        //show the main page
        initFragmnet(R.id.homeBtn);
    }

    View.OnClickListener btnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            initFragmnet(v.getId());
        }
    };

    //show the fragment by id
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


    public void showHomeFragment()
    {
        if(homePageFragment!=null && currentFragment == homePageFragment) return;;
        //switch the fragment
        FragmentManager fm =  getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if(homePageFragment==null)
            homePageFragment = new HomePageFragment();
        transaction.replace(R.id.fragmentLayout,homePageFragment);
        transaction.commit();
        homeBtn.setChecked(true);
        currentFragment = homePageFragment;
    }

    //addfragment
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
