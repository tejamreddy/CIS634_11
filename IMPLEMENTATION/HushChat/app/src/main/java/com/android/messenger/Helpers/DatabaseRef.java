package com.android.messenger.Helpers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DatabaseRef {
    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public static StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    public static DatabaseReference dbref_users = FirebaseDatabase.getInstance().getReference("users");
    public static DatabaseReference dbref_username = FirebaseDatabase.getInstance().getReference("username");
    public static DatabaseReference dbref_chatinfo = FirebaseDatabase.getInstance().getReference("chatinfo");
    public static DatabaseReference dbref_messages = FirebaseDatabase.getInstance().getReference("messages");
    public static DatabaseReference dbref_inbox = FirebaseDatabase.getInstance().getReference("inbox");
    public static DatabaseReference dbref_groupinfo = FirebaseDatabase.getInstance().getReference("groupinfo");
    public static DatabaseReference dbref_Blocks = FirebaseDatabase.getInstance().getReference("Blocks");
    public static DatabaseReference dbref_MessagesDeleteTime = FirebaseDatabase.getInstance().getReference("MessagesDeleteTime");
    public static DatabaseReference dbref_ChatEdPassByUid = FirebaseDatabase.getInstance().getReference("ChatEdPassByUid");




}
