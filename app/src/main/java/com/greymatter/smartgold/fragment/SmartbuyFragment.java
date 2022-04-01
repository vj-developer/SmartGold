package com.greymatter.smartgold.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.greymatter.smartgold.R;
import com.greymatter.smartgold.activity.SmartBuyActivity;
import com.greymatter.smartgold.utils.Constants;
import com.greymatter.smartgold.utils.MyFunctions;

import org.json.JSONObject;

public class SmartbuyFragment extends Fragment {

    TextView gold_rate;

    public SmartbuyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_smartbuy, container, false);

        gold_rate = view.findViewById(R.id.gold_rate);

        view.findViewById(R.id.smart_buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SmartBuyActivity.class);
                startActivity(intent);
            }
        });

        setGoldPrice();

        return view;
    }

    private void setGoldPrice() {
        String price = MyFunctions.getStringFromSharedPref(requireActivity(), Constants.GOLD_RATE,"null");
        if (!price.equals("null")){
            gold_rate.setText("Market price\n"
                    + price
                    +" + GST");
        }

    }

}