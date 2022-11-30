package com.android.messenger.Activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.messenger.Adapter.DialogAskEdp;
import com.android.messenger.Adapter.DialogLoadingAdapter;
import com.android.messenger.Adapter.DialogSetupEdPass;
import com.bumptech.glide.Glide;
import com.android.messenger.Adapter.AllMsgAdapter;
import com.android.messenger.Adapter.DialogImageUploadAdapter;
import com.android.messenger.Helpers.CurrentUser;
import com.android.messenger.Helpers.DatabaseRef;
import com.android.messenger.Model.ChatInfo;
import com.android.messenger.Model.Message;
import com.android.messenger.Model.User;
import com.android.messenger.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TimeZone;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {

    Context context;
    TextView txtusername;
    public static TextView tv_encryption;
    CircleImageView profileimageview;
    Button btnCancel, btnmenu, btnSelectImage;
    EditText edt_msg;
    ImageView imgview_msgsend;
    public static RecyclerView recyclerView_chat;
    List<String> userUsernameList = new ArrayList<String>();
    String chatId;
    long timestamp;
    Calendar calendar;
    User userReceiver;
    List<Message> messageList = new ArrayList<Message>();
    private final int PICK_IMAGE_REQUEST = 71;
    StorageReference ref;
    Boolean isBlock=false,isImblocked=false;
    LinearLayout lytsend;
    Boolean currentUserBlockedRandomUser = true;
    Boolean randomUserBlockedCurrentUser = true;
    AllMsgAdapter allMsgAdapter;
    DialogAskEdp dialogAskEdp;
    DialogSetupEdPass dialogSetupEdPass;

    public static String EDstatus="";
    public static final String MY_PREFS_NAME = "EncSetup";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        // Cast the fields
        init();

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

         EDstatus = prefs.getString("status", "encrypted");

         if(EDstatus.equals("encrypted")){

            Drawable drawable = getResources().getDrawable(R.drawable.ic_lock);
            tv_encryption.setBackground(drawable);

         }
         else if(EDstatus.equals("decrypted")){

            Drawable drawable = getResources().getDrawable(R.drawable.ic_unlock);
            tv_encryption.setBackground(drawable);

        }


        // Get the receiver user from the intent
        userReceiver = (User) getIntent().getSerializableExtra("user");


        // Add both the users to the list so that we can generate the unique and same chatId for each time
        userUsernameList.add(CurrentUser.userInfo.username);
        userUsernameList.add(userReceiver.username);

        // Sort the list
        Collections.sort(userUsernameList, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareTo(rhs);
            }
        });

        // make chat id by jouning them with a plus symbol forexample username1+username2
        chatId = userUsernameList.get(0) + "+" + userUsernameList.get(1);

        // set the layout manager for the recyclerbin
        recyclerView_chat.setLayoutManager(new LinearLayoutManager(context));

        // set the username for the chat receiver
        txtusername.setText(userReceiver.username);

        // set the profile picture using the library glide from the url
        if (!userReceiver.profileImageUrl.equals("default")) {
            Glide.with(context).load(userReceiver.profileImageUrl).placeholder(R.drawable.default_user_img).into(profileimageview);
        }

        messageList.clear();
        allMsgAdapter = new AllMsgAdapter(context,messageList);
        recyclerView_chat.setAdapter(allMsgAdapter);
        allMsgAdapter.notifyDataSetChanged();

        // Load the chat into adapter and set the adapter to recyclerbin
        DatabaseRef.dbref_messages.child(chatId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message message = dataSnapshot.getValue(Message.class);

               if(EDstatus.equals("decrypted")){
                    message.msg = decrypt(message.msg);
                }

                   messageList.add(message);
                   allMsgAdapter.notifyDataSetChanged();
                   recyclerView_chat.scrollToPosition(messageList.size()-1);



            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        // chech if that user is blocked or not
        DatabaseRef.dbref_Blocks.child(CurrentUser.userInfo.uid).child(userReceiver.uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    isBlock=true;

                }else {
                    isBlock=false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // check if that user bloked me or not
        DatabaseRef.dbref_Blocks.child(userReceiver.uid).child(CurrentUser.userInfo.uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    isImblocked=true;

                }else {
                    isImblocked=false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        // open the menue on this button click
        btnmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final PopupMenu popup = new PopupMenu(context, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu1, popup.getMenu());

                // if the user is blocked change the menu item according to it
                if(isBlock){

                    popup.getMenu().getItem(0).setVisible(false);
                    popup.getMenu().getItem(1).setVisible(true);
                }else{

                    popup.getMenu().getItem(0).setVisible(true);
                    popup.getMenu().getItem(1).setVisible(false);
                }

                popup.show();

                // add the click listener for the menu item
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        // block the user
                        if(item.getTitle().equals("Block")){
                            DatabaseRef.dbref_Blocks.child(CurrentUser.userInfo.uid).child(userReceiver.uid).setValue(true);
                            Toast.makeText(context, "User Blocked", Toast.LENGTH_SHORT).show();
                            popup.dismiss();
                        }

                        // unblock the user
                        if(item.getTitle().equals("Unblock")){
                            DatabaseRef.dbref_Blocks.child(CurrentUser.userInfo.uid).child(userReceiver.uid).removeValue();
                            Toast.makeText(context, "User Unblocked", Toast.LENGTH_SHORT).show();
                            popup.dismiss();
                        }

                        return true;
                    }
                });


            }
        });





        // set the layout visibility according to the block or unblock
        DatabaseRef.dbref_Blocks.child(CurrentUser.userInfo.uid).child(userReceiver.uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if(dataSnapshot.exists()){
                    lytsend.setVisibility(View.GONE);
                    imgview_msgsend.setVisibility(View.GONE);
                    currentUserBlockedRandomUser = true;


                }else{
                    currentUserBlockedRandomUser = false;
                    if(!currentUserBlockedRandomUser & !randomUserBlockedCurrentUser){
                        lytsend.setVisibility(View.VISIBLE);
                        imgview_msgsend.setVisibility(View.VISIBLE);
                    }
                }

                DatabaseRef.dbref_Blocks.child(userReceiver.uid).child(CurrentUser.userInfo.uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            lytsend.setVisibility(View.GONE);
                            imgview_msgsend.setVisibility(View.GONE);
                            randomUserBlockedCurrentUser = true;
                        }else{
                            randomUserBlockedCurrentUser = false;
                            if(!currentUserBlockedRandomUser & !randomUserBlockedCurrentUser){
                                lytsend.setVisibility(View.VISIBLE);
                                imgview_msgsend.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






        // send the message
        imgview_msgsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get the message to send
                String msg = edt_msg.getText().toString().trim();

                // make sure the messege is not empty
                if (!msg.isEmpty()) {

                    msg = encrypt(msg);

                    // get the current timestamp in utc
                    calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    timestamp = calendar.getTimeInMillis();

                    // generate the unique id for each message
                    String messageId = DatabaseRef.dbref_messages.push().getKey();

                    // create the object for chatInfo to display information at inbox screen
                    ChatInfo chatInfo = new ChatInfo(chatId, msg, "text", CurrentUser.userInfo.uid,userReceiver.uid, timestamp, false);

                    // create the object of messge to add all the values to database once
                    Message message = new Message(messageId, msg, "text", CurrentUser.userInfo.uid,CurrentUser.userInfo.username, userReceiver.uid, chatId, timestamp);

                    // save message to database
                    DatabaseRef.dbref_messages.child(chatId).child(messageId).setValue(message);

                    // update the inbox information
                    DatabaseRef.dbref_chatinfo.child(chatId).setValue(chatInfo);

                    // update information for inbox screen of sender
                    DatabaseRef.dbref_inbox.child(CurrentUser.userInfo.uid).child(chatId).setValue(timestamp);

                    // update information for the inbox for receiver
                    DatabaseRef.dbref_inbox.child(userReceiver.uid).child(chatId).setValue(timestamp);

                    // make the messge edit text empty
                    edt_msg.setText("");


                }
            }
        });


        // go back to dashboard
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_dashboard = new Intent(context, Dashboard.class);
                startActivity(intent_dashboard);
                finish();
            }
        });




        // select the image from the device
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // check if the permission is granted or not to read from storage
                int checkVal = context.checkCallingOrSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
                if (checkVal == PackageManager.PERMISSION_GRANTED) {

                    // if permission is granted then pick image from the storage
                    chooseImage();
                } else {

                    // if permission is not granted then ask for the permission
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            123);
                }



            }
        });



        tv_encryption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseRef.dbref_ChatEdPassByUid.child(CurrentUser.userInfo.uid).child("EDP").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String EDP = dataSnapshot.getValue(String.class);

                            dialogAskEdp = new DialogAskEdp(context,EDP,messageList);
                            dialogAskEdp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialogAskEdp.setCancelable(false);
                            dialogAskEdp.setCanceledOnTouchOutside(false);
                            dialogAskEdp.show();



                        }else{

                            // Password not Configured , Setup Password for Encryption & Decryption


                            dialogSetupEdPass = new DialogSetupEdPass(context);
                            dialogSetupEdPass.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialogSetupEdPass.setCancelable(false);
                            dialogSetupEdPass.setCanceledOnTouchOutside(false);
                            dialogSetupEdPass.show();


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




            }
        });



    }


    // open the image picker for storage
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    // this will run after image picking
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            uploadImage(filePath);

        }


    }


    // upload the selected image to the database
    private void uploadImage(Uri filePath) {

        if (filePath != null) {


            // show the image uploading dialoge to the user and make sure he does not cancel during uploading
            final DialogImageUploadAdapter dialogImageUploadAdapter = new DialogImageUploadAdapter(context);
            dialogImageUploadAdapter.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogImageUploadAdapter.setCancelable(false);
            dialogImageUploadAdapter.setCanceledOnTouchOutside(false);
            dialogImageUploadAdapter.show();


            // get the current timestamp of the system
            calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            timestamp = calendar.getTimeInMillis();

            // generate the unique id for each image sent
            final String messageId = DatabaseRef.dbref_messages.push().getKey();


            ref = DatabaseRef.storageReference.child("images/" + messageId);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUrl = uri;



                                    final ChatInfo chatInfo = new ChatInfo(chatId, "Image", "image", CurrentUser.userInfo.uid,userReceiver.uid, timestamp, false);
                                    chatInfo.lastMsg = encrypt(chatInfo.lastMsg);

                                    final Message message = new Message(messageId, downloadUrl.toString(), "image", CurrentUser.userInfo.uid,CurrentUser.userInfo.username, userReceiver.uid, chatId, timestamp);
                                    message.msg = encrypt(message.msg);


                                    DatabaseRef.dbref_messages.child(chatId).child(messageId).setValue(message);
                                    DatabaseRef.dbref_chatinfo.child(chatId).setValue(chatInfo);
                                    DatabaseRef.dbref_inbox.child(CurrentUser.userInfo.uid).child(chatId).setValue(timestamp);
                                    DatabaseRef.dbref_inbox.child(userReceiver.uid).child(chatId).setValue(timestamp);
                                    dialogImageUploadAdapter.dismiss();

                                }
                            });





                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialogImageUploadAdapter.dismiss();
                            Toast.makeText(context, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            dialogImageUploadAdapter.txtProgress.setText((int) progress + "%");
                        }
                    });
        }
    }



    private void init() {
        context = this;


        txtusername = (TextView) findViewById(R.id.txtusername);
        profileimageview = (CircleImageView) findViewById(R.id.profileimageview);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnmenu = (Button) findViewById(R.id.btnmenu);
        btnSelectImage = (Button) findViewById(R.id.btnSelectImage);
        edt_msg = (EditText) findViewById(R.id.edt_msg);
        imgview_msgsend = (ImageView) findViewById(R.id.imgview_msgsend);
        recyclerView_chat = (RecyclerView) findViewById(R.id.recyclerView_chat);
        lytsend = (LinearLayout) findViewById(R.id.lytsend);
        tv_encryption = (TextView)findViewById(R.id.tv_encryption);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent_dashboard = new Intent(context, Dashboard.class);
        startActivity(intent_dashboard);
        finish();
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
