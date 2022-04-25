package com.example.kidneyhealthapp.model;

public class Chat {

    int chatId;
    String userName;
    int otherMemberId;

    public Chat(int chatId, String userName, int otherMemberId) {
        this.chatId = chatId;
        this.userName = userName;
        this.otherMemberId = otherMemberId;
    }

    public int getChatId() {
        return chatId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getOtherMemberId() {
        return otherMemberId;
    }
}
