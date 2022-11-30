package com.android.messenger.Model;

import java.io.Serializable;

public class ChatInfo implements Serializable {

    public String chatId;
    public String lastMsg;
    public String msgType;
    public String senderUid;
    public String receiverUid;
    public long timestamp;
    public Boolean isGroup;

    public ChatInfo(){
        // Required Empty Constructor
    }

    public ChatInfo(String chatId, String lastMsg, String msgType, String senderUid,String receiverUid, long timestamp, Boolean isGroup){
        this.chatId = chatId;
        this.lastMsg = lastMsg;
        this.msgType = msgType;
        this.senderUid = senderUid;
        this.receiverUid = receiverUid;
        this.timestamp = timestamp;
        this.isGroup = isGroup;
    }
}
