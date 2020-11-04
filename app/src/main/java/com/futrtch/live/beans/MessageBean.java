package com.futrtch.live.beans;

public class MessageBean {

    public final static int LIVE_MESSAGE = 1;
    public final static int SYSTEM_MESSAGE = 2;
    public final static int USER_MESSAGE = 3;

    int messageType;
    String messageContent;
    String messageTime;

    public MessageBean(int messageType, String messageContent, String messageTime) {
        this.messageType = messageType;
        this.messageContent = messageContent;
        this.messageTime = messageTime;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }
}
