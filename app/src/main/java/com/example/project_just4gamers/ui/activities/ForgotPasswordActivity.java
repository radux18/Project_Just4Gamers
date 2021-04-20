package com.example.project_just4gamers.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.project_just4gamers.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends ProgressDialogActivity {

    private TextInputEditText tiet_emailId;
    private Button btn_submit;
    private FirebaseAuth fAuth;
    private Toolbar tbForgotPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initComp();
        setupActionBar();

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

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = tiet_emailId.getText().toString().trim();
                if (email.isEmpty()) {
                    showErrorSnackBar("Oops. Te rog completeaza campul cu adresa de email!", true);
                } else {
                    showProgressDialog(getResources().getString(R.string.tv_progress_textT));
                    fAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            hideProgressDialog();
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Succes! S-a trimis un email de recuperare a parolei pe adresa dvs.", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                showErrorSnackBar("An error has appeared. Try again", true);
                            }
                        }
                    });
                }
            }
        });
    }

    private void setupActionBar() {
        setSupportActionBar(tbForgotPass);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp);
        }

        tbForgotPass.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initComp() {
        tiet_emailId = findViewById(R.id.tiet_emailId);
        btn_submit = findViewById(R.id.btn_submit);
        tbForgotPass = findViewById(R.id.toolbar_forgotPass);
        fAuth = FirebaseAuth.getInstance();

    }


}