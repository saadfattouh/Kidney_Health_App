package com.example.kidneyhealthapp.model;

public class Message {
    private int id;
    private boolean type;
    private int fromId;
    private int toId;
    private boolean senderType;
    private String content;
    private String createdAt;

    public Message(int id, boolean type, int fromId, int toId, boolean senderType, String content, String createdAt) {
        this.id = id;
        this.type = type;
        this.fromId = fromId;
        this.toId = toId;
        this.senderType = senderType;
        this.content = content;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public boolean isType() {
        return type;
    }

    public int getFromId() {
        return fromId;
    }

    public int getToId() {
        return toId;
    }

    public boolean isSenderType() {
        return senderType;
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

    public void setType(boolean type) {
        this.type = type;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public void setSenderType(boolean senderType) {
        this.senderType = senderType;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
