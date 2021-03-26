package com.example.project_just4gamers.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_just4gamers.firestore.FirestoreManager;
import com.example.project_just4gamers.R;
import com.example.project_just4gamers.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce = false;
    private TextView tv_registerBtn;
    private TextInputEditText tiet_email;
    private TextInputEditText tiet_password;
    private TextView tiet_passwordForgot;
    private  Button btnLogin;
    private ProgressBar progressBar;
    private FirebaseAuth fAuth;
    private CheckBox cb_remember;

    private String email;
    private String password;
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            final WindowInsetsController insetsController = getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.hide(WindowInsets.Type.statusBars());
            }
        } else {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }

        initComponents();
        FirebaseApp.initializeApp(this);


        //to the register activity
        tv_registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = tiet_email.getText().toString().trim();
                String password = tiet_password.getText().toString().trim();

                //validari
                if (TextUtils.isEmpty(email)) {
                    tiet_email.setError("Email is required.");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    tiet_password.setError("Password is required.");
                    return;
                }
                if (password.length() < 6) {
                    tiet_password.setError("Password must have at least 6 Characters.");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                if (cb_remember.isChecked()){
                    saveData();
                }

                //autentificare user
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirestoreManager fStore = new FirestoreManager();
                            fStore.getUserDetails(LoginActivity.this);


                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            private void saveData() {
                SharedPreferences sharedPreferences = getSharedPreferences(Constants.getPREFS(),MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(EMAIL,tiet_email.getText().toString());
                editor.putString(PASSWORD,tiet_password.getText().toString());
                editor.apply();
            }
        });

        loadData();
        updateViews();

        tiet_passwordForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });


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

    private void loadData() {
        SharedPreferences sharedPreferences= getSharedPreferences(Constants.getPREFS(),MODE_PRIVATE);
        email= sharedPreferences.getString(EMAIL,"");
        password= sharedPreferences.getString(PASSWORD,"");
    }

    private void updateViews() {
        tiet_email.setText(email);
        tiet_password.setText(password);
    }

    private void initComponents() {
        tv_registerBtn = findViewById(R.id.tv_login_registerBtn);
        tiet_email = findViewById(R.id.tiet_login_email);
        tiet_password = findViewById(R.id.tiet_login_password);
        tiet_passwordForgot = findViewById(R.id.tv_login_forgotPassword);
        btnLogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.pb_loading);
        cb_remember = findViewById(R.id.cb_login_remember);
        fAuth = FirebaseAuth.getInstance();
    }


}