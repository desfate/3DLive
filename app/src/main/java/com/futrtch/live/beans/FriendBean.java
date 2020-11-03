package com.futrtch.live.beans;

public class FriendBean {

    String UserName;  // 用户名称
    String UserImg;   // 用户头像图片
    boolean isCare;    // 是否关注
    boolean isBlack;   // 是否拉黑

    public FriendBean(String userName, String userImg, boolean isCare, boolean isBlack) {
        UserName = userName;
        UserImg = userImg;
        this.isCare = isCare;
        this.isBlack = isBlack;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserImg() {
        return UserImg;
    }

    public void setUserImg(String userImg) {
        UserImg = userImg;
    }

    public boolean getIsCare() {
        return isCare;
    }

    public void setIsCare(boolean isCare) {
        this.isCare = isCare;
    }

    public boolean getIsBlack() {
        return isBlack;
    }

    public void setIsBlack(boolean isBlack) {
        this.isBlack = isBlack;
    }
}
