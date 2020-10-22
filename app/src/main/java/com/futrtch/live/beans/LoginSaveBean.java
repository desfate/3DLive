package com.futrtch.live.beans;

/**
 * 本地用于保存用户登录信息的类
 */
public class LoginSaveBean {
    private String mUserId;                // 账号
    private String mUserPwd;               // 密码

    private String mUserName;              // 用户称呼
    private String mUserAvatar;            // 用户头像
    private String mCoverPic;              // 直播用的封面图
    private String mLocation;              // 地址信息
    private int mSex = -1;                 // 性别  0:male,1:female,-1:unknown

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getmUserPwd() {
        return mUserPwd;
    }

    public void setmUserPwd(String mUserPwd) {
        this.mUserPwd = mUserPwd;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getmUserAvatar() {
        return mUserAvatar;
    }

    public void setmUserAvatar(String mUserAvatar) {
        this.mUserAvatar = mUserAvatar;
    }

    public String getmCoverPic() {
        return mCoverPic;
    }

    public void setmCoverPic(String mCoverPic) {
        this.mCoverPic = mCoverPic;
    }

    public String getmLocation() {
        return mLocation;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public int getmSex() {
        return mSex;
    }

    public void setmSex(int mSex) {
        this.mSex = mSex;
    }
}
