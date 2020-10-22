package com.futrtch.live.viewmodel;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.util.Size;
import android.view.Display;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.futrtch.live.R;
import com.futrtch.live.activitys.LivePlayActivity;
import com.futrtch.live.activitys.fragments.LiveListFragment;
import com.futrtch.live.beans.ShareBean;
import com.futrtch.live.repositorys.LoginRepository;
import com.futrtch.live.tencent.beans.LivePlayBean;
import com.futrtch.live.tencent.common.msg.TCChatEntity;
import com.futrtch.live.tencent.common.msg.TCSimpleUserInfo;
import com.futrtch.live.tencent.common.ui.ErrorDialogFragment;
import com.futrtch.live.tencent.common.utils.TCConstants;
import com.futrtch.live.tencent.common.utils.TCErrorConstants;
import com.futrtch.live.tencent.common.widget.TCInputTextMsgDialog;
import com.futrtch.live.tencent.common.widget.TCSwipeAnimationController;
import com.futrtch.live.tencent.common.widget.danmaku.TCDanmuMgr;
import com.futrtch.live.tencent.common.widget.video.TCVideoViewMgr;
import com.futrtch.live.tencent.liveroom.IMLVBLiveRoomListener;
import com.futrtch.live.tencent.liveroom.MLVBLiveRoom;
import com.futrtch.live.tencent.liveroom.roomutil.commondef.MLVBCommonDef;
import com.futrtch.live.utils.PressTimeControl;
import com.tencent.rtmp.TXLog;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import github.com.desfate.livekit.LivePlayView;
import github.com.desfate.livekit.live.LiveMessageCommand;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import master.flame.danmaku.controller.IDanmakuView;

public class LivePlayViewModel extends ViewModel {
    /***********************************************   data  *****************************************************/
    final static String TAG = "LivePlayViewModel";

    private LivePlayBean livePlayBean;   //                                                     观看直播需要的数据
    private ShareBean shareBean; //                                                             用于分享的数据
    private boolean mPlaying = false;       //                                                  是否正在播放
    private long mStartPlayPts;  //                                                             直播开始时间

    /***********************************************  LiveData ***********************************************/
    MutableLiveData<Integer> liveState = new MutableLiveData<>();  //                           直播状态
    MutableLiveData<Integer> currentAudienceCount = new MutableLiveData<>(); //                 直播总观众数目（不会太精确）

    MutableLiveData<List<TCSimpleUserInfo>> currentAudienceList = new MutableLiveData<>();//    当前观众头像列表（并不需要全部）
    MutableLiveData<List<TCChatEntity>> currentMessageList = new MutableLiveData<>();//         当前聊天去数据列表

    MutableLiveData<Integer> heartCount = new MutableLiveData<>();//                            点赞数量
    MutableLiveData<String> screenState = new MutableLiveData<>();//                            横竖屏状态


    /***********************************************   用于直播控制的  *************************************************/

    private MLVBLiveRoom mLiveRoom; //                                                          MLVB 组件

    private TCInputTextMsgDialog mInputTextMsgDialog; //                                        消息输入框
    private PressTimeControl mPressControl;      //                                             点赞频率控制
    private TCSwipeAnimationController mTCSwipeAnimationController; //                          手势动画
    private TCDanmuMgr mDanmuMgr;     //                                                        弹幕
    private ErrorDialogFragment mErrDlgFragment;

    /**
     * 获取传递来的必要数据
     *
     * @param intent
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
        livePlayBean = bean;
    }

    /**
     * 初始化必须控件
     * @param context 上下文
     * @param danmakuView 弹幕的View
     * @param relativeLayout 清屏需要清除的View
     */
    public void prepareLive(Context context, IDanmakuView danmakuView , RelativeLayout relativeLayout) {
        mLiveRoom = MLVBLiveRoom.sharedInstance(context);//  初始化 MLVB 组件
        mDanmuMgr = new TCDanmuMgr(context);
        mDanmuMgr.setDanmakuView(danmakuView);
        mInputTextMsgDialog = new TCInputTextMsgDialog(context, R.style.InputDialog);
        mInputTextMsgDialog.setmOnTextSendListener((msg, tanmuOpen) -> sendInputMsg(msg, tanmuOpen, context));
        mTCSwipeAnimationController = new TCSwipeAnimationController(context);
        mTCSwipeAnimationController.setAnimationView(relativeLayout);
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

        mLiveRoom.enterRoom(livePlayBean.getmGroupId(), mLivePlayView, new IMLVBLiveRoomListener.EnterRoomCallback() {
            @Override
            public void onError(int errCode, String errInfo) {
                liveState.postValue(TCErrorConstants.ERROR_CUSTOMER_IN_ROOM_ERROR);  //     进入LiveRoom失败
            }

            @Override
            public void onSuccess() {
                liveState.postValue(TCErrorConstants.SUCCESS_CUSTOMER_IN_ROOM);  // 进入LiveRoom成功
                mLiveRoom.sendRoomCustomMsg(String.valueOf(TCConstants.IMCMD_ENTER_LIVE), "", null);   // 通知有用户进入
            }
        });
        mPlaying = true;
    }

