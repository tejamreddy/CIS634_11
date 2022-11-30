package com.android.messenger.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.android.messenger.Activites.NewChat;
import com.android.messenger.Adapter.DialogLoadingAdapter;
import com.android.messenger.Adapter.InboxAdapter;
import com.android.messenger.Helpers.CurrentUser;
import com.android.messenger.Helpers.DatabaseRef;
import com.android.messenger.Model.ChatInfo;
import com.android.messenger.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;



public class ChatFragment extends Fragment {


    TextView txtusername;
    CircleImageView profileimageview;
    EditText edtSearch;
    Button btnStartNewChat;
    Context context;
    RecyclerView recyclerViewRecentChat;
    List<String> chatIdList  = new ArrayList<String>();
    int counter =1;
    int counter2 = 0;
    int countAllChats2;
    List<ChatInfo> chatInfoList  = new ArrayList<ChatInfo>();
    LinearLayout layout_noResult;

    DialogLoadingAdapter dialogLoadingAdapter;

    public  String EDstatus="";
    public  final String MY_PREFS_NAME = "EncSetup";

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        init(view);

        dialogLoadingAdapter = new DialogLoadingAdapter(context);
        dialogLoadingAdapter.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogLoadingAdapter.setCancelable(false);
        dialogLoadingAdapter.setCanceledOnTouchOutside(false);
        dialogLoadingAdapter.show();


        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        EDstatus = prefs.getString("status", "encrypted");



        recyclerViewRecentChat.setLayoutManager(new LinearLayoutManager(context));


        DatabaseRef.dbref_inbox.child(CurrentUser.userInfo.uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final long countAllChats = dataSnapshot.getChildrenCount();
//                    Log.d("chatId","countAllChats: "+countAllChats);
                counter = 1;

                if(countAllChats == 0){
                    dialogLoadingAdapter.dismiss();
                }

                chatIdList.clear();

                for(final DataSnapshot dataSnapshot_individualChat : dataSnapshot.getChildren()){

                    chatInfoList.clear();

                    String chatId = String.valueOf(dataSnapshot_individualChat.getKey());
                    chatIdList.add(chatId);
//                        Log.d("chatId","chatId: "+chatId);

                    if(counter == countAllChats){

                        counter2 = -1;
                        countAllChats2 = chatIdList.size();

                        for(String chatid : chatIdList){
                            counter2++;
//                                Log.d("chatId",chatId);
                            // dialogLoadingAdapter.dismiss();

                            DatabaseRef.dbref_chatinfo.child(chatid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    ChatInfo chatInfo = dataSnapshot.getValue(ChatInfo.class);

                                    if(EDstatus.equals("decrypted")){
                                        chatInfo.lastMsg = decrypt(chatInfo.lastMsg);
                                    }


//                                        Log.d("chatId",chatInfo.lastMsg);
                                    chatInfoList.add(chatInfo);


                                    if(counter2 == countAllChats2 -1 ) {
                                        InboxAdapter inboxAdapter = new InboxAdapter(context, chatInfoList);
                                        recyclerViewRecentChat.setAdapter(inboxAdapter);
                                        inboxAdapter.notifyDataSetChanged();
                                        dialogLoadingAdapter.dismiss();
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    }

                    counter++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        txtusername.setText(CurrentUser.userInfo.username);

        if(!CurrentUser.userInfo.profileImageUrl.equals("default")) {
            Glide.with(context).load(CurrentUser.userInfo.profileImageUrl).placeholder(R.drawable.default_user_img).into(profileimageview);
        }






        btnStartNewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent_newchat = new Intent(context, NewChat.class);
                startActivity(intent_newchat);
            }
        });


        return view;
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }




    public void init(View view){

        txtusername = (TextView)view.findViewById(R.id.txtusername);
        profileimageview = (CircleImageView)view.findViewById(R.id.profileimageview);
        edtSearch = (EditText)view.findViewById(R.id.edtSearch);
        btnStartNewChat = (Button)view.findViewById(R.id.btnStartNewChat);
        recyclerViewRecentChat = (RecyclerView)view.findViewById(R.id.recyclerViewRecentChat);
        layout_noResult = (LinearLayout)view.findViewById(R.id.layout_noResult);

        context = getActivity();

    }



    public static String encrypt(String value) {
        String key = "aesEncryptionKey";
        String initVector = "aesEncryptionKey";
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.encodeToString(encrypted,Base64.CRLF);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }



    public static String decrypt(String value) {
        String key = "aesEncryptionKey";
        String initVector = "aesEncryptionKey";
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Base64.decode(value,Base64.CRLF));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }



}
