package com.android.messenger.Model;

public class Message {

    public String msgId;
    public String msg;
    public String type;
    public String senderUid;
    public String senderUsername;
    public String receiverUid;
    public String chatId;
    public long timestamp;

    public Message(){
        // Required Empty Constructor
    }

    public Message(String msgId,String msg,String type,String senderUid,String senderUsername,String receiverUid,String chatId, long timestamp){
        this.msgId = msgId;
        this.msg = msg;
        this.type = type;
        this.senderUid = senderUid;
        this.senderUsername = senderUsername;
        this.receiverUid = receiverUid;
        this.chatId = chatId;
        this.timestamp = timestamp;
    }

}