    /**
     * 停止直播
     */
    public void stopLivePlay() {
        if (mPlaying && mLiveRoom != null) {
            mLiveRoom.sendRoomCustomMsg(String.valueOf(TCConstants.IMCMD_EXIT_LIVE), "", null);
            mLiveRoom.exitRoom(new IMLVBLiveRoomListener.ExitRoomCallback() {
                @Override
                public void onError(int errCode, String errInfo) {
                    Log.w(TAG, "exit room error : " + errInfo);
                }

                @Override
                public void onSuccess() {
                    TXLog.d(TAG, "exit room success ");
                }
            });
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
     * @param info
     * @param type
     */
    public void resolveMessage(LifecycleOwner lifecycleOwner, TCSimpleUserInfo info, int type, String message) {
        // 处理收到的消息
        handleMsg(lifecycleOwner, info, message, type);
    }

    /**
     * // FIXME: 2020/10/22 消息模块可以二次优化
     * 消息处理  后面可以单独抽离出来成为一个模块
     *
     * @param lifecycleOwner 生命周期
     * @param info           消息用户信息
     * @param text           文本消息具体文本
     * @param type           消息类型
     */
    private void handleMsg(LifecycleOwner lifecycleOwner, TCSimpleUserInfo info, String text, int type) {
        Observable.create(emitter -> emitter.onNext(info))
                .flatMap((Function<Object, ObservableSource<TCChatEntity>>) userInfo -> { //  用户进入消息通知  转换为  用户聊天区域信息通知
                    TCChatEntity entity = new TCChatEntity();
                    entity.setType(TCConstants.MEMBER_ENTER);
                    Optional.ofNullable((TCSimpleUserInfo) userInfo).ifPresent(info1 -> {
                        switch (type) {
                            case TCConstants.IMCMD_ENTER_LIVE:  // 观众进入房间
                                // 有用户加入
                                if (!Objects.requireNonNull(currentAudienceList.getValue()).contains(info1)) {
                                    List<TCSimpleUserInfo> audienceList = currentAudienceList.getValue();  // 更新头像列表 把新进入的用户加入刀第一位
                                    audienceList.add(0, info1); // // FIXME: 2020/10/22                真实情况应该是根据用户等级进行排序
                                    int audienceNum = currentAudienceCount.getValue() == null ? 0 : currentAudienceCount.getValue();
                                    currentAudienceCount.postValue(audienceNum += 1); //                      当前用户数量 + 1
                                    currentAudienceList.postValue(audienceList);//                            通知头像列表更新
                                }
                                entity.setSenderName("通知");
                                if (TextUtils.isEmpty(((TCSimpleUserInfo) userInfo).nickname))
                                    entity.setContent(((TCSimpleUserInfo) userInfo).userid + "加入直播");
                                else
                                    entity.setContent(((TCSimpleUserInfo) userInfo).nickname + "加入直播");
                                break;
                            case TCConstants.IMCMD_EXIT_LIVE: // 观众退出房间
                                // 退出
                                if (!Objects.requireNonNull(currentAudienceList.getValue()).contains(info1)) {
                                    List<TCSimpleUserInfo> audienceList = currentAudienceList.getValue();  // 更新头像列表 把新进入的用户加入刀第一位
                                    audienceList.remove(info1);
                                    int audienceNum = currentAudienceCount.getValue() == null ? 0 : currentAudienceCount.getValue();
                                    currentAudienceCount.postValue(Math.max(audienceNum - 1, 0)); //         当前用户数量 - 1
                                    currentAudienceList.postValue(audienceList);//                           通知头像列表更新
                                }
                                entity.setSenderName("通知");
                                if (TextUtils.isEmpty(((TCSimpleUserInfo) userInfo).nickname))
                                    entity.setContent(((TCSimpleUserInfo) userInfo).userid + "退出直播");
                                else
                                    entity.setContent(((TCSimpleUserInfo) userInfo).nickname + "退出直播");
                                break;
                            case TCConstants.IMCMD_PRAISE: // 用户点赞消息
                                // 有用户点赞
                                if (!Objects.requireNonNull(currentAudienceList.getValue()).contains(info1)) {
                                    int heartNum = heartCount.getValue() == null ? 0 : heartCount.getValue();
                                    heartCount.postValue(heartNum + 1);
                                }
                                entity.setSenderName("通知");
                                if (TextUtils.isEmpty(((TCSimpleUserInfo) userInfo).nickname))
                                    entity.setContent(((TCSimpleUserInfo) userInfo).userid + "点了个赞");
                                else
                                    entity.setContent(((TCSimpleUserInfo) userInfo).nickname + "点了个赞");
                                break;
                            case TCConstants.IMCMD_DANMU:// 收到弹幕消息
                                Optional.ofNullable(mDanmuMgr).ifPresent(tcDanmuMgr -> tcDanmuMgr.addDanmu(info.avatar, info.nickname, text));
                            case TCConstants.IMCMD_PAILN_TEXT: // 普通文本消息
                                if (!Objects.requireNonNull(currentAudienceList.getValue()).contains(info1)) {
                                    if (info1.userid.equals(livePlayBean.getmPusherId())) {  // 主播消息解密一下  看看是不是命令
                                        String command = LiveMessageCommand.resolveCommand(text);
                                        if (command.length() > 0) {
                                            switch (command) {
                                                case LiveMessageCommand.SWITCH_CAMERA_FRONT:
                                                    //前置（竖屏）
                                                    screenState.postValue(LiveMessageCommand.SWITCH_CAMERA_FRONT);
                                                    break;
                                                case LiveMessageCommand.SWITCH_CAMERA_BACK:
                                                    //后置（横屏）
                                                    screenState.postValue(LiveMessageCommand.SWITCH_CAMERA_BACK);
                                                    break;
                                            }
                                        }
                                    }
                                }
                                entity.setSenderName(((TCSimpleUserInfo) userInfo).nickname);
                                entity.setContent(text);
                                break;
                        }
                    });  // 通知头像更新

                    return Observable.create(emitter -> emitter.onNext(entity));
                }).to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner)))  //   防止内存泄漏
                .subscribe(tcChatEntity -> {
                    // tcChatEntity 不可能为空
                    List<TCChatEntity> messageList = currentMessageList.getValue();
                    Optional.ofNullable(messageList).ifPresent(tcChatEntities -> {
                        tcChatEntities.add(tcChatEntity);  //                                                最新的消息肯定在最下面
                        currentMessageList.postValue(tcChatEntities);  // 通                                 知聊天列表更新
                    });
                });
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
     * 显示输入框
     *
     * @param activity
     */
    public void showInputMsgDialog(Activity activity) {
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mInputTextMsgDialog.getWindow().getAttributes();
        lp.width = (display.getWidth()); //设置宽度
        mInputTextMsgDialog.getWindow().setAttributes(lp);
        mInputTextMsgDialog.setCancelable(true);
        mInputTextMsgDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mInputTextMsgDialog.show();
    }

