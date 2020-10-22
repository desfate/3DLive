package com.futrtch.live.interfaces;

import android.os.Bundle;

import com.futrtch.live.tencent.common.msg.TCSimpleUserInfo;

/**
 * 直播间相关的回调
 */
public interface LiveRoomCallBack {

    /**
     * 消息返回
     *
     * @param info    消息信息
     * @param type    消息类型
     * @param message 自定义消息内容
     */
    void messageCallBack(TCSimpleUserInfo info, int type, String message);

    /**
     * 退出直播
     *
     * @param roomID 退出的房间ID
     */
    void roomClosed(String roomID);

    /**
     * 错误返回
     *
     * @param errorCode    错误码
     * @param errorMessage 错误信息
     * @param extraInfo    额外信息，如错误发生的用户，一般不需要关注，默认是本地错误
     */
    void roomError(int errorCode, String errorMessage, Bundle extraInfo);
}
