package com.futrtch.live.activitys;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.futrtch.live.interfaces.LiveRoomCallBack;
import com.futrtch.live.mvvm.MVVMActivity;
import com.futrtch.live.tencent.common.msg.TCSimpleUserInfo;
import com.futrtch.live.tencent.common.utils.TCConstants;
import com.futrtch.live.tencent.liveroom.IMLVBLiveRoomListener;
import com.futrtch.live.tencent.liveroom.roomutil.commondef.AnchorInfo;
import com.futrtch.live.tencent.liveroom.roomutil.commondef.AudienceInfo;

import java.util.Optional;

/**
 * 腾讯这个直播回调东西太多
 * 用基类来处理
 * 子类只回调他们业务需要的部分
 */
public abstract class BaseIMLVBActivity extends MVVMActivity implements IMLVBLiveRoomListener {

    LiveRoomCallBack liveRoomCallBack;  // 直播间相关回调

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
    }

    public void setLiveRoomCallBack(LiveRoomCallBack liveRoomCallBack) {
        this.liveRoomCallBack = liveRoomCallBack;
    }

    /**
     *     /////////////////////////////////////////////////////////////////////////////////
     *     //
     *     //                      MLVB 回调
     *     //
     *     /////////////////////////////////////////////////////////////////////////////////
     */

    /**
     * 错误回调
     * @param errCode 	错误码
     * @param errMsg 	错误信息
     * @param extraInfo 额外信息，如错误发生的用户，一般不需要关注，默认是本地错误
     */
    @Override
    public void onError(int errCode, String errMsg, Bundle extraInfo) {
        Optional.ofNullable(liveRoomCallBack).ifPresent(liveRoomCallBack -> liveRoomCallBack.roomError(errCode, errMsg, extraInfo));
    }

    /**
     * 警告回调
     * @param warningCode 	错误码 TRTCWarningCode
     * @param warningMsg 	警告信息
     * @param extraInfo 	额外信息，如警告发生的用户，一般不需要关注，默认是本地错误
     */
    @Override
    public void onWarning(int warningCode, String warningMsg, Bundle extraInfo) {

    }

    /**
     * debug相关
     * @param log
     */
    @Override
    public void onDebugLog(String log) {

    }

    /**
     * 房间关闭
     * @param roomID 房间 ID
     */
    @Override
    public void onRoomDestroy(String roomID) {
        Optional.ofNullable(liveRoomCallBack).ifPresent(liveRoomCallBack -> liveRoomCallBack.roomClosed(roomID));
    }

    /**
     * 主播进入
     * @param anchorInfo 新进房用户信息
     *
     */
    @Override
    public void onAnchorEnter(AnchorInfo anchorInfo) {

    }

    /**
     * 主播退出
     * @param anchorInfo 退房用户信息
     *
     */
    @Override
    public void onAnchorExit(AnchorInfo anchorInfo) {

    }

    /**
     * 观众进入
     * @param audienceInfo 进房观众信息
     */
    @Override
    public void onAudienceEnter(AudienceInfo audienceInfo) {

    }

    /**
     * 观众退出
     * @param audienceInfo 退房观众信息
     */
    @Override
    public void onAudienceExit(AudienceInfo audienceInfo) {

    }

    /**
     * 观众连麦
     * @param anchorInfo 观众信息
     * @param reason 连麦原因描述
     */
    @Override
    public void onRequestJoinAnchor(AnchorInfo anchorInfo, String reason) {

    }

    /**
     * 退出连麦
     */
    @Override
    public void onKickoutJoinAnchor() {

    }

    /**
     * 请求PK
     * @param anchorInfo 发起跨房连麦的主播信息
     */
    @Override
    public void onRequestRoomPK(AnchorInfo anchorInfo) {

    }

    /**
     * 退出PK
     * @param anchorInfo
     */
    @Override
    public void onQuitRoomPK(AnchorInfo anchorInfo) {

    }

    /**
     * 收到文本信息
     * @param roomID        房间 ID
     * @param userID        发送者 ID
     * @param userName      发送者昵称
     * @param userAvatar    发送者头像
     * @param message       文本消息
     */
    @Override
    public void onRecvRoomTextMsg(String roomID, String userID, String userName, String userAvatar, String message) {
        TCSimpleUserInfo userInfo = new TCSimpleUserInfo(userID, userName, userAvatar);
        int type = TCConstants.IMCMD_PAILN_TEXT;
        if(liveRoomCallBack != null) liveRoomCallBack.messageCallBack(userInfo, type, message);
    }

    /**
     * 收到自定义消息
     * @param roomID        房间 ID
     * @param userID        发送者 ID
     * @param userName      发送者昵称
     * @param userAvatar    发送者头像
     * @param cmd           自定义 cmd
     * @param message       自定义消息内容
     */
    @Override
    public void onRecvRoomCustomMsg(String roomID, String userID, String userName, String userAvatar, String cmd, String message) {
        TCSimpleUserInfo userInfo = new TCSimpleUserInfo(userID, userName, userAvatar);
        int type = Integer.parseInt(cmd);
        if(liveRoomCallBack != null) liveRoomCallBack.messageCallBack(userInfo, type, message);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
