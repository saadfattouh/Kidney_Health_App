package com.example.kidneyhealthapp.chat.models;

public class Chat {

    int chatId;
    String fromId;
    String userName;

    public Chat(int chatId , String usrName, String fromId) {
        this.userName = usrName;
        this.fromId = fromId;
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

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }


}
