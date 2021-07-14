package com.example.project_just4gamers.userInterface.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.project_just4gamers.R;
import com.example.project_just4gamers.firestore.CloudFirestoreManager;
import com.example.project_just4gamers.models.Message;
import com.example.project_just4gamers.models.User;
import com.example.project_just4gamers.utilities.Constants;
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
    private User receiverUser = null;

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

        if (selectedMessage){
            btnRaspunde.setVisibility(View.VISIBLE);
            getCurrentUserV0();
            getSenderUser();

            btnRaspunde.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), AddMessageActivity.class);
                        intent.putExtra(Constants.getExtraSenderUser(), senderUser);
                        startActivity(intent);
                }
            });
        } else {
            btnRaspunde.setVisibility(View.GONE);
            getCurrentUserV1();
            getReceiverUser();
        }

        setupUI(message);
    }

    private void getSenderUser() {
        new CloudFirestoreManager().getSenderUser(MessageViewActivity.this, message.getSender_id());
    }

    private void getCurrentUserV0() {
        new CloudFirestoreManager().getCurrentDetailsV0(MessageViewActivity.this);
    }

    public void successGetUserV0(User user){
        currentUser = user;
        tvDestinatar.setText(getString(R.string.tv_settings_name, currentUser.getFirstName(), currentUser.getLastName()));
    }

    public void successGetSenderUser(User user) {
        senderUser = user;
        tvExpeditor.setText(getString(R.string.tv_settings_name, senderUser.getFirstName(), senderUser.getLastName()));
    }

    private void getCurrentUserV1() {
        new CloudFirestoreManager().getCurrentDetailsV1(MessageViewActivity.this);
    }

    public void successGetUserV1(User user){
        currentUser = user;
        tvExpeditor.setText(getString(R.string.tv_settings_name, currentUser.getFirstName(), currentUser.getLastName()));
    }

    private void getReceiverUser() {
        new CloudFirestoreManager().getReceiverUser(MessageViewActivity.this, message.getReceiver_id());
    }

    public void successGetReceiverUser(User user) {
        receiverUser = user;
        tvDestinatar.setText(getString(R.string.tv_settings_name, receiverUser.getFirstName(), receiverUser.getLastName()));
    }

    private void initComp() {
        toolbarMesaj = findViewById(R.id.toolbar_message);
        tvDestinatar = findViewById(R.id.tv_message_destinatar);
        tvExpeditor = findViewById(R.id.tv_message_expeditor);
        tietTitlu = findViewById(R.id.tiet_message_titlu);
        tietDescriere = findViewById(R.id.tiet_message_descriere);
        btnRaspunde = findViewById(R.id.btn_raspunde);
    }

    private void setupUI(Message message) {
        tietTitlu.setText(message.getTitle());
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