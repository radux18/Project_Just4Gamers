package com.example.project_just4gamers.ui.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_just4gamers.R;
import com.example.project_just4gamers.firestore.FirestoreManager;
import com.example.project_just4gamers.models.Message;
import com.example.project_just4gamers.models.User;
import com.example.project_just4gamers.utils.Constants;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddMessageActivity extends AppCompatActivity {

    private Intent intent;
    private User productOwner = null;
    private User currentUser = null;
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
        }

        getCurrentUser();
        setupUi(productOwner);
    }

    public void getCurrentUser(){
        new FirestoreManager().getCurrentDetailsV0(AddMessageActivity.this);
    }

    public void getCurrentUserDetails(User user) {
        currentUser = user;
        tvSenderName.setText(getString(R.string.tv_settings_name, currentUser.getFirstName(), currentUser.getLastName()));
    }

    private void setupUi(User productOwner) {
        tvOwnerName.setText(getString(R.string.tv_settings_name, productOwner.getFirstName(), productOwner.getLastName()));
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               saveMessageToFirestore();
            }
        });
    }

    private void saveMessageToFirestore() {
        String title = tietTitle.getText().toString();
        String description = tietDescription.getText().toString();

        String dateFormat = "dd/MM/yyyy HH:mm";
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        String date = formatter.format(calendar.getTime());

        Message message = new Message(currentUser.getId(), productOwner.getId(), description, title, date);

        new FirestoreManager().addMessageToFirestore(AddMessageActivity.this, message);

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
        Toast.makeText(getApplicationContext() ,"The message was sent successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void getUserDetails(User user) {
        currentUser = user;
    }
}