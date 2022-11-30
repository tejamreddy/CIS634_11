package com.android.messenger.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.android.messenger.Activites.Chat;
import com.android.messenger.Activites.GroupChat;
import com.android.messenger.Model.Message;
import com.android.messenger.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.android.messenger.Activites.Chat.MY_PREFS_NAME;
import static com.android.messenger.Activites.Chat.decrypt;
import static com.android.messenger.Activites.Chat.encrypt;

public class DialogAskEdpGroup   extends Dialog implements
        android.view.View.OnClickListener {

    public Context context;
    EditText edt_edp;
    Button btnCancel, btnDone;
    String edp;
    String OrignalEdp;
    List<Message> messageList;

    public DialogAskEdpGroup(@NonNull Context context, String OrignalEdp, List<Message> messageList) {
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
                            GroupChat.recyclerView_chat.setAdapter(allMsgAdapter);
                            allMsgAdapter.notifyDataSetChanged();
                            GroupChat.recyclerView_chat.scrollToPosition(messageList.size()-1);

                            SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                            editor.putString("status", "decrypted");
                            editor.apply();

                            Drawable drawable = context.getResources().getDrawable(R.drawable.ic_unlock);
                            GroupChat.tv_encryption.setBackground(drawable);

                            GroupChat.EDstatus = "decrypted";

                            dismiss();


                        } else if(status.equals("decrypted")){
                            // Encrypt chat here

                            for(Message message : messageList){
                                message.msg = encrypt(message.msg);
                            }

                            AllMsgAdapter allMsgAdapter = new AllMsgAdapter(context,messageList);
                            GroupChat.recyclerView_chat.setAdapter(allMsgAdapter);
                            allMsgAdapter.notifyDataSetChanged();
                            GroupChat.recyclerView_chat.scrollToPosition(messageList.size()-1);


                            SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                            editor.putString("status", "encrypted");
                            editor.apply();

                            Drawable drawable = context.getResources().getDrawable(R.drawable.ic_lock);
                            GroupChat.tv_encryption.setBackground(drawable);

                            GroupChat.EDstatus = "encrypted";

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

