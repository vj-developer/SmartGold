package com.greymatter.smartgold;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.greymatter.smartgold.activity.SigninActivity;
import com.greymatter.smartgold.fragment.CartFragment;
import com.greymatter.smartgold.fragment.HomeFragment;
import com.greymatter.smartgold.fragment.ProfileFragment;
import com.greymatter.smartgold.fragment.SmartbuyFragment;
import com.greymatter.smartgold.utils.Constants;
import com.greymatter.smartgold.utils.MyFunctions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    public static TabLayout tabLayout;
    private List<Fragment> fragmentList;
    private long backpressedtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //tab layout
        frameLayout = findViewById(R.id.framelayout);
        tabLayout = findViewById(R.id.tablayout);

        fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new SmartbuyFragment());
        fragmentList.add(new CartFragment());
        fragmentList.add(new ProfileFragment());

        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //setFragment(tab.getPosition());
            }
        });

        setFragment(0);

    }

    public void getCartCount() {
        /*Cart count*/
        int count = Integer.parseInt(MyFunctions.getStringFromSharedPref(getApplicationContext(),Constants.CART_COUNT,"0"));
        if (count > 0) {
            tabLayout.getTabAt(2).getOrCreateBadge().setNumber(count);
        }
    }

    @Override
    public void onBackPressed() {
        setFragment(0);
        tabLayout.setScrollPosition(0,0f,true);
        if (backpressedtime + 2000 > System.currentTimeMillis()){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        else {
            Toast.makeText(getBaseContext(),"Press back again to exit",Toast.LENGTH_SHORT).show();
        }
        backpressedtime = System.currentTimeMillis();
    }

    public void setFragment (int position){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(frameLayout.getId(),fragmentList.get(position));
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCartCount();
    }
}



