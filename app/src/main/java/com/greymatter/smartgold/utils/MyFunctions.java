package com.greymatter.smartgold.utils;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;

import com.greymatter.smartgold.activity.ProductDetailActivity;

public class MyFunctions {

    private static ProgressDialog progressDialog;

    public static boolean getBooleanFromSharedPref(Context context, String key, boolean defValue) {
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

    public static void showLoading(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(Constants.LOADING);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static void cancelLoading() {
        progressDialog.dismiss();
    }
}
