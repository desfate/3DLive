package com.futrtch.live.mvvm.vm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Size;
import android.widget.RelativeLayout;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.futrtch.live.activitys.LivePlayActivity;
import com.futrtch.live.activitys.fragments.main.LiveListFragment;
import com.futrtch.live.beans.ShareBean;
import com.futrtch.live.mvvm.repository.LiveRoomRepository;
import com.futrtch.live.mvvm.repository.LoginRepository;
import com.futrtch.live.tencent.beans.LivePlayBean;
import com.futrtch.live.tencent.common.msg.TCChatEntity;
import com.futrtch.live.tencent.common.msg.TCSimpleUserInfo;
import com.futrtch.live.tencent.common.ui.ErrorDialogFragment;
import com.futrtch.live.tencent.common.utils.TCConstants;
import com.futrtch.live.tencent.common.widget.TCSwipeAnimationController;
import com.futrtch.live.tencent.liveroom.IMLVBLiveRoomListener;
import com.futrtch.live.tencent.liveroom.roomutil.commondef.MLVBCommonDef;
import com.futrtch.live.utils.PressTimeControl;

import java.util.List;

import github.com.desfate.livekit.LivePlayView;
import master.flame.danmaku.controller.IDanmakuView;

public class LivePlayViewModel extends BaseMessageViewModel {
    /***********************************************   data  ******************************************************/
    final static String TAG = "LivePlayViewModel";

    LiveRoomRepository mRepository;
    private boolean mPlaying = false; //                                                        是否正在播放
    private long mStartPlayPts;  //                                                             直播开始时间
    LifecycleOwner lifecycleOwner;
    private LivePlayBean livePlayBean;   //                                                     观看直播需要的数据
    private ShareBean shareBean; //                                                             用于分享的数据
    /***********************************************  LiveData ******************************************************/
    MutableLiveData<Integer> liveState = new MutableLiveData<>();  //                           直播状态
    /***********************************************   用于直播控制的  *************************************************/
    private PressTimeControl mPressControl;      //                                             点赞频率控制
    private TCSwipeAnimationController mTCSwipeAnimationController; //                          手势动画
    @SuppressLint("StaticFieldLeak")
    private ErrorDialogFragment mErrDlgFragment;

    LivePlayViewModel(LifecycleOwner lifecycleOwner , LiveRoomRepository livePlayRepository){
        this.mRepository = livePlayRepository;
        this.lifecycleOwner = lifecycleOwner;
    }

    /**
     * 获取传递来的必要数据
     *
     * @param intent 页面传递的数据
     */
    public void getIntentData(Intent intent) {
        LivePlayBean bean = new LivePlayBean();
        bean.setmPusherId(intent.getStringExtra(TCConstants.PUSHER_ID));
        bean.setmGroupId(intent.getStringExtra(TCConstants.GROUP_ID));
        bean.setmPusherNickname(intent.getStringExtra(TCConstants.PUSHER_NAME));
        bean.setmPusherAvatar(intent.getStringExtra(TCConstants.PUSHER_AVATAR));
        bean.setmHeartCount(Long.decode(intent.getStringExtra(TCConstants.HEART_COUNT)));
        bean.setmCurrentAudienceCount(Long.decode(intent.getStringExtra(TCConstants.MEMBER_COUNT)));
        bean.setmFileId(intent.getStringExtra(TCConstants.FILE_ID));
        bean.setmTimeStamp(intent.getStringExtra(TCConstants.TIMESTAMP));
        bean.setmTitle(intent.getStringExtra(TCConstants.ROOM_TITLE));
        bean.setmCoverUrl(intent.getStringExtra(TCConstants.COVER_PIC));
        bean.setmUserId(LoginRepository.getInstance().getUserId());
        bean.setmNickname(LoginRepository.getInstance().getLoginInfo().getmUserName());
        bean.setmAvatar(LoginRepository.getInstance().getLoginInfo().getmUserAvatar());
        livePlayBean = bean;  // 储存必要数据
    }

    /**
     * 初始化必须控件
     * @param activity 上下文
     * @param danMaKuView 弹幕的View
     * @param relativeLayout 清屏需要清除的View
     */
    public void prepareLive(Activity activity, IDanmakuView danMaKuView , RelativeLayout relativeLayout) {
        mTCSwipeAnimationController = new TCSwipeAnimationController(activity);
        mTCSwipeAnimationController.setAnimationView(relativeLayout);
        super.prepare(activity
                , danMaKuView
                , livePlayBean.getmAvatar()
                , livePlayBean.getmNickname()
                , livePlayBean.getmPusherNickname());
    }

