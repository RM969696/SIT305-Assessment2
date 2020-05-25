package com.example.accountbook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.accountbook.Data.AccountData;
import com.example.accountbook.Data.Account_info;
import com.example.accountbook.Helper.CommonHelper;

import java.text.CollationKey;
import java.text.SimpleDateFormat;

public class AccountMsgActivity extends Activity {

    Account_info info;

    private TextView targetNameTxt;
    private TextView noteTxt;
    private TextView timeTxt;
    private TextView valueTxt;
    private TextView typeTxt;
    private Button closeBtn;
    private TextView orlderIDTxt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_msg_layout);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int id = bundle.getInt("id");
        Log.d("id",String.valueOf(id));



        info = AccountData.getInstance().GetInfoByID(id);

        if(info == null)
            finish();

        targetNameTxt = findViewById(R.id.targetNameTxt);
        noteTxt = findViewById(R.id.noteTxt);
        timeTxt = findViewById(R.id.timeTxt);
        valueTxt = findViewById(R.id.valueTxt);
        typeTxt = findViewById(R.id.typeTxt);
        closeBtn = findViewById(R.id.closeBtn);
        orlderIDTxt = findViewById(R.id.orderIDTxt);

        closeBtn.setOnClickListener(clickListener);

        init();
    }

    private void init()
    {
        targetNameTxt.setText(String.format("交易方:%s",info.target));
        noteTxt.setText(String.format("备注:%s",info.note.equals("")?"无":info.note));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeDesc = format.format(Long.parseLong(info.time));
        timeTxt.setText(String.format("交易时间:%s",timeDesc));
        typeTxt.setText(String.format("收支类型:%s",info.value>=0?"收入":"支出"));
        valueTxt.setText(String.format("交易金额:%s",Math.abs(info.value)));
        orlderIDTxt.setText(String.format("订单号:%s",info.orderID));
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            closePanel();
        }
    };

    private void closePanel()
    {
        finish();
    }
}
