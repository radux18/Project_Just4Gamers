package com.example.project_just4gamers.ui.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.project_just4gamers.R;
import com.google.android.material.snackbar.Snackbar;

public class ProgressDialogActivity extends AppCompatActivity {

    private Dialog progressDialog;
    private TextView tv_progress_text ;


    public void showErrorSnackBar(String message, boolean errorMessage){
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                message, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();

        if (errorMessage){
            snackbarView.setBackgroundColor(
                    ContextCompat.getColor(
                            ProgressDialogActivity.this,
                            R.color.redError
                    )
            );
        } else {
            snackbarView.setBackgroundColor(
                    ContextCompat.getColor(
                            ProgressDialogActivity.this,
                            R.color.orderStatusDelivered
                    )
            );
        }
        snackbar.show();
    }

    public void showProgressDialog(String text){
        progressDialog = new Dialog(ProgressDialogActivity.this);
        progressDialog.setContentView(R.layout.progress_dialog_layout);

        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        progressDialog.show();
    }

    public void hideProgressDialog(){
        progressDialog.dismiss();
    }
}
