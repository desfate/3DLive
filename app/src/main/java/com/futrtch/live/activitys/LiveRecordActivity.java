package com.futrtch.live.activitys;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.futrtch.live.R;
import com.futrtch.live.databinding.ActivityRecordBinding;
import com.futrtch.live.interfaces.LiveRoomCallBack;
import com.futrtch.live.mvvm.vm.LiveRecordViewModel;
import com.futrtch.live.mvvm.vm.LiveRecordViewModelFactory;
import com.futrtch.live.tencent.common.msg.TCChatMsgListAdapter;
import com.futrtch.live.tencent.common.msg.TCSimpleUserInfo;
import com.futrtch.live.tencent.common.utils.TCErrorConstants;
import com.futrtch.live.tencent.common.utils.TCUtils;
import com.futrtch.live.tencent.common.widget.RTCUserAvatarListAdapter;
import com.futrtch.live.tencent.liveroom.roomutil.commondef.MLVBCommonDef;
import com.futrtch.live.utils.BroadcastTimerTask;
import com.jakewharton.rxbinding4.view.RxView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

import static com.futrtch.live.tencent.common.utils.TCErrorConstants.ERROR_CUSTOMER_CREATE_ROOM_ERROR;
import static com.futrtch.live.tencent.common.utils.TCErrorConstants.SUCCESS_CUSTOMER_CREATE_ROOM;
import static com.futrtch.live.tencent.common.utils.TCErrorConstants.SUCCESS_CUSTOMER_START_LIVE;

/**
 * 主播录制页面
 */
public class LiveRecordActivity extends BaseIMLVBActivity implements LiveRoomCallBack {

    private static final String TAG = "LiveRecordActivity";

    private LiveRecordViewModel mViewModel;
    private ActivityRecordBinding mDataBinding;

    private RTCUserAvatarListAdapter mAvatarListAdapter;  // 头像列表适配器
    private TCChatMsgListAdapter mChatMsgListAdapter;    // 消息列表的Adapter

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initViewModel() {
        ViewModelProvider.Factory factory = new LiveRecordViewModelFactory(getApplication(), this);
        mViewModel = ViewModelProviders.of(this, factory).get(LiveRecordViewModel.class);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_record);
    }

    @Override
    public void init() {
        // 计时器回调 更新直播时间
        BroadcastTimerTask.TimeCallBack callBack = time -> {
            mDataBinding.layoutLivePusherInfo.anchorTvBroadcastingTime.setText(TCUtils.formattedTime(time));  //             更新直播时间
        };

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mDataBinding.anchorRvAvatar.setLayoutManager(linearLayoutManager);
        mAvatarListAdapter = new RTCUserAvatarListAdapter(this, mViewModel.getmPusherId());
        mDataBinding.anchorRvAvatar.setAdapter(mAvatarListAdapter);

        mChatMsgListAdapter = new TCChatMsgListAdapter(this, mDataBinding.imMsgListview, new ArrayList<>());
        mDataBinding.imMsgListview.setAdapter(mChatMsgListAdapter);

        mViewModel.bindView(mDataBinding); //                                        绑定view 和 viewModel
        mViewModel.prepareRecord(this, callBack, mDataBinding); //           初始化必要控件
        mViewModel.startPush(this);//                                        开始推送
        mDataBinding.anchorPushView.startPush();//                                   控件打开推送开关
    }

    @Override
    public void bindUi() {
        // 退出按钮
        RxView.clicks(mDataBinding.btnBack)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> onBackPressed());//                                                                           返回按钮
        // 切换摄像头
        RxView.clicks(mDataBinding.btnCamera)
                .throttleFirst(2, TimeUnit.SECONDS)  //                                                            防抖
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> {
                    mViewModel.switchCamera(LiveRecordActivity.this, mDataBinding.anchorPushView);//                     切换横竖屏
                });
        //  聊天按钮
        RxView.clicks(mDataBinding.btnMessageInput)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> mViewModel.showInputMsgDialog(LiveRecordActivity.this)); //                            显示输入框
    }

    @Override
    public void subscribeUi() {
        mViewModel.getLiveState().observe(this, integer -> {
            switch (integer) {
                case ERROR_CUSTOMER_CREATE_ROOM_ERROR:  //   创建房间失败
                    Log.w(TAG, "直播房间创建失败");
                    mViewModel.showErrorAndQuit(this, integer, TCErrorConstants.getErrorInfo(integer));
                    break;
                case SUCCESS_CUSTOMER_CREATE_ROOM: //                                            房间创建成功
                    Log.d(TAG, "直播计时器启动");
                    mViewModel.getTimerTask().interval(1L, TimeUnit.SECONDS); //       开始计时
                    mViewModel.getAnimatorUtils().startAlphaChange(); //                         开始动画

                    break;
                case SUCCESS_CUSTOMER_START_LIVE://                                              开始直播(预留)
                    // FIXME: 2020/10/26 现阶段 房间创建成功等于就开始推流了
                    Log.d(TAG, "开始直播");
                    break;
            }
        });
        // 聊天信息通知
        mViewModel.getCurrentMessageList().observe(this, tcChatEntities -> mChatMsgListAdapter.setData(tcChatEntities)); //         更新聊天区域
        // 观众进入房间
        mViewModel.getCurrentAudienceList().observe(this, tcSimpleUserInfo -> mAvatarListAdapter.submitList(tcSimpleUserInfo));  // 更新头像区域
    }

    @Override
    public void initRequest() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.release();
        mDataBinding.anchorPushView.release();
    }

    @Override
    public void onBackPressed() {
        mViewModel.showExitInfoDialog(this, "当前正在直播，是否退出直播？", false);
    }

    @Override
    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        int W = mDisplayMetrics.widthPixels;
        int H = mDisplayMetrics.heightPixels;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mDataBinding.anchorPushView.getLayoutParams().width = W;
            mDataBinding.anchorPushView.getLayoutParams().height = H;
            // land do nothing is ok
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // port do nothing is ok
            mDataBinding.anchorPushView.getLayoutParams().width = W;
            mDataBinding.anchorPushView.getLayoutParams().height = H;
        }
    }

    @Override
    public void messageCallBack(TCSimpleUserInfo info, int type, String message) {
        mViewModel.resolveMessage(this, info, type, message);
    }

    @Override
    public void roomClosed(String roomID) {
            mViewModel.showErrorAndQuit(this,-1,"直播已结束");
    }

    @Override
    public void roomError(int errorCode, String errorMessage, Bundle extraInfo) {
        if (errorCode == MLVBCommonDef.LiveRoomErrorCode.ERROR_IM_FORCE_OFFLINE) { // IM 被强制下线。
            TCUtils.showKickOut(LiveRecordActivity.this);
        } else {
            mViewModel.showErrorAndQuit(this,  errorCode,"视频流播放失败，Error:");
        }
    }
}
