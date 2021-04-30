package com.example.project_just4gamers.ui.activities;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import com.example.project_just4gamers.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class DashboardActivity extends AppCompatActivity {
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        getSupportActionBar().setBackgroundDrawable(
                ContextCompat.getDrawable(
                        DashboardActivity.this,
                        R.color.teal_200
                )
        );

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_products, R.id.navigation_dashboard, R.id.navigation_orders, R.id.navigation_soldProducts)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    public void doubleBackToExit() {
        if (doubleBackToExitPressedOnce){
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(getApplicationContext(),R.string.please_click_back_again_to_exit,Toast.LENGTH_SHORT).show();
        //the case if the user press the button later than 2 seconds, the trigger resets.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public void onBackPressed() {
        doubleBackToExit();
    }
}