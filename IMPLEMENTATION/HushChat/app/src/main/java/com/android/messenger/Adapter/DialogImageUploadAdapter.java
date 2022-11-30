package com.android.messenger.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.messenger.R;

public class DialogImageUploadAdapter extends Dialog implements
        android.view.View.OnClickListener {

    public Context context;
    public static TextView txtProgress;


    public DialogImageUploadAdapter(@NonNull Context context) {
        super(context);
        this.context = context;


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_uploading_image);
        txtProgress = (TextView) findViewById(R.id.txt_progress);


    }

    @Override
    public void onClick(View v) {

    }


}
