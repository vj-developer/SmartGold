package com.greymatter.smartgold.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.greymatter.smartgold.R;
import com.greymatter.smartgold.activity.SmartBuyActivity;

public class SmartbuyFragment extends Fragment {


    public SmartbuyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_smartbuy, container, false);

        view.findViewById(R.id.smart_buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SmartBuyActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}