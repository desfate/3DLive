package com.futrtch.live.beans;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class MessageInfoBean implements MultiItemEntity {

    public final static int MESSAGE_TYPE_FANS = 1;
    public final static int MESSAGE_TYPE_ASSIST = 2;
    public final static int MESSAGE_TYPE_MINE = 3;
    public final static int MESSAGE_TYPE_DISCUSS = 4;

    private int messageType;
    private String headImg;
    private String headName;
    private String headContext;

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getHeadName() {
        return headName;
    }

    public void setHeadName(String headName) {
        this.headName = headName;
    }

    public String getHeadContext() {
        return headContext;
    }

    public void setHeadContext(String headContext) {
        this.headContext = headContext;
    }

    @Override
    public int getItemType() {
        return messageType;
    }
}
