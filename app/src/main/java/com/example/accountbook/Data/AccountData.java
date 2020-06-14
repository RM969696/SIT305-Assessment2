package com.example.accountbook.Data;

import java.util.List;

//存放所有账单信息数据
public class AccountData {

    private static AccountData instance;

    public static AccountData getInstance() {
        if(instance==null)
            instance = new AccountData();
        return instance;
    }
    //所有账单
    public List<Account_info> infoList;

    //根据id获取账单
    public  Account_info GetInfoByID(int id)
    {
        if(infoList == null)
            return null;
        for (Account_info info : infoList)
        {
            if(info.id == id)
                return  info;
        }
        return null;
    }

    public void DeleteAccountByID(int id)
    {
        if(infoList == null)
            return;
        for (Account_info info : infoList)
        {
            if(info.id == id)
            {
                infoList.remove(info);
                return;
            }

        }
    }
}
