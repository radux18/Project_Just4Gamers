package com.example.project_just4gamers.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputEditText tiet_emailId;
    private Button btn_submit;
    private ProgressBar pb_loading;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

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

        initComp();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = tiet_emailId.getText().toString().trim();
                if (email.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Error. Please write the correct email", Toast.LENGTH_SHORT).show();
                } else {
                    pb_loading.setVisibility(View.VISIBLE);
                    fAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            pb_loading.setVisibility(View.INVISIBLE);
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "You succesfully sent the email address", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "An error has appeared. Try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void initComp() {
        tiet_emailId = findViewById(R.id.tiet_emailId);
        btn_submit = findViewById(R.id.btn_submit);
        pb_loading = findViewById(R.id.pb_loadingBar);
        fAuth = FirebaseAuth.getInstance();
    }


}