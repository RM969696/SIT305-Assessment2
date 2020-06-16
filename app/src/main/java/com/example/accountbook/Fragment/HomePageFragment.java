package com.example.accountbook.Fragment;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.accountbook.Data.AccountData;
import com.example.accountbook.Data.Account_info;
import com.example.accountbook.Helper.DateBaseHelper;
import com.example.accountbook.Helper.TipHelper;
import com.example.accountbook.MainActivity;
import com.example.accountbook.R;
import com.example.accountbook.layout.AccountMsgItemLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class HomePageFragment extends Fragment {

    private MainActivity mainActivity;
    private ScrollView scrollView;
    private LinearLayout root;

    //the drop down menu of date
    private Spinner dateSpinner;
    private List<String> dateList;
    private ArrayAdapter<String> dateAdapterList;

    //the drop down menu of type
    private Spinner typeSpinner;
    private List<String> typeList;
    private ArrayAdapter<String> typeAdapterList;

    private TextView emptyTipTxt;

    //the position of selecData
    private int selectDatePos;
    //当the position of selectType
    private int selectTypePos;

    public HomePageFragment() {
    }

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
        dateSpinner = view.findViewById(R.id.dateSpinner);
        typeSpinner = view.findViewById(R.id.typeSpinner);
        emptyTipTxt = view.findViewById(R.id.emptyTipTxt);
        selectDatePos = 0;
        selectTypePos = 0;
        initSpinner();
        initData();
        init();
        return view;
    }

    private void initSpinner()
    {
        //initialize the drop down menu for date
        if(dateList==null)
        {
            dateList=new ArrayList<>();
            dateList.add(TipHelper.getContent(mainActivity.getResources(),R.string.All));
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH)+1;
            for (int i = month;i > 0; i--)
            {
                dateList.add(year+"-"+i);
            }
        }

        if(dateAdapterList ==null)
        {
            dateAdapterList = new ArrayAdapter<String>(mainActivity,R.layout.support_simple_spinner_dropdown_item,dateList);
        }
        dateSpinner.setAdapter(dateAdapterList);
        dateSpinner.setSelection(0,true);
        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectDatePos = position;
                init();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //initialize the drop down menu for type
        if(typeList==null)
        {
            typeList=new ArrayList<>();
            typeList.add(TipHelper.getContent(mainActivity.getResources(),R.string.All));
            typeList.add(TipHelper.getContent(mainActivity.getResources(),R.string.In));
            typeList.add(TipHelper.getContent(mainActivity.getResources(),R.string.Out));
        }
        if(typeAdapterList ==null)
        {
            typeAdapterList = new ArrayAdapter<String>(mainActivity,R.layout.support_simple_spinner_dropdown_item,typeList);

        }

        typeSpinner.setAdapter(typeAdapterList);
        typeSpinner.setSelection(0,true);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectTypePos = position;
                init();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    //show the data
    private void init()
    {
        long dateMin = Long.MIN_VALUE;
        long dateMax = Long.MAX_VALUE;
        if(selectDatePos != 0)//normalize the time value
        {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
                Date date = formatter.parse(dateList.get(selectDatePos));
                dateMin = date.getTime();
                if(selectDatePos != 1)
                {
                    Date date1 = formatter.parse(dateList.get(selectDatePos-1));
                    dateMax = date1.getTime();
                }

                Log.d("time",date.getTime()+"");
            }catch (Exception e){}
        }

        boolean all = selectTypePos==0;
        boolean in = selectTypePos == 1;
        //set the filter

        root.removeAllViews();
        for (Account_info info : AccountData.getInstance().infoList)
        {

            if( Long.parseLong(info.time)  >dateMax || Long.parseLong(info.time) < dateMin) continue;


            if(all || (in && info.value >0) ||(!in && info.value<0))
            {
                AccountMsgItemLayout itemLayout = new AccountMsgItemLayout(mainActivity,root,info);
            }
        }

        //show tips if need
        emptyTipTxt.setVisibility(root.getChildCount()>0?View.GONE:View.VISIBLE);
    }

    private void initData()
    {
        //qury all data in database
        SQLiteDatabase db= DateBaseHelper.Instance.getWritableDatabase();//create or open the database

        Cursor cursor = db.rawQuery(String.format("select * from '%s' order by time desc", Account_info.TableName),null );
        if(AccountData.getInstance().infoList==null) AccountData.getInstance().infoList = new ArrayList<>();
        else  AccountData.getInstance().infoList.clear();
//        set the data
        while (cursor.moveToNext())
        {
            Account_info info = new Account_info();
            info.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Account_info.Key_ID)));
            info.target = cursor.getString(cursor.getColumnIndex(Account_info.Key_Target));
            info.note = cursor.getString(cursor.getColumnIndex(Account_info.Key_Note));
            info.orderID = cursor.getString(cursor.getColumnIndex(Account_info.Key_OrderID));
            info.time = cursor.getString(cursor.getColumnIndex(Account_info.Key_Time));
            info.value = Float.parseFloat(cursor.getString(cursor.getColumnIndex(Account_info.Key_Value)));
            AccountData.getInstance().infoList.add(info);
        }
        //close the database
        db.close();
    }
}
