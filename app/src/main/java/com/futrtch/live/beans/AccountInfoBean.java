package com.futrtch.live.beans;

public class AccountInfoBean {
    /**
     * sex : 1
     * nickname : Wwe
     * avatar :
     * frontcover :
     * userid : test
     */
    private int sex;
    private String nickname;
    private String avatar;
    private String frontcover;
    private String userid;

    public void setSex(int sex) {
        this.sex = sex;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setFrontcover(String frontcover) {
        this.frontcover = frontcover;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getSex() {
        return sex;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getFrontcover() {
        return frontcover;
    }

    public String getUserid() {
        return userid;
    }
}
