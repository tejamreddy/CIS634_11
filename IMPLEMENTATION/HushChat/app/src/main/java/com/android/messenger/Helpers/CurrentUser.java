package com.android.messenger.Helpers;

import com.android.messenger.Model.User;

public class CurrentUser {
    public static User userInfo;
    public static void setUser(User user){
        userInfo = user;
    }
    public static int userMsgDeleteTime = -1;
}
