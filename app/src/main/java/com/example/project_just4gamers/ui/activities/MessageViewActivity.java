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
        //TODO : DE REZOLVAT MESAJELE REPLY/ SAU NU REPLY
        //mesaj primit
        if (selectedMessage){
            btnRaspunde.setVisibility(View.VISIBLE);
        } else {
            btnRaspunde.setVisibility(View.GONE);
        }

        setupUI(message);

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
        tvDestinatar.setText(message.getReceiver_id());
        tvExpeditor.setText(message.getSender_id());
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