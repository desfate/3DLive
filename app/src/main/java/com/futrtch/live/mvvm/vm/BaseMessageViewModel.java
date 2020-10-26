package com.futrtch.live.mvvm.vm;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.futrtch.live.R;
import com.futrtch.live.tencent.common.msg.TCChatEntity;
import com.futrtch.live.tencent.common.msg.TCSimpleUserInfo;
import com.futrtch.live.tencent.common.utils.TCConstants;
import com.futrtch.live.tencent.common.widget.TCInputTextMsgDialog;
import com.futrtch.live.tencent.common.widget.danmaku.TCDanmuMgr;
import com.futrtch.live.tencent.liveroom.IMLVBLiveRoomListener;
import com.futrtch.live.tencent.liveroom.MLVBLiveRoom;
import com.futrtch.live.tencent.login.TCUserMgr;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import github.com.desfate.livekit.live.LiveMessageCommand;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;
import master.flame.danmaku.controller.IDanmakuView;

public class BaseMessageViewModel extends ViewModel {

    private final static String TAG = "BaseMessageViewModel";
    /***********************************************  Data **********************************************************/
    String mPusherId = "";
    /***********************************************  LiveData ******************************************************/
    MutableLiveData<List<TCSimpleUserInfo>> currentAudienceList = new MutableLiveData<>();//    当前观众头像列表（并不需要全部）
    MutableLiveData<List<TCChatEntity>> currentMessageList = new MutableLiveData<>();//         当前聊天数据列表
    MutableLiveData<Integer> currentAudienceCount = new MutableLiveData<>(); //                 直播总观众数目（不会太精确）
    MutableLiveData<Integer> heartCount = new MutableLiveData<>();//                            点赞数量
    MutableLiveData<String> screenState = new MutableLiveData<>();//                            横竖屏状态
    /***********************************************  用于直播控制的  *************************************************/
    public TCDanmuMgr mDanMuMgr;//                                                              弹幕
    public MLVBLiveRoom mLiveRoom;//                                                            直播间 M L V B
    public TCInputTextMsgDialog mInputTextMsgDialog;//                                          底部输入控件

    /**
     * 初始化数据
     * @param activity 上下文
     * @param danMakUView 弹幕控件
     * @param avatar 用户头像图片
     * @param nickname 用户名称
     * @param mPusherId 主播id
     */
    public void prepare(Activity activity, IDanmakuView danMakUView, String avatar, String nickname, String mPusherId) {
        this.mPusherId = mPusherId;
        mLiveRoom = MLVBLiveRoom.sharedInstance(activity);//                                    初始化 M L V B 组件
        mDanMuMgr = new TCDanmuMgr(activity);
        mDanMuMgr.setDanmakuView(danMakUView);
        mInputTextMsgDialog = new TCInputTextMsgDialog(activity, R.style.InputDialog);
        mInputTextMsgDialog.setmOnTextSendListener((msg, tanMuOpen) -> sendInputMsg(msg, tanMuOpen, activity, avatar, nickname));
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
    public void handleMsg(LifecycleOwner lifecycleOwner, TCSimpleUserInfo info, String text, int type) {
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
                                    currentAudienceCount.postValue(audienceNum + 1); //                      当前用户数量 + 1
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
                                Optional.ofNullable(mDanMuMgr).ifPresent(tcDanmuMgr -> tcDanmuMgr.addDanmu(info.avatar, info.nickname, text));
                            case TCConstants.IMCMD_PAILN_TEXT: // 普通文本消息
                                if (!Objects.requireNonNull(currentAudienceList.getValue()).contains(info1)) {
                                    if (info1.userid.equals(mPusherId)) {  // 主播消息解密一下  看看是不是命令
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
     * 发送输入信息
     *
     * @param msg       发送消息正文
     * @param danMuOpen 是否开启弹幕
     */
    public void sendInputMsg(String msg, boolean danMuOpen, Context context, String avatar, String nickName) {
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

        if (danMuOpen) {
            if (mDanMuMgr != null) {
                mDanMuMgr.addDanmu(avatar, nickName, msg);
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
     * 发送文本消息
     *
     * @param context   上下文
     * @param msg       消息正文
     * @param danMuOpen 是否开启弹幕
     */
    public void onTextSend(Context context, String msg, boolean danMuOpen) {
        if (msg.length() == 0)
            return;
        byte[] byte_num = msg.getBytes(StandardCharsets.UTF_8);
        if (byte_num.length > 160) {
            Toast.makeText(context, "请输入内容", Toast.LENGTH_SHORT).show();
            return;
        }

        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName("我:");
        entity.setContent(msg);
        entity.setType(TCConstants.TEXT_TYPE);
        // tcChatEntity 不可能为空
        List<TCChatEntity> messageList = currentMessageList.getValue();
        Optional.ofNullable(messageList).ifPresent(tcChatEntities -> {
            tcChatEntities.add(entity);  //                                                最新的消息肯定在最下面
            currentMessageList.postValue(tcChatEntities);  // 通                                 知聊天列表更新
        });

        // 发送弹幕或发送房间信息
        if (danMuOpen) {
            if (mDanMuMgr != null) {
                mDanMuMgr.addDanmu(TCUserMgr.getInstance().getAvatar(), TCUserMgr.getInstance().getNickname(), msg);
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
     * 显示输入框
     *
     * @param activity 上下文
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
     * 获取推送主播id
     * @return mPusherId
     */
    public String getmPusherId() {
        return mPusherId;
    }
}
