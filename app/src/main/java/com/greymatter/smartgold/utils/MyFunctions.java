package com.greymatter.smartgold.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class MyFunctions {

    public static boolean getBooleanFromSharedPref(Context context,String key, boolean defValue) {
        SharedPreferences sh = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
        return sh.getBoolean(key,defValue);
    }

    public static void saveBooleanToSharedPref(Context context,String key, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putBoolean(key,value);
        myEdit.apply();
    }

    public static void saveStringToSharedPref(Context context,String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString(key,value);
        myEdit.apply();
    }

    public static String getStringFromSharedPref(Context context,String key, String defValue) {
        SharedPreferences sh = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
        return sh.getString(key,defValue);
    }

}
