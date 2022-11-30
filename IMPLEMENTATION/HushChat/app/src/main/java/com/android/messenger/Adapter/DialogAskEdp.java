package com.android.messenger.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.messenger.Activites.Chat;
import com.android.messenger.Helpers.CurrentUser;
import com.android.messenger.Helpers.DatabaseRef;
import com.android.messenger.Model.Message;
import com.android.messenger.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.android.messenger.Activites.Chat.MY_PREFS_NAME;
import static com.android.messenger.Activites.Chat.decrypt;
import static com.android.messenger.Activites.Chat.encrypt;

public class DialogAskEdp  extends Dialog implements
        android.view.View.OnClickListener {

    public Context context;
    EditText edt_edp;
    Button btnCancel, btnDone;
    String edp;
    String OrignalEdp;
    List<Message> messageList;

    public DialogAskEdp(@NonNull Context context, String OrignalEdp, List<Message> messageList) {
        super(context);
        this.context = context;
        this.OrignalEdp = OrignalEdp;
        this.messageList = messageList;


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_askedp);

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

                    if(edp.equals(OrignalEdp)){

                        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                        String status = prefs.getString("status", "encrypted");

                        // Ask for password and macth it with the EDP

                        // if password matched , then perform task accordingly

                        if(status.equals("encrypted")){
                            // Decrypt chat here


                            for(Message message : messageList){
                                message.msg = decrypt(message.msg);
                            }

                            AllMsgAdapter allMsgAdapter = new AllMsgAdapter(context,messageList);
                            Chat.recyclerView_chat.setAdapter(allMsgAdapter);
                            allMsgAdapter.notifyDataSetChanged();
                            Chat.recyclerView_chat.scrollToPosition(messageList.size()-1);

                            SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                            editor.putString("status", "decrypted");
                            editor.apply();

                            Drawable drawable = context.getResources().getDrawable(R.drawable.ic_unlock);
                            Chat.tv_encryption.setBackground(drawable);

                            Chat.EDstatus = "decrypted";

                            dismiss();


                        } else if(status.equals("decrypted")){
                            // Encrypt chat here

                            for(Message message : messageList){
                                message.msg = encrypt(message.msg);
                            }

                            AllMsgAdapter allMsgAdapter = new AllMsgAdapter(context,messageList);
                            Chat.recyclerView_chat.setAdapter(allMsgAdapter);
                            allMsgAdapter.notifyDataSetChanged();
                            Chat.recyclerView_chat.scrollToPosition(messageList.size()-1);


                            SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                            editor.putString("status", "encrypted");
                            editor.apply();

                            Drawable drawable = context.getResources().getDrawable(R.drawable.ic_lock);
                            Chat.tv_encryption.setBackground(drawable);

                            Chat.EDstatus = "encrypted";

                            dismiss();

                        }


                    }else{

                        edt_edp.setError("Incorrect password");
                    }


                }


            }
        });

    }

    @Override
    public void onClick(View v) {

    }

}

