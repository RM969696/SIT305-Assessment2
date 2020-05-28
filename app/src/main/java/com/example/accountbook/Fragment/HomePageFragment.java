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
import com.example.accountbook.MainActivity;
import com.example.accountbook.R;
import com.example.accountbook.layout.AccountMsgItemLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//首页，账单列表
public class HomePageFragment extends Fragment {

    private MainActivity mainActivity;
    private ScrollView scrollView;
    private LinearLayout root;

    //日期下拉菜单，数据
    private Spinner dateSpinner;
    private List<String> dateList;
    private ArrayAdapter<String> dateAdapterList;

    //收支类型下拉菜单，数据
    private Spinner typeSpinner;
    private List<String> typeList;
    private ArrayAdapter<String> typeAdapterList;

    //提示
    private TextView emptyTipTxt;

    //当前日期下拉框的位置，默认为0
    private int selectDatePos;
    //当前收支类型下拉框的位置，默认为0
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
        //初始化日期下拉菜单数据
        if(dateList==null)
        {
            dateList=new ArrayList<>();
            dateList.add("全部");
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);//现在的年份
            int month = calendar.get(Calendar.MONTH)+1;//现在的月份
            for (int i = month;i > 0; i--)
            {
                dateList.add(year+"-"+i);//格式化时间显示
            }
        }

        if(dateAdapterList ==null)
        {
            dateAdapterList = new ArrayAdapter<String>(mainActivity,R.layout.support_simple_spinner_dropdown_item,dateList);
        }
        dateSpinner.setAdapter(dateAdapterList);
        dateSpinner.setSelection(0,true);//禁止OnItemSelectedListener默认自动调用一次
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

        //初始化收支类型下拉菜单
        if(typeList==null)
        {
            typeList=new ArrayList<>();
            typeList.add("全部");
            typeList.add("收入");
            typeList.add("支出");
        }
        if(typeAdapterList ==null)
        {
            typeAdapterList = new ArrayAdapter<String>(mainActivity,R.layout.support_simple_spinner_dropdown_item,typeList);

        }

        typeSpinner.setAdapter(typeAdapterList);
        typeSpinner.setSelection(0,true);//禁止OnItemSelectedListener默认自动调用一次
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

    //显示列表数据
    private void init()
    {
        long dateMin = Long.MIN_VALUE;
        long dateMax = Long.MAX_VALUE;
        if(selectDatePos != 0)//对日期进行了筛选，计算出日期最小值与最大值，符合范围的进行显示
        {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");//格式化计算时间戳
                Date date = formatter.parse(dateList.get(selectDatePos));
                dateMin = date.getTime();
                if(selectDatePos != 1)
                {
                    Date date1 = formatter.parse(dateList.get(selectDatePos-1));//获取下一个月的时间
                    dateMax = date1.getTime();
                }

                Log.d("time",date.getTime()+"");
            }catch (Exception e){}
        }

        boolean all = selectTypePos==0;//不筛选时，为true
        boolean in = selectTypePos == 1;//是否为收入
        //上述条件有一个满足则符合收支筛选条件

        root.removeAllViews();
        for (Account_info info : AccountData.getInstance().infoList)
        {
            //时间不符合筛选条件，continue
            if( Long.parseLong(info.time)  >dateMax || Long.parseLong(info.time) < dateMin) continue;

            //不筛选，或筛选且符合条件时，显示
            if(all || (in && info.value >0) ||(!in && info.value<0))
            {
                AccountMsgItemLayout itemLayout = new AccountMsgItemLayout(mainActivity,root,info);
            }
        }

        //根据是否有内容，进行提示
        emptyTipTxt.setVisibility(root.getChildCount()>0?View.GONE:View.VISIBLE);
    }

    private void initData()
    {
        //查询所有数据
        SQLiteDatabase db= DateBaseHelper.Instance.getWritableDatabase();//创建 or 打开 可读/写的数据库
        //第一个参数为sql语句，第二个当语句中有占位符时使用
        Cursor cursor = db.rawQuery(String.format("select * from '%s' order by time desc", Account_info.TableName),null );
        if(AccountData.getInstance().infoList==null) AccountData.getInstance().infoList = new ArrayList<>();
        else  AccountData.getInstance().infoList.clear();
        //遍历赋值
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
        //关闭数据库
        db.close();
    }
}