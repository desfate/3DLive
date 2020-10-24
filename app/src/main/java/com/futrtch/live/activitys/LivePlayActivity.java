package com.futrtch.live.activitys;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Size;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.futrtch.live.R;
import com.futrtch.live.databinding.ActivityLiveBinding;
import com.futrtch.live.interfaces.LiveRoomCallBack;
import com.futrtch.live.tencent.common.msg.TCChatMsgListAdapter;
import com.futrtch.live.tencent.common.msg.TCSimpleUserInfo;
import com.futrtch.live.tencent.common.utils.TCErrorConstants;
import com.futrtch.live.tencent.common.utils.TCUtils;
import com.futrtch.live.tencent.common.widget.RTCUserAvatarListAdapter;
import com.futrtch.live.tencent.liveroom.roomutil.commondef.MLVBCommonDef;
import com.futrtch.live.utils.StyleUtils;
import com.futrtch.live.utils.TransitionUtils;
import com.futrtch.live.mvvm.vm.LivePlayViewModel;
import com.futrtch.live.mvvm.vm.LivePlayViewModelFactory;
import com.jakewharton.rxbinding4.view.RxView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import github.com.desfate.livekit.live.LiveMessageCommand;

/**
 * 直播播放页面
 */
public class LivePlayActivity extends BaseIMLVBActivity implements LiveRoomCallBack {

    private static final String TAG = "LivaPlayActivity";
    public final static String TRANSITION_NAME_IMAGE = "btn";  // 动画相关

    private LivePlayViewModel mViewModel;
    private ActivityLiveBinding mDataBinding;

