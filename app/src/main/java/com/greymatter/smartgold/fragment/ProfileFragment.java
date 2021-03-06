package com.greymatter.smartgold.fragment;

import android.Manifest;
import android.content.ClipDescription;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.greymatter.smartgold.BuildConfig;
import com.greymatter.smartgold.MainActivity;
import com.greymatter.smartgold.R;
import com.greymatter.smartgold.activity.AddressActivity;
import com.greymatter.smartgold.activity.EditProfileActivity;
import com.greymatter.smartgold.activity.OrderListActivity;
import com.greymatter.smartgold.activity.ScanQRActivity;
import com.greymatter.smartgold.activity.SigninActivity;
import com.greymatter.smartgold.activity.ViewOfferLockActivity;
import com.greymatter.smartgold.utils.Constants;
import com.greymatter.smartgold.utils.MyFunctions;

public class ProfileFragment extends Fragment {
    ImageView profileImage;
    TextView user_name,user_num;
    Button sign_out;
    LinearLayout Trans;

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
        Trans = root.findViewById(R.id.trans);


        if (!MyFunctions.getBooleanFromSharedPref(getActivity(),Constants.ISLOGGEDIN, false)){
            /*Intent intent = new Intent(getActivity(), SigninActivity.class);
            startActivity(intent);
            getActivity().finish();*/

            root.findViewById(R.id.edit).setVisibility(View.GONE);
            root.findViewById(R.id.signout_btn).setVisibility(View.GONE);
            root.findViewById(R.id.user_container).setVisibility(View.GONE);
            root.findViewById(R.id.login).setVisibility(View.VISIBLE);
            root.findViewById(R.id.guest_container).setVisibility(View.VISIBLE);
        }else {
            root.findViewById(R.id.edit).setVisibility(View.VISIBLE);
            root.findViewById(R.id.signout_btn).setVisibility(View.VISIBLE);
            root.findViewById(R.id.user_container).setVisibility(View.VISIBLE);
            root.findViewById(R.id.login).setVisibility(View.GONE);
            root.findViewById(R.id.guest_container).setVisibility(View.GONE);
        }

        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFunctions.logout(getActivity());
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        Trans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewOfferLockActivity.class);
                startActivity(intent);
            }
        });

        root.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddressActivity.class);
                startActivity(intent);
            }
        });
        root.findViewById(R.id.order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OrderListActivity.class);
                startActivity(intent);
            }
        });
        root.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
            }
        });
        root.findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareAppLink();
            }
        });
        root.findViewById(R.id.feedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedbackMail();
            }
        });

        root.findViewById(R.id.guest_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareAppLink();
            }
        });
        root.findViewById(R.id.guest_feedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedbackMail();
            }
        });

        root.findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SigninActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return root;
    }

    private void sendFeedbackMail() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("mailto:"+Constants.CONTACT_MAIL+"?subject=" + Uri.encode(Constants.EMAIL_SUBJECT) + "&body=" + Uri.encode(Constants.EMAIL_BODY));
        intent.setData(data);
        startActivity(intent);
    }

    // Function to check and request permission
    public boolean checkPermission(String permission, int requestCode) {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(getActivity(), permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] { permission }, requestCode);
        }
        else {
            //Toast.makeText(getActivity(), "Permission already granted", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }


    private void shareAppLink() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Smart Gold");
            String shareMessage= "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch(Exception e) {
            //e.toString();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try{
            user_name.setText(MyFunctions.getStringFromSharedPref(getActivity(), Constants.NAME,"Guest User"));
            user_num.setText(MyFunctions.getStringFromSharedPref(getActivity(), Constants.MOBILE,""));
        }catch (Exception e){e.printStackTrace();}
    }

    private void openUrl(String link) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(link));
        startActivity(i);
    }
}