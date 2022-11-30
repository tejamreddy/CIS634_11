package com.android.messenger.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.messenger.Helpers.CurrentUser;
import com.android.messenger.Helpers.DatabaseRef;
import com.android.messenger.R;

public class DialogSetupEdPass extends Dialog implements
        android.view.View.OnClickListener {

    public Context context;
    EditText edt_edp;
    Button btnCancel, btnDone;
    String edp;

    public DialogSetupEdPass(@NonNull Context context) {
        super(context);
        this.context = context;


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_setupedpass);

        edt_edp = (EditText) findViewById(R.id.edt_edp);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnDone = (Button) findViewById(R.id.btnDone);


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edp = edt_edp.getText().toString().trim();
                if (edp.length() < 1) {
                    edt_edp.setError("Please type password");
                } else {

                    DatabaseRef.dbref_ChatEdPassByUid.child(CurrentUser.userInfo.uid).child("EDP").setValue(edp);

                    Toast toast = Toast.makeText(context,"Setup successfully !",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();

                    dismiss();

                }


            }
        });

    }

    @Override
    public void onClick(View v) {

    }

}

