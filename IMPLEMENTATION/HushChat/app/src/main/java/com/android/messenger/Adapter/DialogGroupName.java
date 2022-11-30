package com.android.messenger.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.android.messenger.Activites.GroupChat;
import com.android.messenger.Activites.NewGroup;
import com.android.messenger.Helpers.CurrentUser;
import com.android.messenger.Helpers.DatabaseRef;
import com.android.messenger.Model.ChatInfo;
import com.android.messenger.Model.Message;
import com.android.messenger.Model.User;
import com.android.messenger.R;

import java.util.Calendar;
import java.util.TimeZone;

public class DialogGroupName extends Dialog implements
        android.view.View.OnClickListener {

    public Context context;
    EditText edt_group_name;
    Button btnCancel, btnCreate;
    String groupName;
    long timestamp;
    Calendar calendar;


    public DialogGroupName(@NonNull Context context) {
        super(context);
        this.context = context;


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_group_info);

        edt_group_name = (EditText) findViewById(R.id.edt_group_name);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCreate = (Button) findViewById(R.id.btnCreate);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupName = edt_group_name.getText().toString().trim();
                if (groupName.length() < 1) {
                    edt_group_name.setError("Please type few characters");
                } else {

                    DialogLoadingAdapter dialogLoadingAdapter = new DialogLoadingAdapter(context);
                    dialogLoadingAdapter.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogLoadingAdapter.setCancelable(false);
                    dialogLoadingAdapter.setCanceledOnTouchOutside(false);
                    dialogLoadingAdapter.show();


                    String groupId = DatabaseRef.dbref_chatinfo.push().getKey();
                    calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    timestamp = calendar.getTimeInMillis();

                    ChatInfo chatInfo = new ChatInfo(groupId, CurrentUser.userInfo.username + " created the group", "groupevent", CurrentUser.userInfo.uid, groupName, timestamp, true);
                    chatInfo.lastMsg = GroupChat.encrypt(chatInfo.lastMsg);

                    DatabaseRef.dbref_chatinfo.child(groupId).setValue(chatInfo);

                    String msgId = DatabaseRef.dbref_messages.push().getKey();
                    Message message = new Message(msgId, CurrentUser.userInfo.username + " created the group", "groupevent", CurrentUser.userInfo.uid,CurrentUser.userInfo.username, "", groupId, timestamp);
                    message.msg = GroupChat.encrypt(message.msg);

                    DatabaseRef.dbref_messages.child(groupId).child(msgId).setValue(message);

                    for (User user : NewGroup.selectedUserList) {
                        DatabaseRef.dbref_groupinfo.child(groupId).child("members").child(user.uid).setValue(timestamp);
                    }

                    DatabaseRef.dbref_groupinfo.child(groupId).child("info").child("admins").child(CurrentUser.userInfo.uid).setValue(timestamp);
                    DatabaseRef.dbref_groupinfo.child(groupId).child("info").child("name").setValue(groupName);
                    DatabaseRef.dbref_groupinfo.child(groupId).child("info").child("groupid").setValue(groupId);
                    DatabaseRef.dbref_groupinfo.child(groupId).child("info").child("groupimage").setValue(groupId);


                    for (User user : NewGroup.selectedUserList) {
                        DatabaseRef.dbref_inbox.child(user.uid).child(groupId).setValue(timestamp);
                    }

                    DatabaseRef.dbref_inbox.child(CurrentUser.userInfo.uid).child(groupId).setValue(timestamp);

                    dialogLoadingAdapter.dismiss();

                    Intent intent_groupchat = new Intent(context, GroupChat.class);
                    intent_groupchat.putExtra("group",chatInfo);
                    context.startActivity(intent_groupchat);

                }
            }
        });


    }

    @Override
    public void onClick(View v) {

    }


}
