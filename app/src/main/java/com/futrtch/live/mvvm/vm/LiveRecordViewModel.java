package com.futrtch.live.mvvm.vm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.futrtch.live.R;
import com.futrtch.live.activitys.BaseIMLVBActivity;
import com.futrtch.live.databinding.ActivityRecordBinding;
import com.futrtch.live.mvvm.repository.LiveRoomRepository;
import com.futrtch.live.mvvm.repository.LoginRepository;
import com.futrtch.live.tencent.common.msg.TCChatEntity;
import com.futrtch.live.tencent.common.msg.TCSimpleUserInfo;
import com.futrtch.live.tencent.common.ui.ErrorDialogFragment;
import com.futrtch.live.tencent.common.utils.TCUtils;
import com.futrtch.live.tencent.common.widget.FinishDetailDialogFragment;
import com.futrtch.live.tencent.live.LiveMessageCommand;
import com.futrtch.live.utils.AnimatorUtils;
import com.futrtch.live.utils.BroadcastTimerTask;
import com.tencent.rtmp.TXLivePusher;

import java.util.List;
import java.util.Locale;

import github.com.desfate.livekit.live.LiveCallBack;
import github.com.desfate.livekit.live.LiveConfig;
import github.com.desfate.livekit.live.LivePushControl;
import github.com.desfate.livekit.utils.LiveSupportUtils;

/**
 * 直播录制 viewModel
 */
public class LiveRecordViewModel extends BaseMessageViewModel {

    private final static String TAG = "LiveRecordViewModel";

    LiveRecordViewModel(LiveRoomRepository livePusherRepository) {
        this.mRepository = livePusherRepository;
    }

    /***********************************************  Data *********************************************************/
    private final LiveRoomRepository mRepository;//                                             直播推流 数据仓库
    String liveTitle = "直播测试2"; //                                                                        直播标题
    String liveCover; //                                                                        直播图片
    String location;  //                                                                        直播位置信息
    private LiveConfig liveConfig;
    /***********************************************  LiveData ******************************************************/
    MutableLiveData<Integer> liveState = new MutableLiveData<>();  //                           直播状态
    /***********************************************  用于直播控制的  *************************************************/
    private AnimatorUtils animatorUtils; //                                                     开始直播后 录制原点的动画
    @SuppressLint("StaticFieldLeak")
    private ErrorDialogFragment mErrDlgFragment; //                                             错误提示弹窗
    private BroadcastTimerTask timerTask;//                                                     直播时间计时器
    private LivePushControl control;//                                                          推流控制器 只有texture模式会初始化它 其实 data模式也会  只是封装在DataLivePushView里

    /**
     * 初始化相关
     *
     * @param activity     上下文
     * @param timeCallBack 直播时长计时器回调
     * @param binding      视图
     */
    public void prepareRecord(BaseIMLVBActivity activity, BroadcastTimerTask.TimeCallBack timeCallBack, ActivityRecordBinding binding) {
        timerTask = new BroadcastTimerTask(activity);
        timerTask.setTimeCallBack(timeCallBack);
        mErrDlgFragment = new ErrorDialogFragment();
        super.prepare(activity
                , binding.anchorDanmakuView
                , LoginRepository.getInstance().getLoginInfo().getmUserAvatar()
                , LoginRepository.getInstance().getUserId()
                , LoginRepository.getInstance().getUserId());
    }

    /**
     * 视图绑定
     *
     * @param mDataBinding 视图的双向持有对象
     */
    public void bindView(Context context, ActivityRecordBinding mDataBinding) {
        animatorUtils = new AnimatorUtils(mDataBinding.layoutLivePusherInfo.anchorIvRecordBall);   //                         录制原点的view
        liveConfig = new LiveConfig();
        liveConfig.setLiveQuality(LiveSupportUtils.LIVE_SIZE_720);  //  设置分辨率
        liveConfig.setLivePushType(LiveConfig.LIVE_PUSH_TEXTURE);  //   设置推送模式
        liveConfig.setPushCameraType(LiveConfig.LIVE_CAMERA_FRONT);//   设置摄像头
        // 这里做自己本地预览和开启摄像头的工作
        if(liveConfig.getLivePushType() == LiveConfig.LIVE_PUSH_TEXTURE){
            TextureView textureView = new TextureView(context);
            control = new LivePushControl.LivePushControlBuilder()
                    .setContext(context)
                    .setLiveConfig(liveConfig)
                    .setSurfaceTexture(textureView.getSurfaceTexture())
                    .setTextureView(textureView)
                    .setLiveCallBack(new LiveCallBack() {
                        @Override
                        public void startPushByData(byte[] buffer, int w, int h) {

                        }

                        @Override
                        public void startPushByTextureId(int textureID, int w, int h) {
                            int returnCode = mLiveRoom.customerTexturePush(textureID, w, h);
                            if (returnCode != 0) Log.e(TAG, "push error code = " + returnCode);
                        }
                    }).build();
            mDataBinding.txCloudView.addVideoView(textureView);//        绑定本地预览UI
        }else if(liveConfig.getLivePushType() == LiveConfig.LIVE_PUSH_DATA){
            mDataBinding.anchorPushView.init(
                    liveConfig,
                    mDataBinding.rootLayout,
                    new LiveCallBack() {
                        @Override
                        public void startPushByData(byte[] buffer, int w, int h) {
                        /*
                          returnCode
                          0：发送成功；
                          1：视频分辨率非法；
                          2：YUV 数据长度与设置的视频分辨率所要求的长度不一致；
                          3：视频格式非法；
                          4：视频图像长宽不符合要求，画面比要求的小了；
                          1000：SDK 内部错误。
                         */
                            int returnCode = mLiveRoom.customerDataPush(buffer, TXLivePusher.YUV_420P, w, h);
                            if (returnCode != 0) Log.e(TAG, "push error code = " + returnCode);
                        }

                        @Override
                        public void startPushByTextureId(int textureID, int w, int h) {

                        }
                    }
            );
            control = mDataBinding.anchorPushView.getControl();
        }
    }

