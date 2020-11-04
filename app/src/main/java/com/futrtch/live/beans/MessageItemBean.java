package com.futrtch.live.beans;

public class MessageItemBean {

    String messageTitle;
    String messageTime;
    String messageContent;
    String messageDetail;

    public MessageItemBean(String messageTitle, String messageTime, String messageContent) {
        this.messageTitle = messageTitle;
        this.messageTime = messageTime;
        this.messageContent = messageContent;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageDetail() {
        return messageDetail;
    }

    public void setMessageDetail(String messageDetail) {
        this.messageDetail = messageDetail;
    }
}
