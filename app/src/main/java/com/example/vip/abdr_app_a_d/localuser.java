package com.example.vip.abdr_app_a_d;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by v i p on 2/15/2017.
 */

public class localuser {
    public static final String SP_NAME="userDetails";
    SharedPreferences userLocalDatabase;
    SharedPreferences serviceDatabase;
HashMap<SharedPreferences,SharedPreferences> hh=new HashMap<>();
    JSONObject jo=null;
    public localuser(Context context){

        userLocalDatabase=context.getSharedPreferences(SP_NAME,0);
        serviceDatabase=context.getSharedPreferences(SP_NAME,0);
    }
    public localuser(){


    }
    public void storeData(adminDriver AD){
        SharedPreferences.Editor spEditor=userLocalDatabase.edit();
        spEditor.putString("flag" ,AD.flag);
        spEditor.putInt("ID" ,AD.ID);
        spEditor.commit();

    }
    public adminDriver getLoggedInUser(){
        String flag =userLocalDatabase.getString("flag"," ");
        int id =userLocalDatabase.getInt("ID",-1);
    adminDriver p=new adminDriver(flag,id);
    return p;
    }
    public void setLoggedInUser(boolean loggedin){
       SharedPreferences.Editor spEditor=userLocalDatabase.edit();
        spEditor.putBoolean("LoggedIn",loggedin);
        spEditor.commit();

    }
    public boolean getIfLoggedInUser(){
    if (userLocalDatabase.getBoolean("LoggedIn",false)){
        return true;
    }
        else{
        return false;
    }

    }
    public void clear(){
        SharedPreferences.Editor spEditor=userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();

    }

    public void setsid(int st){
        SharedPreferences.Editor spEditor=serviceDatabase.edit();
        spEditor.putInt("sid" ,st);

        spEditor.commit();

    }
    public int getsid(){

        int st =serviceDatabase.getInt("sid",0);

        return st;
    }

    public void setcbooking(String st){
        SharedPreferences.Editor spEditor=serviceDatabase.edit();
        spEditor.putString("CB" ,st);

        spEditor.commit();

    }
    public String  getcbooking(){

        String  st =serviceDatabase.getString("CB","");

        return st;
    }
    public void setsuid(int st){
        SharedPreferences.Editor spEditor=serviceDatabase.edit();
        spEditor.putInt("suid" ,st);

        spEditor.commit();

    }
    public int getsuid(){

        int st =serviceDatabase.getInt("suid",0);

        return st;
    }


}
