package com.example.project_just4gamers.ui.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.project_just4gamers.models.User;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_just4gamers.firestore.FirestoreManager;
import com.example.project_just4gamers.R;
import com.example.project_just4gamers.utils.Constants;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class LoginActivity extends ProgressDialogActivity {

    private boolean doubleBackToExitPressedOnce = false;
    private TextView tv_registerBtn;
    private EditText etEmail;
    private EditText etPassword;
    private TextView tiet_passwordForgot;
    private Button btnLogin;
    private FirebaseAuth fAuth;
    private CheckBox cb_remember;
    private LoginButton fbLoginButton;
    private String email;
    private String password;
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";

    private AccessTokenTracker accessTokenTracker;
    private FirebaseAuth.AuthStateListener authStateListener;
    private CallbackManager callbackManager;

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

        fbLoginButton.setPermissions("email", "public_profile");
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        getAllUsers();

    }

    private void getAllUsers() {
        new FirestoreManager().getAllUsersV4(LoginActivity.this);
    }

    public void successGetUsers(ArrayList<User> users) {
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                showProgressDialog(getString(R.string.tv_progress_textT));
                AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
                fAuth.signInWithCredential(credential).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = fAuth.getCurrentUser();
                            updateUI(user, users);
                        } else {
                            hideProgressDialog();
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            updateUI(null, null);
                        }
                    }
                });
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext() ,"Error with fb login: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    updateUI(user, users);
                } else {
                    updateUI(null, null);
                }
            }
        };

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null){
                    FirebaseAuth.getInstance().signOut();
                }
            }
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateUI(FirebaseUser user, ArrayList<User> users) {
        if (user != null){
            String[] name = user.getDisplayName().split(" ");
            String firstName = name[0];
            String lastName = name[1];
            String email = user.getEmail();
            String image = user.getPhotoUrl().toString();

            if (!containsID(users, user.getUid())) {
                User newUser = new User(user.getUid(),
                        firstName, lastName, email, image);
                new FirestoreManager().registerUser(LoginActivity.this, newUser);
            }

            new FirestoreManager().getUserDetails(LoginActivity.this);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean containsID(final ArrayList<User> list, final String id){
        return list.stream().anyMatch(o -> o.getId().equals(id));
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
                    String email = etEmail.getText().toString().trim();
                    String password = etPassword.getText().toString().trim();
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
        editor.putString(EMAIL,etEmail.getText().toString());
        editor.putString(PASSWORD,etPassword.getText().toString());
        editor.apply();
    }

    private boolean validateLoginEntries() {
          String email = etEmail.getText().toString().trim();
          String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required.");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required.");
            return false;
        }
        if (password.length() < 6) {
            etPassword.setError("Password must have at least 6 Characters.");
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
        etEmail.setText(email);
        etPassword.setText(password);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        fAuth.addAuthStateListener(authStateListener);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (authStateListener != null){
//            fAuth.removeAuthStateListener(authStateListener);
//        }
//    }



    private void initComponents() {
        tv_registerBtn = findViewById(R.id.tv_login_registerBtn);
        etPassword = findViewById(R.id.et_login_password);
        etEmail = findViewById(R.id.et_login_email);
        tiet_passwordForgot = findViewById(R.id.tv_login_forgotPassword);
        btnLogin = findViewById(R.id.btn_login);
        cb_remember = findViewById(R.id.cb_login_remember);
        fbLoginButton = findViewById(R.id.btn_fbLogin);
        fAuth = FirebaseAuth.getInstance();
    }



}