    /**
     * 开始观看直播
     */
    public void startLivePlay(LivePlayView mLivePlayView, IMLVBLiveRoomListener listener) {
        if (mPlaying) return;
        if (mLiveRoom == null) return;
        mLiveRoom.setSelfProfile(livePlayBean.getmNickname(), livePlayBean.getmAvatar());
        mLiveRoom.setListener(listener);
        mLiveRoom.setPlayViewChange(mLivePlayView.getVideoChange());
        mRepository.enterRoom(mLiveRoom, mLivePlayView, liveState, livePlayBean.getmGroupId());  // 进入房间
        mPlaying = true;
    }

    /**
     * 停止直播
     */
    public void stopLivePlay() {
        if (mPlaying && mLiveRoom != null) {
            mLiveRoom.sendRoomCustomMsg(String.valueOf(TCConstants.IMCMD_EXIT_LIVE), "", null);
            mRepository.exitRoom(mLiveRoom);
            mPlaying = false;
            mLiveRoom.setListener(null);
        }
    }

    /**
     * 显示错误以及退出的弹窗
     *
     * @param errorMsg 错误消息
     */
    public void showErrorAndQuit(String errorMsg, Activity activity) {
        stopLivePlay();

        Intent rstData = new Intent();
        rstData.putExtra(TCConstants.ACTIVITY_RESULT, errorMsg);
        activity.setResult(LiveListFragment.START_LIVE_PLAY, rstData);
        if (mErrDlgFragment == null) mErrDlgFragment = new ErrorDialogFragment();
        if (!mErrDlgFragment.isAdded() && !activity.isFinishing()) {
            Bundle args = new Bundle();
            args.putString("errorMsg", errorMsg);
            mErrDlgFragment.setArguments(args);
            mErrDlgFragment.setCancelable(false);
            //此处不使用用.show(...)的方式加载dialogfragment，避免IllegalStateException
            FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
            transaction.add(mErrDlgFragment, "loading");
            transaction.commitAllowingStateLoss();
        }
    }

    /**
     * 处理房间消息
     *
     * @param info 生命周期
     * @param type 消息类型
     */
    public void resolveMessage(LifecycleOwner lifecycleOwner, TCSimpleUserInfo info, int type, String message) {
        // 处理收到的消息
        super.handleMsg(lifecycleOwner, info, message, type);
    }

    /**
     * 发送点赞消息
     */
    public void sendLikeMsg() {
        //点赞发送请求限制
        if (mPressControl == null) {
            mPressControl = new PressTimeControl();
            mPressControl.init(2, 1);
        }
        if (mPressControl.canTrigger()) {
            heartCount.postValue(heartCount.getValue() == null ? 0 : heartCount.getValue() + 1);
            mLiveRoom.setCustomInfo(MLVBCommonDef.CustomFieldOp.INC, "praise", 1, null);
            //向ChatRoom发送点赞消息
            mLiveRoom.sendRoomCustomMsg(String.valueOf(TCConstants.IMCMD_PRAISE), "", null);
        }
    }

    /**
     * 退出房间
     * @param activity 上下文
     */
    public void exitRoom(LivePlayActivity activity){
        Intent rstData = new Intent();
        long memberCount = (currentAudienceCount.getValue() == null ? 0 : currentAudienceCount.getValue()) - 1;
        rstData.putExtra(TCConstants.MEMBER_COUNT, memberCount>=0 ? memberCount:0);
        rstData.putExtra(TCConstants.HEART_COUNT, heartCount.getValue());
        rstData.putExtra(TCConstants.PUSHER_ID, livePlayBean.getmPusherId());
        activity.setResult(0,rstData);
        stopLivePlay();
        activity.finish();
    }

    public Size getLiveSize(){
        return mLiveRoom.getPlaySize();
    }

    public TCSwipeAnimationController getSwipControl(){
        return mTCSwipeAnimationController;
    }

    public MutableLiveData<Integer> getLiveState() {
        return liveState;
    }

    public MutableLiveData<Integer> getCurrentAudienceCount() {
        return currentAudienceCount;
    }

    public MutableLiveData<List<TCSimpleUserInfo>> getCurrentAudienceList() {
        return currentAudienceList;
    }

    public MutableLiveData<List<TCChatEntity>> getCurrentMessageList() {
        return currentMessageList;
    }

    public MutableLiveData<Integer> getHeartCount() {
        return heartCount;
    }

    public MutableLiveData<String> getScreenState() {
        return screenState;
    }

    public String getPusherId() {
        return livePlayBean.getmPusherId() == null ? "0" : livePlayBean.getmPusherId();
    }


}
