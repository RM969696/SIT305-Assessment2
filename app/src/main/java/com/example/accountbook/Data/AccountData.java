package com.example.accountbook.Data;

import java.util.List;

//store all the information of each expense
public class AccountData {

    private static AccountData instance;

    public static AccountData getInstance() {
        if(instance==null)
            instance = new AccountData();
        return instance;
    }

    public List<Account_info> infoList;

    //get ID
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
