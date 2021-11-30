package com.greymatter.smartgold.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.greymatter.smartgold.R;
import com.greymatter.smartgold.utils.Constants;

public class CategoryDetailActivity extends AppCompatActivity {

    String category_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);

        category_id = getIntent().getStringExtra(Constants.CATEGORY_ID);

    }
}