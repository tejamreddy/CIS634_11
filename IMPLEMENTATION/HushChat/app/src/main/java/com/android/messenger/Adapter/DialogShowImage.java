package com.android.messenger.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.android.messenger.R;

public class DialogShowImage  extends Dialog implements
        android.view.View.OnClickListener {


    public Dialog d;
    public Button no;
    public ImageView showimagehere;
    public Context context;
    public String url;

    public DialogShowImage(@NonNull Context context, String url) {
        super(context);
        this.context=context;
        this.url=url;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_show_image);

        no = (Button) findViewById(R.id.btn_no);
        no.setOnClickListener(this);
        showimagehere=(ImageView) findViewById(R.id.showimagehere);


        Glide.with(context)
                .load(url)
                .into(showimagehere);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_no:

                dismiss();

                break;

            default:
                break;
        }
        dismiss();
    }




}