    /**
     * 发送输入信息
     * @param msg
     * @param danmuOpen
     */
    public void sendInputMsg(String msg, boolean danmuOpen, Context context) {
        if (msg.length() == 0)
            return;
        byte[] byte_num = msg.getBytes(StandardCharsets.UTF_8);
        if (byte_num.length > 160) {
            Toast.makeText(context, "请输入内容", Toast.LENGTH_SHORT).show();
            return;
        }

        //消息回显
        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName("我:");
        entity.setContent(msg);
        entity.setType(TCConstants.TEXT_TYPE);
        List<TCChatEntity> messageList = currentMessageList.getValue();
        Optional.ofNullable(messageList).ifPresent(tcChatEntities -> {
            tcChatEntities.add(entity);  //                                                最新的消息肯定在最下面
            currentMessageList.postValue(tcChatEntities);  // 通                                 知聊天列表更新
        });

        if (danmuOpen) {
            if (mDanmuMgr != null) {
                mDanmuMgr.addDanmu(livePlayBean.getmAvatar(), livePlayBean.getmNickname(), msg);
            }
            mLiveRoom.sendRoomCustomMsg(String.valueOf(TCConstants.IMCMD_DANMU), msg, new IMLVBLiveRoomListener.SendRoomCustomMsgCallback() {
                @Override
                public void onError(int errCode, String errInfo) {
                    Log.w(TAG, "sendRoomDanmuMsg error: " + errInfo);
                }

                @Override
                public void onSuccess() {
                    Log.d(TAG, "sendRoomDanmuMsg success");
                }
            });
        } else {
            mLiveRoom.sendRoomTextMsg(msg, new IMLVBLiveRoomListener.SendRoomTextMsgCallback() {
                @Override
                public void onError(int errCode, String errInfo) {
                    Log.d(TAG, "sendRoomTextMsg error:");
                }

                @Override
                public void onSuccess() {
                    Log.d(TAG, "sendRoomTextMsg success:");
                }
            });
        }
    }


    /**
     * 退出房间
     * @param activity
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
        return livePlayBean == null ? "0" : livePlayBean.getmPusherId();
    }


}
