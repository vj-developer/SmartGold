package com.greymatter.smartgold.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.greymatter.smartgold.activity.AddressActivity;
import com.greymatter.smartgold.MainActivity;
import com.greymatter.smartgold.R;
import com.greymatter.smartgold.utils.Constants;
import com.greymatter.smartgold.utils.MyFunctions;

public class ProfileFragment extends Fragment {
    ImageView profileImage;
    TextView user_name,user_num;
    Button sign_out;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        profileImage = root.findViewById(R.id.profile_image);
        user_name = root.findViewById(R.id.user_name);
        user_num = root.findViewById(R.id.user_num);
        sign_out = root.findViewById(R.id.signout_btn);

        user_name.setText(MyFunctions.getStringFromSharedPref(getActivity(), Constants.NAME,""));
        user_num.setText(MyFunctions.getStringFromSharedPref(getActivity(), Constants.MOBILE,""));

        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFunctions.saveBooleanToSharedPref(getActivity(),Constants.ISLOGGEDIN, false);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        root.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddressActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }
}