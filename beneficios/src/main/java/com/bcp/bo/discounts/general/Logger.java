package com.bcp.bo.discounts.general;

import android.util.Log;

import java.util.Calendar;

/**
 * Created by BC2078 on 10/16/2015.
 */
public class Logger {
    public static String Tag;
    public static void Tag(String tag){
        Tag=tag;
    }
    public static void Info(String message){
        Calendar calendar= Calendar.getInstance();
        Log.d(Tag,"["+calendar.getTime()+"]"+message);
    }
}
