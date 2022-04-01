package com.greymatter.smartgold.utils;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.greymatter.smartgold.R;
import com.greymatter.smartgold.activity.ProductDetailActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Currency;

import javax.net.ssl.HttpsURLConnection;

public class MyFunctions {

    private static ProgressDialog progressDialog;
    private static Dialog dialog;

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
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.viewload);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ImageView load = dialog.findViewById(R.id.loadgif);
        Glide.with(context).asGif().load(R.raw.load).into(load);
        dialog.show();
//        progressDialog = new ProgressDialog(context);
//        progressDialog.setTitle(Constants.LOADING);
//        progressDialog.setCancelable(false);
//        progressDialog.show();
    }

    public static void cancelLoading() {
        dialog.dismiss();
//        progressDialog.dismiss();
    }

    public static String ConvertToINR(String price) {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance(Constants.INR));
        return format.format(Integer.parseInt(price));
    }

    public static String getResponseFromUrl(String url) {
        HttpsURLConnection con = null;
        try {
            URL u = new URL(url);
            con = (HttpsURLConnection) u.openConnection();
            con.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            return sb.toString();

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }

}
