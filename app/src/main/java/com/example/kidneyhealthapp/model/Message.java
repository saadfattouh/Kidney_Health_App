package com.example.kidneyhealthapp.model;

public class Message {
    private int id;
    private int senderType;
    private String content;
    private String createdAt;
    private boolean fromMe;

    public Message(int id, int senderType, String content, String createdAt, boolean fromMe) {
        this.id = id;
        this.senderType = senderType;
        this.content = content;
        this.createdAt = createdAt;
        this.fromMe = fromMe;
    }

    public boolean isFromMe() {
        return fromMe;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getSenderType() {
        return senderType;
    }
}
