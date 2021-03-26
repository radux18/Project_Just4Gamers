package com.example.project_just4gamers.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.example.project_just4gamers.R;
import com.example.project_just4gamers.utils.Constants;

public class MainActivity extends AppCompatActivity {

    private TextView tv_welcome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComp();

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.getPREFS(), Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Constants.getUSERNAME(), "");
        tv_welcome.setText(getString(R.string.tv_main_welcome,username));


    }

    private void initComp() {
        tv_welcome = findViewById(R.id.tv_main_hello);
    }


}