package com.android.messenger.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.android.messenger.Helpers.DatabaseRef;
import com.android.messenger.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfirmDeleteAllMessages extends Dialog implements
        View.OnClickListener {


    public Dialog d;
    public Button yes, no;
    public Context context;
    public String chatId;
    public String senderUid;
    public String receiverUid;
    Button BtnDeletAllCancel, btnDeleteAll;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference("images");

    public ConfirmDeleteAllMessages(@NonNull Context context, String chatId, String senderUid, String receiverUid) {
        super(context);
        this.context=context;
        this.chatId=chatId;
        this.senderUid=senderUid;
        this.receiverUid=receiverUid;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.deleteallconfirmdialogue);
        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:

                DatabaseRef.dbref_inbox.child(senderUid).child(chatId).removeValue();
                DatabaseRef.dbref_inbox.child(receiverUid).child(chatId).removeValue();
                DatabaseRef.dbref_chatinfo.child(chatId).removeValue();
                DatabaseRef.dbref_messages.child(chatId).removeValue();


                dismiss();

                break;
            case R.id.btn_no:
                dismiss();


                break;
            default:
                break;
        }
        dismiss();
    }






}