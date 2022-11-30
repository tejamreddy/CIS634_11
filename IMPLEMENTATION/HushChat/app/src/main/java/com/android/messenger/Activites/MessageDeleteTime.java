package com.android.messenger.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.android.messenger.Helpers.CurrentUser;
import com.android.messenger.Helpers.DatabaseRef;
import com.android.messenger.R;

import adil.dev.lib.materialnumberpicker.dialog.NumberPickerDialog;

public class MessageDeleteTime extends AppCompatActivity {

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_delete_time);
        context = this;



        try {
            NumberPickerDialog numberPickerDialog = new NumberPickerDialog(context, 1, 60, new NumberPickerDialog.NumberPickerCallBack() {
                @Override
                public void onSelectingValue(int value) {

                    DatabaseRef.dbref_MessagesDeleteTime.child(CurrentUser.userInfo.uid).setValue(value);
                    CurrentUser.userMsgDeleteTime = value;
                   // Toast.makeText(context, "Your all messages will be deleted after "+value+" minutes", Toast.LENGTH_LONG).show();


                }
            });
            numberPickerDialog.show();
        }catch (Exception e){
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }
}
