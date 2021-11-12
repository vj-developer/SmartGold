package com.greymatter.smartgold;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.greymatter.smartgold.activity.SigninActivity;
import com.greymatter.smartgold.fragment.CartFragment;
import com.greymatter.smartgold.fragment.HomeFragment;
import com.greymatter.smartgold.fragment.ProfileFragment;
import com.greymatter.smartgold.fragment.SmartbuyFragment;
import com.greymatter.smartgold.utils.Constants;
import com.greymatter.smartgold.utils.MyFunctions;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    SmartbuyFragment smartbuyFragment = new SmartbuyFragment();
    CartFragment cartFragment = new CartFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    boolean isLoggedin;
    public static FragmentManager fragmentManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);

        isLoggedin = MyFunctions.getBooleanFromSharedPref(MainActivity.this, Constants.ISLOGGEDIN,false);

        fragmentManager = getSupportFragmentManager();
    }



    public boolean onNavigationItemSelected(@NonNull MenuItem item){

        switch (item.getItemId()){
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
                return true;

            case R.id.store:
                getSupportFragmentManager().beginTransaction().replace(R.id.container,smartbuyFragment).commit();
                return true;

            case R.id.cart:
                getSupportFragmentManager().beginTransaction().replace(R.id.container,cartFragment).commit();
                return true;

            case R.id.profile:
                if (isLoggedin){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,profileFragment).commit();
                }
                else {
                    Intent intent = new Intent(MainActivity.this, SigninActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
        }
        return false;
    }
}



