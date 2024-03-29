package com.example.project_just4gamers.userInterface.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_just4gamers.R;
import com.example.project_just4gamers.firestore.CloudFirestoreManager;
import com.example.project_just4gamers.models.Message;
import com.example.project_just4gamers.models.User;
import com.example.project_just4gamers.utilities.Constants;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddMessageActivity extends AppCompatActivity {

    private Intent intent;
    private User productOwner = null;
    private User currentUser = null;
    private User senderUser = null;
    private TextView tvOwnerName;
    private TextView tvSenderName;
    private TextInputEditText tietDescription;
    private TextInputEditText tietTitle;
    private Toolbar toolbarAddMessage;
    private Button btnSendMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_message);
        initComp();
        intent = getIntent();
        setupActionBar();

        if (intent.hasExtra(Constants.getExtraDetailsMessage())){
            productOwner = intent.getParcelableExtra(Constants.getExtraDetailsMessage());
            getCurrentUser();
            setupUiV0(productOwner);
        }

        if (intent.hasExtra(Constants.getExtraSenderUser())){
                senderUser = intent.getParcelableExtra(Constants.getExtraSenderUser());
                getCurrentUser();
                setupUiV1(senderUser);
        }

    }

    public void getCurrentUser(){
        new CloudFirestoreManager().getCurrentDetailsV0(AddMessageActivity.this);
    }

    public void getCurrentUserDetails(User user) {
        currentUser = user;
        tvSenderName.setText(getString(R.string.tv_settings_name, currentUser.getFirstName(), currentUser.getLastName()));
    }

    private void setupUiV0(User productOwner) {
        tvOwnerName.setText(getString(R.string.tv_settings_name, productOwner.getFirstName(), productOwner.getLastName()));
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate())
               saveMessageToFirestore();
            }
        });
    }

    private boolean validate() {
        String titlu = tietTitle.getText().toString();
        String content = tietDescription.getText().toString();

        if (TextUtils.isEmpty(titlu)){
            tietTitle.setError("Completeaza titlul");
            return false;
        }
        if (TextUtils.isEmpty(content)){
            tietTitle.setError("Completeaza descrierea");
            return false;
        }
        return true;
    }

    private void saveMessageToFirestore() {
        String title = tietTitle.getText().toString();
        String description = tietDescription.getText().toString();

        String dateFormat = "dd/MM/yyyy HH:mm";
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        String date = formatter.format(calendar.getTime());

        Message message = new Message(currentUser.getId(), productOwner.getId(), description, title, date);

        new CloudFirestoreManager().addMessageToFirestore(AddMessageActivity.this, message);

    }

    private void setupUiV1(User senderUser) {
        tvOwnerName.setText(getString(R.string.tv_settings_name, senderUser.getFirstName(), senderUser.getLastName()));
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate())
               saveMessageToFirestoreV1();
            }
        });
    }

    private void saveMessageToFirestoreV1() {
        String title = tietTitle.getText().toString();
        String description = tietDescription.getText().toString();

        String dateFormat = "dd/MM/yyyy HH:mm";
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        String date = formatter.format(calendar.getTime());

        Message message = new Message(currentUser.getId(), senderUser.getId(), description, title, date);

        new CloudFirestoreManager().addMessageToFirestore(AddMessageActivity.this, message);

    }

    private void initComp() {
        toolbarAddMessage = findViewById(R.id.toolbar_addMessage);
        tvOwnerName = findViewById(R.id.tv_message_receiverName);
        tietDescription = findViewById(R.id.tiet_addMessage_description);
        tietTitle = findViewById(R.id.tiet_addMessage_title);
        btnSendMessage = findViewById(R.id.btn_sendMessage);
        tvSenderName = findViewById(R.id.tv_message_senderName);
    }

    private void setupActionBar(){
        setSupportActionBar(toolbarAddMessage);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp);
        }

        toolbarAddMessage.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void successMessageSent() {
        Toast.makeText(getApplicationContext() ,"Mesajul a fost trimis cu succes!", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void getUserDetails(User user) {
        currentUser = user;
    }
}