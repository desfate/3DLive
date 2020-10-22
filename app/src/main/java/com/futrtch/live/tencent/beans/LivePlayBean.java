package com.futrtch.live.tencent.beans;

import java.io.Serializable;

/**
 * 观众观看直播需要的数据
 */
public class LivePlayBean implements Serializable {
    private String mPusherAvatar;          // 主播头像连接地址
    private long mCurrentAudienceCount;  // 当前观众数量
    private long mHeartCount;            // 点赞数量
    private boolean mPlaying = false;       // 是否正在播放
    private String mPusherNickname;        // 主播昵称
    private String mPusherId;              // 主播id
    private String mGroupId = "";          // 房间id
    private String mUserId = "";           // 我的id
    private String mNickname = "";         // 我的昵称
    private String mAvatar = "";           // 我的头像
    private String mFileId = "";
    private String mTimeStamp = "";
    private String mTitle = "";
    private String mCoverUrl;

    public String getmPusherAvatar() {
        return mPusherAvatar;
    }

    public void setmPusherAvatar(String mPusherAvatar) {
        this.mPusherAvatar = mPusherAvatar;
    }

    public long getmCurrentAudienceCount() {
        return mCurrentAudienceCount;
    }

    public void setmCurrentAudienceCount(long mCurrentAudienceCount) {
        this.mCurrentAudienceCount = mCurrentAudienceCount;
    }

    public long getmHeartCount() {
        return mHeartCount;
    }

    public void setmHeartCount(long mHeartCount) {
        this.mHeartCount = mHeartCount;
    }

    public boolean ismPlaying() {
        return mPlaying;
    }

    public void setmPlaying(boolean mPlaying) {
        this.mPlaying = mPlaying;
    }

    public String getmPusherNickname() {
        return mPusherNickname;
    }

    public void setmPusherNickname(String mPusherNickname) {
        this.mPusherNickname = mPusherNickname;
    }

    public String getmPusherId() {
        return mPusherId;
    }

    public void setmPusherId(String mPusherId) {
        this.mPusherId = mPusherId;
    }

    public String getmGroupId() {
        return mGroupId;
    }

    public void setmGroupId(String mGroupId) {
        this.mGroupId = mGroupId;
    }

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getmNickname() {
        return mNickname;
    }

    public void setmNickname(String mNickname) {
        this.mNickname = mNickname;
    }

    public String getmAvatar() {
        return mAvatar;
    }

    public void setmAvatar(String mAvatar) {
        this.mAvatar = mAvatar;
    }

    public String getmFileId() {
        return mFileId;
    }

    public void setmFileId(String mFileId) {
        this.mFileId = mFileId;
    }

    public String getmTimeStamp() {
        return mTimeStamp;
    }

    public void setmTimeStamp(String mTimeStamp) {
        this.mTimeStamp = mTimeStamp;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmCoverUrl() {
        return mCoverUrl;
    }

    public void setmCoverUrl(String mCoverUrl) {
        this.mCoverUrl = mCoverUrl;
    }
}
