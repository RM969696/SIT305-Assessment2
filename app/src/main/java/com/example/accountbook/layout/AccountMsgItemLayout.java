package com.example.accountbook.layout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.accountbook.AccountMsgActivity;
import com.example.accountbook.Data.Account_info;
import com.example.accountbook.Helper.CommonHelper;
import com.example.accountbook.MainActivity;
import com.example.accountbook.R;

import java.text.SimpleDateFormat;

public class AccountMsgItemLayout extends ConstraintLayout {

    private Context superContext;
    private Account_info info;
    private View self;

    private TextView targetNameTxt;
    private TextView noteTxt;
    private TextView timeTxt;
    private TextView valueTxt;


    public AccountMsgItemLayout(Context context, ViewGroup root, Account_info _info)
    {

        super(context);
        self =  LayoutInflater.from(context).inflate(R.layout.account_msg_item,root,false);//第三个参数传入false，当传true时，会有数据赋值失败的情况，false的时候要addview
        root.addView(self);//addView必须要加在这里
        superContext = context;
        info = _info;

        targetNameTxt = self.findViewById(R.id.targetNameTxt);
        noteTxt = self.findViewById(R.id.noteTxt);
        timeTxt = self.findViewById(R.id.timeTxt);
        valueTxt = self.findViewById(R.id.ValueTxt);


        self.setOnClickListener(clickListener);

        init();
    }

    private void init()
    {
        targetNameTxt.setText(info.target);
        noteTxt.setText(info.note.equals("")?"无":info.note);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeDesc = format.format(Long.parseLong(info.time));
        timeTxt.setText(timeDesc);
        valueTxt.setText( (info.value<0? "":"+") + info.value );
        //valueTxt.setTextColor(info.value<0? CommonHelper.GetColorId("colorRed",superContext):CommonHelper.GetColorId("colorGreen",superContext));
        valueTxt.setTextColor(info.value<0? superContext.getResources().getColor(R.color.colorRed):superContext.getResources().getColor(R.color.colorGreen));
    }

    OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            showMsgPanel();
        }
    };

    private void showMsgPanel()
    {
        Intent intent = new Intent(superContext, AccountMsgActivity.class);
        intent.putExtra("id",info.id);
        superContext.startActivity(intent);
    }
}
