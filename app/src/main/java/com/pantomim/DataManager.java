package com.pantomim;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by aryahm on 5/16/17.
 */

public class DataManager {
    public static String getBaseUrl(){
        return "https://harchi";
    }
    public static String getToken(Context context){
        if(context!=null) {
            SharedPreferences sharedpreferences = context.getApplicationContext().getSharedPreferences(context.getApplicationContext().getString(R.string.perfer_key), Context.MODE_PRIVATE);
            return sharedpreferences.getString("token", "");
        }
        else
            return "";

    }
    public static String getUsername(Context context){
        if(context!=null) {
            SharedPreferences sharedpreferences = context.getApplicationContext().getSharedPreferences(context.getApplicationContext().getString(R.string.perfer_key), Context.MODE_PRIVATE);
            return sharedpreferences.getString("username", "");
        }
        else
            return "";

    }
    public static void setUsername(Context context, String username){
        SharedPreferences sharedpreferences =context. getApplicationContext().getSharedPreferences(context.getApplicationContext().getString(R.string.perfer_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if(context!=null){
            editor.putString("username",username);
        }
        editor.commit();

    }
    public static void setToken(Context context, String username){
        SharedPreferences sharedpreferences =context. getApplicationContext().getSharedPreferences(context.getApplicationContext().getString(R.string.perfer_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if(context!=null){
            editor.putString("token",username);
        }
        editor.commit();


    }
    public static void setScore(Context context, int score){
        SharedPreferences sharedpreferences =context. getApplicationContext().getSharedPreferences(context.getApplicationContext().getString(R.string.perfer_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if(context!=null){
            editor.putString("score",String.valueOf(score));
        }
        editor.commit();
    }
    public static int getScore(Context context){
        if(context!=null) {
            SharedPreferences sharedpreferences = context.getApplicationContext().getSharedPreferences(context.getApplicationContext().getString(R.string.perfer_key), Context.MODE_PRIVATE);
            return Integer.parseInt(sharedpreferences.getString("score", ""));
        }
        else
            return -1;

    }
    public static void setId(Context context, int id){
        SharedPreferences sharedpreferences =context. getApplicationContext().getSharedPreferences(context.getApplicationContext().getString(R.string.perfer_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if(context!=null){
            editor.putString("id",String.valueOf(id));
        }
        editor.commit();

    }
    public static int getId(Context context){
        if(context!=null) {
            SharedPreferences sharedpreferences = context.getApplicationContext().getSharedPreferences(context.getApplicationContext().getString(R.string.perfer_key), Context.MODE_PRIVATE);
            return Integer.parseInt(sharedpreferences.getString("id", ""));
        }
        else
            return 0;

    }

}
