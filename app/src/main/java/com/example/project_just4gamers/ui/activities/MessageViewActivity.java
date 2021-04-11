package com.example.project_just4gamers.ui.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.project_just4gamers.R;
import com.example.project_just4gamers.firestore.FirestoreManager;
import com.example.project_just4gamers.models.Message;
import com.example.project_just4gamers.models.User;
import com.example.project_just4gamers.utils.Constants;
import com.google.android.material.textfield.TextInputEditText;

public class MessageViewActivity extends AppCompatActivity {

    private Intent intent ;
    private Toolbar toolbarMesaj;
    private TextView tvExpeditor;
    private TextView tvDestinatar;
    private TextInputEditText tietTitlu;
    private TextInputEditText tietDescriere;
    private Button btnRaspunde;

    private Message message = null;
    private boolean selectedMessage = false;
    private User currentUser = null;
    private User senderUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_view);
        initComp();
        intent = getIntent();
        setupActionBar();

        if (intent.hasExtra(Constants.getExtraSelectMessage() )){
            message = intent.getParcelableExtra(Constants.getExtraSelectMessage());
        }

        if (intent.hasExtra(Constants.getExtraBoolean())){
            selectedMessage = intent.getBooleanExtra(Constants.getExtraSelectMessage(), true);
        }

        //TODO : Sa incerci sa faci cele 2 selecturi pt ambele 2 cazuri
        //mesaje primite
        if (selectedMessage){
            btnRaspunde.setVisibility(View.VISIBLE);
            getCurrentUser();
            getSenderUser();
          //  tvExpeditor.setText(getString(R.string.tv_settings_name, senderUser.getFirstName(), senderUser.getLastName()));
           // tvDestinatar.setText(getString(R.string.tv_settings_name, currentUser.getFirstName(), currentUser.getLastName()));
        } else {
            //mesaje trimise
            btnRaspunde.setVisibility(View.GONE);
            getCurrentUser();

        }
        //setupUi(message);
        setupUI(message);
    }

    private void getSenderUser() {
        new FirestoreManager().getSenderUser(MessageViewActivity.this, message.getSender_id());
    }

    private void getCurrentUser() {
        new FirestoreManager().getCurrentDetails(MessageViewActivity.this);
    }

    public void successGetUser(User user){
        currentUser = user;
        tvDestinatar.setText(getString(R.string.tv_settings_name, currentUser.getFirstName(), currentUser.getLastName()));
    }


    private void initComp() {
        toolbarMesaj = findViewById(R.id.toolbar_message);
        tvDestinatar = findViewById(R.id.tv_message_destinatar);
        tvExpeditor = findViewById(R.id.tv_message_expeditor);
        tietTitlu = findViewById(R.id.tiet_message_titlu);
        tietDescriere = findViewById(R.id.tiet_message_descriere);
        btnRaspunde = findViewById(R.id.btn_raspunde);

    }

//    private void setupUiV1(Message message) {
//        tvDestinatar.setText(getString(R.string.tv_settings_name, currentUser.getFirstName(), currentUser.getLastName()));
//        tvExpeditor.setText(getString(R.string.tv_settings_name, senderUser.getFirstName(), senderUser.getLastName()));
//
//        tietTitlu.setText(message.getDescription());
//        tietDescriere.setText(message.getDescription());
//
//        tvDestinatar.setEnabled(false);
//        tvExpeditor.setEnabled(false);
//        tietTitlu.setEnabled(false);
//        tietDescriere.setEnabled(false);
//    }

    public void successGetSenderUser(User user) {
        senderUser = user;
        tvExpeditor.setText(getString(R.string.tv_settings_name, senderUser.getFirstName(), senderUser.getLastName()));
    }

    private void setupUI(Message message) {
        tietTitlu.setText(message.getDescription());
        tietDescriere.setText(message.getDescription());

        tvDestinatar.setEnabled(false);
        tvExpeditor.setEnabled(false);
        tietTitlu.setEnabled(false);
        tietDescriere.setEnabled(false);
    }


    private void setupActionBar() {
        setSupportActionBar(toolbarMesaj);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp);
        }

        toolbarMesaj.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }



}