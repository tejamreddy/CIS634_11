package com.android.messenger.Model;

import java.io.Serializable;

public class User implements Serializable {
    public String username;
    public String uid;
    public String phone;
    public String name;
    public String deviceToken;
    public String profileImageUrl;
    public String status;

    public User(){
        // Required empty public constructor
    }

    public User(String username, String uid, String phone, String name,String deviceToken,String profileImageUrl,String status){
        this.username = username;
        this.uid = uid;
        this.phone = phone;
        this.name = name;
        this.deviceToken = deviceToken;
        this.profileImageUrl = profileImageUrl;
        this.status = status;

    }

}