    /**
     * 切换相机
     *
     * @param activity     上下文
     */
    public void switchCamera(Activity activity) {
        //fixme 通过文字消息通知观众 主播正在进行前后摄像头切换 用户也要跟随进行切换
        if (control.getCameraState() == LiveConfig.LIVE_CAMERA_FRONT) {
            onTextSend(activity, LiveMessageCommand.addCommand(LiveMessageCommand.SWITCH_CAMERA_BACK), false);
        } else {
            onTextSend(activity, LiveMessageCommand.addCommand(LiveMessageCommand.SWITCH_CAMERA_FRONT), false);
        }
        if (control.getCameraState() == LiveConfig.LIVE_CAMERA_FRONT) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);  // 切换为横屏
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  // 切换为竖屏
        }
        control.switchCamera();
    }

    /**
     * 开始推流
     */
    public void startPush(BaseIMLVBActivity activity) {
        mLiveRoom.setListener(activity);
        mLiveRoom.setCameraMuteImage(BitmapFactory.decodeResource(activity.getResources(), R.mipmap.pause_publish));
        mLiveRoom.startLocalPreview(true, null); // 初始化pusher
        mRepository.createRoom(liveState, mLiveRoom, liveTitle, liveCover, location, liveConfig);  // 创建直播间
    }

    /**
     * 开始预览
     */
    public void startPreview(){
        if(control != null) control.startPreview();
    }

    /**
     * 开始推送
     */
    public void startPush(){
        if(control != null) control.startPush();
    }

    /**
     * 停止推流
     */
    public void stopPush() {
        mRepository.exitRoom(mLiveRoom);
        mLiveRoom.setListener(null);
    }

    /**
     * 显示确认消息
     *
     * @param msg     消息内容
     * @param isError true错误消息（必须退出） false提示消息（可选择是否退出）
     */
    public void showExitInfoDialog(Context context, String msg, Boolean isError) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.ConfirmDialogStyle);
        builder.setCancelable(true);
        builder.setTitle(msg);

        if (!isError) {
            builder.setPositiveButton("确定", (dialog, which) -> {
                dialog.dismiss();
                stopPush();
                showPublishFinishDetailsDialog((Activity) context);
            });
            builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
        } else {
            //当情况为错误的时候，直接停止推流
            stopPush();
            builder.setPositiveButton("确定", (dialog, which) -> {
                dialog.dismiss();
                showPublishFinishDetailsDialog((Activity) context);
            });
        }
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }

    /**
     * 显示直播结果的弹窗
     * <p>
     * 如：观看数量、点赞数量、直播时长数
     */
    protected void showPublishFinishDetailsDialog(Activity activity) {
        //确认则显示观看detail
        FinishDetailDialogFragment dialogFragment = new FinishDetailDialogFragment();
        Bundle args = new Bundle();
        args.putString("time", TCUtils.formattedTime(0));
        args.putString("heartCount", String.format(Locale.CHINA, "%d", 0));
        args.putString("totalMemberCount", String.format(Locale.CHINA, "%d", 0));
        dialogFragment.setArguments(args);
        dialogFragment.setCancelable(false);
        if (dialogFragment.isAdded())
            dialogFragment.dismiss();
        else
            dialogFragment.show(activity.getFragmentManager(), "");
    }

    /**
     * 显示错误并且退出直播的弹窗
     *
     * @param errorCode 错误码
     * @param errorMsg  错误内容
     */
    public void showErrorAndQuit(Activity activity, int errorCode, String errorMsg) {
        stopPush();
        if (!mErrDlgFragment.isAdded() && !activity.isFinishing()) {
            Bundle args = new Bundle();
            args.putInt("errorCode", errorCode);
            args.putString("errorMsg", errorMsg);
            mErrDlgFragment.setArguments(args);
            mErrDlgFragment.setCancelable(false);
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

    public MutableLiveData<Integer> getLiveState() {
        return liveState;
    }

    public BroadcastTimerTask getTimerTask() {
        return timerTask;
    }

    public AnimatorUtils getAnimatorUtils() {
        return animatorUtils;
    }

    public MutableLiveData<List<TCChatEntity>> getCurrentMessageList() {
        return currentMessageList;
    }

    public MutableLiveData<List<TCSimpleUserInfo>> getCurrentAudienceList() {
        return currentAudienceList;
    }

    public LiveConfig getLiveConfig(){
        return liveConfig;
    }

    /**
     * release
     */
    public void release() {
        if (mDanMuMgr != null) {
            mDanMuMgr.destroy();
            mDanMuMgr = null;
        }
        animatorUtils.release();  // 动画释放
        if(control != null) control.releaseRes();
    }


}
