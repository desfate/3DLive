package com.futrtch.live.beans;

public class AddressBookBean {
    String UserImg;
    String UserName;
    String RealName;
    boolean isCare;

    public AddressBookBean(String userImg, String userName, String realName, boolean isCare) {
        UserImg = userImg;
        UserName = userName;
        RealName = realName;
        this.isCare = isCare;
    }

    public String getUserImg() {
        return UserImg;
    }

    public void setUserImg(String userImg) {
        UserImg = userImg;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getRealName() {
        return RealName;
    }

    public void setRealName(String realName) {
        RealName = realName;
    }

    public boolean isCare() {
        return isCare;
    }

    public void setCare(boolean care) {
        isCare = care;
    }
}
