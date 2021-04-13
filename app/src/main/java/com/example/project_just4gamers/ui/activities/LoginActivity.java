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

public class LoginActivity extends ProgressDialogActivity {

    private boolean doubleBackToExitPressedOnce = false;
    private TextView tv_registerBtn;
    private TextInputEditText tiet_email;
    private TextInputEditText tiet_password;
    private TextView tiet_passwordForgot;
    private Button btnLogin;
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

        tv_registerBtn.setOnClickListener(registerEventListener());

        btnLogin.setOnClickListener(loginEventListener());
        loadData();
        updateViews();

        tiet_passwordForgot.setOnClickListener(forgotPassEvListener());
    }

    private View.OnClickListener loginEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateLoginEntries()){
                    showProgressDialog(getString(R.string.tv_progress_textT));
                    if (cb_remember.isChecked()){
                        saveData();
                    }
                    String email = tiet_email.getText().toString().trim();
                    String password = tiet_password.getText().toString().trim();
                    fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirestoreManager fStore = new FirestoreManager();
                                fStore.getUserDetails(LoginActivity.this);

                            } else {
                                hideProgressDialog();
                                showErrorSnackBar(task.getException().getMessage(),true);
                            }
                        }
                    });
                }
            }
        };
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.getPREFS(),MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EMAIL,tiet_email.getText().toString());
        editor.putString(PASSWORD,tiet_password.getText().toString());
        editor.apply();
    }

    private boolean validateLoginEntries() {
        String email = tiet_email.getText().toString().trim();
        String password = tiet_password.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            tiet_email.setError("Email is required.");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            tiet_password.setError("Password is required.");
            return false;
        }
        if (password.length() < 6) {
            tiet_password.setError("Password must have at least 6 Characters.");
            return false;
        }
        return true;
    }

    private View.OnClickListener forgotPassEvListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener registerEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        };
    }

    public void doubleBackToExit() {
        if (doubleBackToExitPressedOnce){
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;

        Toast.makeText(getApplicationContext(),R.string.please_click_back_again_to_exit,Toast.LENGTH_SHORT).show();

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
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.getPREFS(),MODE_PRIVATE);
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
        cb_remember = findViewById(R.id.cb_login_remember);
        fAuth = FirebaseAuth.getInstance();
    }


}