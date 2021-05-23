package com.example.project_just4gamers.userInterface.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.project_just4gamers.firestore.CloudFirestoreManager;
import com.example.project_just4gamers.R;
import com.example.project_just4gamers.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends ProgressDialogActivity {

    private EditText et_firstName;
    private EditText et_lastName;
    private EditText et_email;
    private EditText et_password;
    private EditText et_passwordConfirm;
    private TextView tv_loginBtn;
    private Button btn_register;
    private CheckBox cb_terms;

    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initComponents();
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


        tv_loginBtn.setOnClickListener(loginListener());
        btn_register.setOnClickListener(registerListener());

    }

    private View.OnClickListener loginListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        };
    }

    private View.OnClickListener registerListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString().trim();
                String password1 = et_password.getText().toString().trim();
                String firstName = et_firstName.getText().toString();
                String lastName = et_lastName.getText().toString();

                if (validateInputs()) {
                    showProgressDialog(getString(R.string.tv_progress_textT));
                    fAuth.createUserWithEmailAndPassword(email, password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                hideProgressDialog();
                                FirebaseUser fUser = fAuth.getCurrentUser();
                                assert fUser != null;
                                User user = new User(fUser.getUid(),
                                        firstName, lastName, email);

                                CloudFirestoreManager fStore = new CloudFirestoreManager();
                                fStore.registerUser(RegisterActivity.this, user);
                                Toast.makeText(getApplicationContext(),"Te-ai inregistrat cu succes!", Toast.LENGTH_SHORT).show();
                                fAuth.signOut();
                                finish();
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

    private void initComponents() {
        et_firstName = findViewById(R.id.et_register_prenume);
        et_lastName = findViewById(R.id.et_register_nume);
        et_email = findViewById(R.id.et_register_email);
        et_password = findViewById(R.id.et_register_password1);
        et_passwordConfirm = findViewById(R.id.et_register_password2);
        tv_loginBtn = findViewById(R.id.tv_register_loginBtn);
        btn_register = findViewById(R.id.btn_register);
        cb_terms = findViewById(R.id.cb_register_terms);
        fAuth = FirebaseAuth.getInstance();
    }

    private boolean validateInputs() {
        String email = et_email.getText().toString().trim();
        String password1 = et_password.getText().toString().trim();
        String password2 = et_passwordConfirm.getText().toString().trim();
        String firstName = et_firstName.getText().toString();
        String lastName = et_lastName.getText().toString();
        if (TextUtils.isEmpty(firstName)) {
            et_firstName.setError("First name is required.");
            return false;
        }
        if (TextUtils.isEmpty(lastName)) {
            et_lastName.setError("Last name is required.");
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            et_email.setError("Email is required.");
            return false;
        }
        if (TextUtils.isEmpty(password1)) {
            et_password.setError("Password is required.");
            return false;
        }
        if (TextUtils.isEmpty(password2)) {
            et_passwordConfirm.setError("Confirm password.");
            return false;
        }
        if (!password2.equals(password1)) {
            et_passwordConfirm.setError("The password is not the same.");
            return false;
        }
        if (password1.length() < 6) {
            et_password.setError("Password must have at least 6 Characters.");
            return false;
        }

        if (!cb_terms.isChecked()) {
            cb_terms.setError("You must accept terms and conditions.");
            return false;
        }
        return true;
    }
}