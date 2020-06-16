package com.example.accountbook.Fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.accountbook.Data.Account_info;
import com.example.accountbook.Helper.DateBaseHelper;
import com.example.accountbook.Helper.TipHelper;
import com.example.accountbook.MainActivity;
import com.example.accountbook.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//add new entry and edit entries
public class AddFragment extends Fragment {
    private MainActivity mainActivity;

    private EditText nameTxt;
    private EditText noteTxt;
    private EditText valueTxt;
    private EditText orderIDTxt;
    private Button okBtn;
    private Button setTimeBtn;
    private TextView timeTitleTxt;
    private TextView updateTipTxt;

    //the flag to identify if the users selected the date for the entry
    private boolean selectDate;

    //the date and time of entry
    private int _year;
    private int _month;
    private int _day;
    private int _hour;
    private int _minute;

    private Account_info info;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity)getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_account_fragment,container,false);

        nameTxt = view.findViewById(R.id.nameEditTxt);
        noteTxt = view.findViewById(R.id.noteEditTxt);
        valueTxt = view.findViewById(R.id.valueEditTxt);
        orderIDTxt = view.findViewById(R.id.orderIDEditTxt);
        okBtn =view.findViewById(R.id.okBtn);
        setTimeBtn = view.findViewById(R.id.setTimeBtn);
        timeTitleTxt = view.findViewById(R.id.timeTitleTxt);
        updateTipTxt = view.findViewById(R.id.updateTipTxt);

        okBtn.setOnClickListener(clickListener);
        setTimeBtn.setOnClickListener(clickListener);

        //initialize the data and get the value
        if(info == null)
        {
            nameTxt.setText("");
            valueTxt.setText("");
            orderIDTxt.setText("");
            noteTxt.setText("");
            timeTitleTxt.setText(R.string.timeTitle);
            updateTipTxt.setVisibility(View.GONE);
            okBtn.setText(R.string.Save);
            selectDate = false;
        }
        else
        {
            nameTxt.setText(info.target);
            valueTxt.setText(String.valueOf(info.value));
            orderIDTxt.setText(info.orderID);
            noteTxt.setText(info.note);
            timeTitleTxt.setText(R.string.timeTitle2);
            updateTipTxt.setVisibility(View.VISIBLE);
            okBtn.setText(R.string.Update);
            selectDate = false;
        }

        return  view;
    }

    //before oncreate, initialize the data
    public void init(Account_info _info)
    {
        info = _info;
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.okBtn: saveData();break;
                case R.id.setTimeBtn:setTime(); break;
            }
        }
    };


    //save data
    private void saveData()
    {
        //write the value
        String name = nameTxt.getText().toString();
        if(name == null || name.equals(""))
        {
            TipHelper.showContentTip(mainActivity,R.string.TargetCanNotBeEmpty);
            //TipHelper.showContentTipByKey(mainActivity,"TargetCanNotBeEmpty");
            return;
        }
        String note = noteTxt.getText().toString();
        float value = Float.parseFloat(valueTxt.getText().toString());
        String orderID = orderIDTxt.getText().toString();
        String time = "";
        //if user did not selected the data, get the current time
        if(!selectDate)
        {
            if(info == null)
                time =String.valueOf(System.currentTimeMillis());
            else
                time = info.time;
        }
        else
        {
            //clear the time and get the time value
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            StringBuilder builder = new StringBuilder();
            builder.append(_year).append("-")
                    .append(_month).append("-")
                    .append(_day).append(" ")
                    .append(_hour).append(":")
                    .append(_minute).append(":")
                    .append("00");
            try {
                Date date = formatter.parse(builder.toString());
                time = String.valueOf(date.getTime()) ;
            }
            catch (Exception e)
            {

            }
        }

        //write to database
        SQLiteDatabase db= DateBaseHelper.Instance.getWritableDatabase();//create or open the database
        ContentValues values=new ContentValues();
        values.put(Account_info.Key_Target,name);
        values.put(Account_info.Key_Note,note);
        values.put(Account_info.Key_Value,value);
        values.put(Account_info.Key_OrderID,orderID);
        values.put(Account_info.Key_Time,time);

        try {
            if(info == null)//add new entry
            {
                long student_Id=db.insert(Account_info.TableName,null,values);
                Log.d("insertResult",String.valueOf(student_Id));
                TipHelper.showContentTip(mainActivity,R.string.SaveSuccess);
            }
            else //edit entry
            {
                int resultID = db.update(Account_info.TableName,values,Account_info.Key_ID + "=" +info.id,null);
                Log.d("updateResult",String.valueOf(resultID));
                TipHelper.showContentTip(mainActivity,R.string.UpdateSuccess);
            }

        }
        catch (Exception e)
        {
            Log.d("AddFragment_Exc",e.getMessage());
        }
        finally {

            nameTxt.setText("");
            valueTxt.setText("");
            orderIDTxt.setText("");
            noteTxt.setText("");
            selectDate = false;
            db.close();
        }

    }

    //call the calender function of android
    private void setTime()
    {
        Calendar calendar=Calendar.getInstance();

        new DatePickerDialog( mainActivity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                _year = year;
                _month = month+1;
                _day = dayOfMonth;
                setHour();
            }

        }
                ,calendar.get(Calendar.YEAR)
                ,calendar.get(Calendar.MONTH)
                ,calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    //call the calender function of android and set the data
    private void setHour()
    {
        Calendar calendar=Calendar.getInstance();
        new TimePickerDialog( mainActivity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                _hour = hourOfDay;
                _minute = minute;
                selectDate =true;
            }
        }
                ,calendar.get(Calendar.HOUR_OF_DAY)
                ,calendar.get(Calendar.MINUTE),true).show();
    }
}
