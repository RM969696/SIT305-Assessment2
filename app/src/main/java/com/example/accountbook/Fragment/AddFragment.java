package com.example.accountbook.Fragment;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.accountbook.Data.Account_info;
import com.example.accountbook.Helper.CommonHelper;
import com.example.accountbook.Helper.DateBaseHelper;
import com.example.accountbook.Helper.TipHelper;
import com.example.accountbook.MainActivity;
import com.example.accountbook.R;

public class AddFragment extends Fragment {
    private MainActivity mainActivity;

    private EditText nameTxt;
    private EditText noteTxt;
    private EditText valueTxt;
    private EditText orderIDTxt;
    private EditText timeTxt;
    private Button okBtn;

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
        timeTxt = view.findViewById(R.id.timeEditTxt);
        okBtn =view.findViewById(R.id.okBtn);



        okBtn.setOnClickListener(clickListener);
        return  view;
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            saveData();
        }
    };

    private void saveData()
    {
        //打开连接，写入数据
        String name = nameTxt.getText().toString();
        if(name == null || name.equals(""))
        {
            TipHelper.showContentTipByKey(mainActivity,"TargetCanNotBeEmpty");
            return;
        }
        String note = noteTxt.getText().toString();
        int value = Integer.parseInt(valueTxt.getText().toString());
        String orderID = orderIDTxt.getText().toString();
        String time = timeTxt.getText().toString();
        if(time==null || time.equals(""))
            time =String.valueOf(System.currentTimeMillis());

        SQLiteDatabase db= DateBaseHelper.Instance.getWritableDatabase();//创建 or 打开 可读/写的数据库
        ContentValues values=new ContentValues();
        values.put(Account_info.Key_Target,name);
        values.put(Account_info.Key_Note,note);
        values.put(Account_info.Key_Value,value);
        values.put(Account_info.Key_OrderID,orderID);
        values.put(Account_info.Key_Time,time);

        try {
            long student_Id=db.insert(Account_info.TableName,null,values);
            Log.d("insertResult",String.valueOf(student_Id));
            TipHelper.showContentTipByKey(mainActivity,"SaveSuccess");
        }
        catch (Exception e)
        {
            Log.d("AddFragment_Exc",e.getMessage());
        }
        finally {
            nameTxt.setText("");
            noteTxt.setText("");
            valueTxt.setText("");
            orderIDTxt.setText("");
            timeTxt.setText("");
            db.close();
        }


    }
}
