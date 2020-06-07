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
import com.example.accountbook.Helper.TipHelper;

import java.text.CollationKey;
import java.text.SimpleDateFormat;

public class AccountMsgActivity extends Activity {

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
import com.example.accountbook.Helper.TipHelper;

import java.text.CollationKey;
import java.text.SimpleDateFormat;

    //账单详情弹窗
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

            //取得传来的id
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            int id = bundle.getInt("id");

            //根据id获取数据
            info = AccountData.getInstance().GetInfoByID(id);

            //空数据，界面关闭
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

        //初始化,显示信息
        private void init()
        {
            targetNameTxt.setText(String.format(TipHelper.getContent(this.getResources(),R.string.TargetTitle),info.target));
            noteTxt.setText(String.format(TipHelper.getContent(this.getResources(),R.string.NoteTitle),info.note.equals("")?"Null":info.note));
            //时间格式化显示
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timeDesc = format.format(Long.parseLong(info.time));
            timeTxt.setText(String.format(TipHelper.getContent(this.getResources(),R.string.TimeTitle),timeDesc));
            typeTxt.setText(String.format(TipHelper.getContent(this.getResources(),R.string.TypeTitle),info.value>=0?TipHelper.getContent(this.getResources(),R.string.In):TipHelper.getContent(this.getResources(),R.string.Out)));
            valueTxt.setText(String.format(TipHelper.getContent(this.getResources(),R.string.PriceTitle),Math.abs(info.value)));
            orlderIDTxt.setText(String.format(TipHelper.getContent(this.getResources(),R.string.OrderIDTitle),info.orderID));
        }

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePanel();
            }
        };

        //点击背景关闭界面
        private void closePanel()
        {
            finish();
        }
    }
