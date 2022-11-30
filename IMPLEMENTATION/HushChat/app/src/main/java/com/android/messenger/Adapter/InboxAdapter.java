package com.android.messenger.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.messenger.Model.Message;
import com.bumptech.glide.Glide;
import com.android.messenger.Activites.Chat;
import com.android.messenger.Activites.GroupChat;
import com.android.messenger.Helpers.CurrentUser;
import com.android.messenger.Helpers.DatabaseRef;
import com.android.messenger.Model.ChatInfo;
import com.android.messenger.Model.User;
import com.android.messenger.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.UserViewHolder> {

    public Context context;
    List<ChatInfo> chatInfoList;
    private SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
    User user;
    DialogLoadingAdapter dialogLoadingAdapter;
    List<String> userUsernameList = new ArrayList<String>();
    String chatId;
    long timestamp;
    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    long userTimeToDeleteMsg;
    FirebaseStorage mFirebaseStorage = FirebaseStorage.getInstance();
    StorageReference photoRef;

    public InboxAdapter(Context context, List<ChatInfo> chatInfoList) {
        this.context = context;
        this.chatInfoList = chatInfoList;
    }

    @NonNull
    @Override
    public InboxAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chatinfo, viewGroup, false);
        return new InboxAdapter.UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final InboxAdapter.UserViewHolder userViewHolder, int i) {
        final ChatInfo chatInfo = chatInfoList.get(i);


        if(chatInfo.isGroup){

            userViewHolder.tv_username.setText(chatInfo.receiverUid);
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(chatInfo.chatId);
            Glide.with(context).load(storageReference.getDownloadUrl()).placeholder(R.drawable.default_group_profile).into(userViewHolder.profileimageview);

        }else{

        String senderUid;
        if (chatInfo.senderUid.equals(CurrentUser.userInfo.uid)) {
            senderUid = chatInfo.receiverUid;
        } else {
            senderUid = chatInfo.senderUid;
        }


        DatabaseRef.dbref_users.child(senderUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);

                if (!user.profileImageUrl.equals("default")) {
                    Glide.with(context).load(user.profileImageUrl).placeholder(R.drawable.default_user_img).into(userViewHolder.profileimageview);
                }

                userViewHolder.tv_username.setText(user.username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


        userViewHolder.tv_lastmsg.setText(chatInfo.lastMsg);

        String  dateTime = sdf.format(new Date(chatInfo.timestamp));
        userViewHolder.tv_time.setText(dateTime);




    }

    @Override
    public int getItemCount() {
        return chatInfoList.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profileimageview;
        TextView tv_username, tv_lastmsg,tv_time;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            profileimageview = (CircleImageView) itemView.findViewById(R.id.profileimageview);
            tv_username = (TextView) itemView.findViewById(R.id.tv_username);
            tv_lastmsg = (TextView) itemView.findViewById(R.id.tv_lastmsg);
            tv_time= (TextView) itemView.findViewById(R.id.tv_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChatInfo chatInfo = chatInfoList.get(getAdapterPosition());

                    if(chatInfo.isGroup){

                        Intent intent_groupchat = new Intent(context, GroupChat.class);
                        intent_groupchat.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent_groupchat.putExtra("group", chatInfo);
                        context.startActivity(intent_groupchat);
                    }else {

                        dialogLoadingAdapter = new DialogLoadingAdapter(context);
                        dialogLoadingAdapter.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialogLoadingAdapter.setCancelable(false);
                        dialogLoadingAdapter.setCanceledOnTouchOutside(false);
                        dialogLoadingAdapter.show();


                        String senderUid;
                        if (chatInfo.senderUid.equals(CurrentUser.userInfo.uid)) {
                            senderUid = chatInfo.receiverUid;
                        } else {
                            senderUid = chatInfo.senderUid;
                        }
//

                        DatabaseRef.dbref_users.child(senderUid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                user = dataSnapshot.getValue(User.class);
                                dialogLoadingAdapter.dismiss();
                                Intent intent_chat = new Intent(context, Chat.class);
                                intent_chat.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent_chat.putExtra("user", user);
                                context.startActivity(intent_chat);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });



                    }



                }
            });


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {


                    ChatInfo chatInfo = chatInfoList.get(getAdapterPosition());
                    if(!chatInfo.isGroup){

                        ConfirmDeleteAllMessages confirmDeleteAllMessages = new ConfirmDeleteAllMessages(context,chatInfo.chatId,chatInfo.senderUid,chatInfo.receiverUid);
                        confirmDeleteAllMessages.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        confirmDeleteAllMessages.show();

                    }




                    return true;
                }
            });


        }

    }


}


