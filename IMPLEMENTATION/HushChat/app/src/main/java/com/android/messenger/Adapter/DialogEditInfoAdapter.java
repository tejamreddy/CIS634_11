package com.android.messenger.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.messenger.Fragment.ProfileFragment;
import com.android.messenger.Helpers.CurrentUser;
import com.android.messenger.Helpers.DatabaseRef;
import com.android.messenger.Model.User;
import com.android.messenger.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class DialogEditInfoAdapter extends Dialog implements
        android.view.View.OnClickListener {



    private Context context;
    private  TextView tv_info_to_update;
    private   EditText edt_info;
    private Button btnCancel,btnUpdate;
    private DatabaseReference dbref_update_info;
    private String text_for_edt_info,text_for_tv_info_to_update;




    public DialogEditInfoAdapter(@NonNull Context context, String text_for_tv_info_to_update,String text_for_edt_info,DatabaseReference dbref_update_info) {
        super(context);

        this.context=context;
        this.dbref_update_info = dbref_update_info;
        this.text_for_edt_info = text_for_edt_info;
        this.text_for_tv_info_to_update = text_for_tv_info_to_update;


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_edit_info);

        init();

        this.edt_info.setText(text_for_edt_info);
        this.tv_info_to_update.setText(text_for_tv_info_to_update);


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info  = edt_info.getText().toString();
                if(info.length()<1){
                    edt_info.setError("Please Type Few Characters");
                    return;
                }
                dbref_update_info.setValue(info);
                dismiss();
                DatabaseRef.dbref_users.child(CurrentUser.userInfo.uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        CurrentUser.setUser(dataSnapshot.getValue(User.class));
                        ProfileFragment.SetUserUpdatedInfo();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

    }

    @Override
    public void onClick(View v) {

    }


    private void init(){
        tv_info_to_update = (TextView)findViewById(R.id.tv_info_to_update);
        edt_info = (EditText)findViewById(R.id.edt_info);
        btnCancel = (Button)findViewById(R.id.btnCancel);
        btnUpdate = (Button)findViewById(R.id.btnUpdate);
    }

}