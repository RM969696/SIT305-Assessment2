package com.example.accountbook.layout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.accountbook.AccountMsgActivity;
import com.example.accountbook.Data.AccountData;
import com.example.accountbook.Data.Account_info;
import com.example.accountbook.Helper.DateBaseHelper;
import com.example.accountbook.Helper.TipHelper;
import com.example.accountbook.MainActivity;
import com.example.accountbook.R;

import java.text.SimpleDateFormat;

//账单item 信息类
public class AccountMsgItemLayout extends ConstraintLayout {

    private Context superContext;
    private Account_info info;
    private View self;

    private TextView targetNameTxt;
    private TextView noteTxt;
    private TextView timeTxt;
    private TextView valueTxt;
    private Button editBtn;
    private Button deleteBtn;

    private MainActivity mainActivity;

    public AccountMsgItemLayout(Context context, ViewGroup root, Account_info _info)
    {
        super(context);
        self =  LayoutInflater.from(context).inflate(R.layout.account_msg_item,root,false);//第三个参数传入false，当传true时，会有数据赋值失败的情况，false的时候要addview
        root.addView(self);//addView必须要加在这里
        mainActivity = (MainActivity) context;
        superContext = context;
        info = _info;

        targetNameTxt = self.findViewById(R.id.targetNameTxt);
        noteTxt = self.findViewById(R.id.noteTxt);
        timeTxt = self.findViewById(R.id.timeTxt);
        valueTxt = self.findViewById(R.id.ValueTxt);
        editBtn = self.findViewById(R.id.editBtn);
        deleteBtn = self.findViewById(R.id.deleteBtn);


        self.setOnClickListener(clickListener);
        editBtn.setOnClickListener(btnClickListener);
        deleteBtn.setOnClickListener(btnClickListener);

        init();
    }

    //初始化，显示数据
    private void init()
    {
        targetNameTxt.setText(info.target);
        noteTxt.setText(info.note.equals("")?"无":info.note);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeDesc = format.format(Long.parseLong(info.time));
        timeTxt.setText(timeDesc);
        valueTxt.setText( (info.value<0? "":"+") + info.value );
        valueTxt.setTextColor(info.value<0? superContext.getResources().getColor(R.color.colorRed):superContext.getResources().getColor(R.color.colorGreen));
    }

    OnClickListener btnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.editBtn:showEditPanel();break;
                case R.id.deleteBtn:showDeleteDialog();break;
            }
        }
    };

    OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            showMsgPanel();
        }
    };

    //显示账单详情弹窗
    private void showMsgPanel()
    {
        Intent intent = new Intent(superContext, AccountMsgActivity.class);
        intent.putExtra("id",info.id);//传入参数 账单id
        superContext.startActivity(intent);
    }
    //前往编辑账单
    private void showEditPanel()
    {
        mainActivity.showAddFragment(info);
    }

    private void showDeleteDialog()
    {
        new AlertDialog.Builder( mainActivity )
                .setIcon( R.drawable.ic_launcher_background)
                .setTitle( R.string.DialogTitle )
                .setMessage( R.string.DialogContent )
                .setNegativeButton( R.string.CancleBtn,null )
                .setPositiveButton( R.string.OkBtn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteData();
                    }
                } )
                .show();

    }

    private void deleteData()
    {
        SQLiteDatabase db= DateBaseHelper.Instance.getWritableDatabase();//创建 or 打开 可读/写的数据库
        db.delete(Account_info.TableName,Account_info.Key_ID+ "=" +info.id,null);
        TipHelper.showContentTip(mainActivity,R.string.DeleteSuccess);
        AccountData.getInstance().DeleteAccountByID(info.id);
        self.setVisibility(GONE);

    }
}
