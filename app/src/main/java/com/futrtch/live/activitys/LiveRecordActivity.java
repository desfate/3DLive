package com.futrtch.live.activitys;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.futrtch.live.R;
import com.futrtch.live.databinding.ActivityRecordBinding;
import com.futrtch.live.mvvm.vm.LiveRecordViewModel;
import com.futrtch.live.mvvm.vm.LiveRecordViewModelFactory;
import com.futrtch.live.tencent.common.msg.TCChatMsgListAdapter;
import com.futrtch.live.tencent.common.utils.TCErrorConstants;
import com.futrtch.live.tencent.common.utils.TCUtils;
import com.futrtch.live.tencent.common.widget.RTCUserAvatarListAdapter;
import com.futrtch.live.utils.BroadcastTimerTask;
import com.jakewharton.rxbinding4.view.RxView;

import java.util.concurrent.TimeUnit;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

import static com.futrtch.live.tencent.common.utils.TCErrorConstants.ERROR_CUSTOMER_CREATE_ROOM_ERROR;
import static com.futrtch.live.tencent.common.utils.TCErrorConstants.SUCCESS_CUSTOMER_CREATE_ROOM;
import static com.futrtch.live.tencent.common.utils.TCErrorConstants.SUCCESS_CUSTOMER_START_LIVE;

/**
 * 主播录制页面
 */
public class LiveRecordActivity extends BaseIMLVBActivity{

    private static final String TAG = "LiveRecordActivity";

    private LiveRecordViewModel mViewModel;
    private ActivityRecordBinding mDataBinding;

    private RTCUserAvatarListAdapter mAvatarListAdapter;  // 头像列表适配器
    private TCChatMsgListAdapter mChatMsgListAdapter;    // 消息列表的Adapter

    private BroadcastTimerTask.TimeCallBack callBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider.Factory factory = new LiveRecordViewModelFactory(getApplication(), this);
        mViewModel = ViewModelProviders.of(this,factory).get(LiveRecordViewModel.class);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_record);
        init();
        bindUi();
        subscribeUi();
    }

    private void init() {
        // 计时器回调
        callBack = time -> {
            mDataBinding.layoutLivePusherInfo.anchorTvBroadcastingTime.setText(TCUtils.formattedTime(time));  //             更新直播时间
        };
        mViewModel.bindView(mDataBinding); //                                        绑定view 和 viewModel
        mViewModel.prepareRecord(this, callBack, mDataBinding); //                         初始化必要
        if (TCUtils.checkRecordPermission(this)) {
            mDataBinding.anchorPushView.startPush();
            mViewModel.startPush( this);//                                   开始推送
        }
    }

    private void bindUi() {
        // 退出按钮
        RxView.clicks(mDataBinding.btnBack)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> onBackPressed());//                                                                           返回按钮
        // 切换摄像头
        RxView.clicks(mDataBinding.btnCamera)
                .throttleFirst(2,TimeUnit.SECONDS)  //                                                            防抖
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> {
                    System.out.println("11111");
                    mViewModel.switchCamera(LiveRecordActivity.this, mDataBinding.anchorPushView);//                     切换横竖屏
                });
        //  聊天按钮
        RxView.clicks(mDataBinding.btnMessageInput)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> mViewModel.showInputMsgDialog(LiveRecordActivity.this)); //                            显示输入框
    }

    private void subscribeUi() {
        mViewModel.getLiveState().observe(this, integer -> {
            switch (integer){
                case ERROR_CUSTOMER_CREATE_ROOM_ERROR:  //   创建房间失败
                    mViewModel.showErrorAndQuit(this, integer, TCErrorConstants.getErrorInfo(integer));
                    break;
                case SUCCESS_CUSTOMER_CREATE_ROOM: //        房间创建成功
                    mViewModel.getTimerTask().interval(1L, TimeUnit.SECONDS); //  开始计时
                    mViewModel.getAnimatorUtils().startAlphaChange(); // 开始动画
                    break;
                case SUCCESS_CUSTOMER_START_LIVE://          开始直播

                    break;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.release();
        mDataBinding.anchorPushView.release();
    }

    @Override
    public void onBackPressed() {
        mViewModel.showExitInfoDialog(this,"当前正在直播，是否退出直播？", false);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
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
}