    private RTCUserAvatarListAdapter mAvatarListAdapter;  // 头像列表适配器
    private TCChatMsgListAdapter mChatMsgListAdapter;    // 消息列表的Adapter

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        StyleUtils.initStyle(this);  //     初始化直播页面的style
        super.onCreate(savedInstanceState);
        setLiveRoomCallBack(this);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_live);
        ViewModelProvider.Factory factory = new LivePlayViewModelFactory(getApplication(), this);
        mViewModel = ViewModelProviders.of(this, factory).get(LivePlayViewModel.class);
        TransitionUtils.setTransitionAnim(mDataBinding.audienceBackground, getWindow(), TRANSITION_NAME_IMAGE); // 设置过场动画

        init();
        bindUi();
        subscribeUi();
    }

    private void init() {
        mDataBinding.audienceBackground.setImageResource(R.mipmap.live2_icon);
        mViewModel.getIntentData(getIntent()); //   拿到主播相关数据
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mDataBinding.anchorRvAvatar.setLayoutManager(linearLayoutManager);
        mAvatarListAdapter = new RTCUserAvatarListAdapter(this, mViewModel.getPusherId());
        mDataBinding.anchorRvAvatar.setAdapter(mAvatarListAdapter);
        mChatMsgListAdapter = new TCChatMsgListAdapter(this, mDataBinding.imMsgListview, new ArrayList<>());
        mDataBinding.imMsgListview.setAdapter(mChatMsgListAdapter);
        mViewModel.prepareLive(this, mDataBinding.anchorDanmakuView, mDataBinding.anchorControlLayer);  // 直播前准备
        mViewModel.startLivePlay(mDataBinding.anchorPlayView, this);
    }

    private void bindUi() {
        // 右滑还原
        RxView.touches(mDataBinding.anchorPlayView)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(motionEvent -> mViewModel.getSwipControl().processEvent(motionEvent));
        RxView.touches(mDataBinding.audienceBackground)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(motionEvent -> mViewModel.getSwipControl().processEvent(motionEvent));
        // 左滑清屏
        RxView.touches(mDataBinding.anchorControlLayer)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(motionEvent -> mViewModel.getSwipControl().processEvent(motionEvent));
        // 退出按钮
        RxView.clicks(mDataBinding.btnBack)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> mViewModel.exitRoom(LivePlayActivity.this));
        // 点赞按钮
        RxView.clicks(mDataBinding.heartLayout)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> {
                    mDataBinding.heartLayout.addFavor(); // 显示点赞动画
                    mViewModel.sendLikeMsg(); // 发送点赞消息
                });
        // 消息按钮
        RxView.clicks(mDataBinding.btnMessageInput)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> mViewModel.showInputMsgDialog(LivePlayActivity.this));
    }

    private void subscribeUi() {
        mViewModel.getLiveState().observe(this, integer -> {
            switch (integer) {
                case TCErrorConstants.SUCCESS_CUSTOMER_IN_ROOM:  //                 进入LiveRoom成功
                    mDataBinding.anchorPlayView.setVisibility(View.VISIBLE);//      解决白色闪屏问题
                    mDataBinding.audienceBackground.setVisibility(View.GONE);
                    break;
                case TCErrorConstants.ERROR_CUSTOMER_IN_ROOM_ERROR: //              进入LiveRoom失败
                    mDataBinding.anchorPlayView.setVisibility(View.INVISIBLE);
                    mDataBinding.audienceBackground.setVisibility(View.VISIBLE);
                    mViewModel.showErrorAndQuit(TCErrorConstants.getErrorInfo(integer), LivePlayActivity.this);
                    break;
            }
        });
        // 观众数目绑定
        mViewModel.getCurrentAudienceCount().observe(this, integer -> mDataBinding.layoutLivePusherInfo.anchorTvMemberCounts.setText(String.valueOf(integer)));
        // 观众进入房间
        mViewModel.getCurrentAudienceList().observe(this, tcSimpleUserInfo -> mAvatarListAdapter.submitList(tcSimpleUserInfo));  // 更新头像区域
        mViewModel.getCurrentMessageList().observe(this, tcChatEntities -> mChatMsgListAdapter.setData(tcChatEntities)); //         更新聊天区域
        // 横竖屏切换
        mViewModel.getScreenState().observe(this, this::switchWatchModel);
        // 用户点赞操作
        mViewModel.getHeartCount().observe(this, integer -> {

        });
    }


    /**
     *     /////////////////////////////////////////////////////////////////////////////////
     *     //
     *     //                      直播房间回调
     *     //
     *     /////////////////////////////////////////////////////////////////////////////////
     */
    /**
     * 消息返回
     *
     * @param info 消息信息
     * @param type 消息类型
     */
    @Override
    public void messageCallBack(TCSimpleUserInfo info, int type, String message) {
        mViewModel.resolveMessage(this, info, type, message);
    }

    /**
     * 房间关闭
     *
     * @param roomID 退出的房间ID
     */
    @Override
    public void roomClosed(String roomID) {
        mViewModel.showErrorAndQuit("直播已结束", this);
    }

    /**
     * 房间错误回调
     *
     * @param errorCode    错误码
     * @param errorMessage 错误信息
     * @param extraInfo    额外信息，如错误发生的用户，一般不需要关注，默认是本地错误
     */
    @Override
    public void roomError(int errorCode, String errorMessage, Bundle extraInfo) {
        if (errorCode == MLVBCommonDef.LiveRoomErrorCode.ERROR_IM_FORCE_OFFLINE) { // IM 被强制下线。
            TCUtils.showKickOut(LivePlayActivity.this);
        } else {
            mViewModel.showErrorAndQuit("视频流播放失败，Error:", this);
        }
    }

    /**
     *     /////////////////////////////////////////////////////////////////////////////////
     *     //
     *     //                      直播间横竖屏幕切换
     *     //
     *     /////////////////////////////////////////////////////////////////////////////////
     */
    /**
     * 横竖屏切换
     *
     * @param watchType
     */
    public void switchWatchModel(String watchType) {
        switch (watchType) {
            case LiveMessageCommand.SWITCH_CAMERA_FRONT:
                //前置（竖屏）
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  // 切换为竖屏
                break;
            case LiveMessageCommand.SWITCH_CAMERA_BACK:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);  // 切换为横屏
                break;
        }
    }

    /**
     * 横竖屏切换后的宽高处理
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        int W = mDisplayMetrics.widthPixels;
        int H = mDisplayMetrics.heightPixels;
        Size playSize = mViewModel.getLiveSize();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 这个大小要根据推过来的视频流进行变换
            if (playSize != null) {
                // 当前是横屏状态
                mDataBinding.anchorPlayView.getLayoutParams().height = H;
                mDataBinding.anchorPlayView.getLayoutParams().width = H
                        * (Math.max(playSize.getWidth(), playSize.getHeight()))
                        / (Math.min(playSize.getWidth(), playSize.getHeight()));
            }
            // land do nothing is ok
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // port do nothing is ok
            // 当前是竖屏状态
            if (playSize != null) {
                mDataBinding.anchorPlayView.getLayoutParams().width = W;
                mDataBinding.anchorPlayView.getLayoutParams().height = W
                        * (Math.max(playSize.getWidth(), playSize.getHeight()))
                        / (Math.min(playSize.getWidth(), playSize.getHeight()));
            }
        }
    }
}
