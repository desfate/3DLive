package com.futrtch.live.mvvm.repository;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.futrtch.live.base.ApiException;
import com.futrtch.live.base.BaseResponBean;
import com.futrtch.live.beans.RoomCreateResponseBean;
import com.futrtch.live.http.LiveRoomReqUtils;
import com.futrtch.live.http.flowables.RoomReqFlowable;
import com.futrtch.live.tencent.common.utils.TCConstants;
import com.futrtch.live.tencent.common.utils.TCErrorConstants;
import com.futrtch.live.tencent.liveroom.IMLVBLiveRoomListener;
import com.futrtch.live.tencent.liveroom.MLVBLiveRoom;
import com.tencent.rtmp.TXLog;

import org.reactivestreams.Subscription;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import github.com.desfate.livekit.live.LiveConfig;
import github.com.desfate.livekit.ui.LivePlayView;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableOnSubscribe;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;

public class LiveRoomRepository {

    private final static String TAG = "LiveRoomRepository";

    private final RoomCreateResponseBean responseBean = new RoomCreateResponseBean();//         进入 / 退出 房间回应

    private final LifecycleOwner lifecycleOwner;

    public LiveRoomRepository(LifecycleOwner lifecycleOwner) {  //                            同步生命周期  防止网络请求导致内存泄漏
        this.lifecycleOwner = lifecycleOwner;
    }

    /**
     * 创建房间请求  创建完成后  继续请求  进行房间建权
     * @param liveState 回调通知UI
     * @param mLiveRoom 腾讯组件
     */
    public void createRoom(MutableLiveData liveState, MLVBLiveRoom mLiveRoom, String liveTitle, String liveCover, String location, LiveConfig liveConfig) {
        Flowable.create((FlowableOnSubscribe<RoomCreateResponseBean>) emitter -> mLiveRoom.createRoom(//      创建房间请求
                "",
                LiveRoomReqUtils.toRoomInfoString(liveTitle, liveCover, location),
                liveConfig,
                new IMLVBLiveRoomListener.CreateRoomCallback() {
                    @Override
                    public void onError(int errCode, String errInfo) {
                        responseBean.setReturnCode(errCode);  //                                            返回错误码
                        responseBean.setErrInfo(errInfo); //                                                错误信息
                        Log.w(TAG, String.format("创建直播间错误, code=%s,error=%s", errCode, errInfo));
                        liveState.postValue(TCErrorConstants.ERROR_CUSTOMER_CREATE_ROOM_ERROR); //          通知页面  直播间创建失败
                        emitter.onNext(responseBean);
                    }

                    @Override
                    public void onSuccess(String RoomID) {
                        responseBean.setReturnCode(-1); //                                                  默认错误码-1 认为是成功
                        responseBean.setRoomID(RoomID);//                                                   成功需要返回roomId
                        liveState.postValue(TCErrorConstants.SUCCESS_CUSTOMER_CREATE_ROOM);//                 通知页面  直播间创建成功
                        emitter.onNext(responseBean);  //
                    }
                }
        ), BackpressureStrategy.BUFFER)
                .flatMap((Function<RoomCreateResponseBean, Flowable<BaseResponBean>>) roomCreateResponseBean -> {//   链式请求房间建权
                    if (roomCreateResponseBean.getReturnCode() == -1) { //                                            房间创建成功
                        return RoomReqFlowable.roomSuccessFlowable( //                                             建权请求
                                LoginRepository.getInstance().getUserId()
                                , liveTitle
                                , liveCover
                                , location
                                , LoginRepository.getInstance().getLoginInfo().getmUserName()
                                , LoginRepository.getInstance().getToken());
                    } else {//                                                                                        房间创建失败
                        return Flowable.error(new ApiException(roomCreateResponseBean.getReturnCode()
                                , roomCreateResponseBean.getErrInfo()));  //                                          抛出登录异常  不会继续链式调用
                    }
                }).to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this.lifecycleOwner)))
                .subscribe(new DisposableSubscriber<BaseResponBean>() {
                    @Override
                    public void onNext(BaseResponBean baseResponBean) {
                        Log.e("TAG", "request error" );
                        //                                                                                             这边暂时不做处理
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (t instanceof ApiException) {
                            Log.e("TAG", "request error" + ((ApiException) t).getStatusDesc());
                        } else {
                            Log.e("TAG", "request error" + t.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 进入房间
     * @param mLiveRoom
     * @param mLivePlayView
     * @param liveState
     */
    public void enterRoom(MLVBLiveRoom mLiveRoom, LivePlayView mLivePlayView, MutableLiveData liveState, String groupId) {
        Flowable.create((FlowableOnSubscribe<RoomCreateResponseBean>) emitter ->
                mLiveRoom.enterRoom(groupId, mLivePlayView, new IMLVBLiveRoomListener.EnterRoomCallback() {
                    @Override
                    public void onError(int errCode, String errInfo) {
                        liveState.postValue(TCErrorConstants.ERROR_CUSTOMER_IN_ROOM_ERROR);  //                                  进入LiveRoom失败 通知 UI
                        responseBean.setErrInfo(errInfo);
                        responseBean.setReturnCode(errCode);
                        emitter.onNext(responseBean);
                    }

                    @Override
                    public void onSuccess() {
                        liveState.postValue(TCErrorConstants.SUCCESS_CUSTOMER_IN_ROOM);  //                                      进入LiveRoom成功 通知 UI
                        mLiveRoom.sendRoomCustomMsg(String.valueOf(TCConstants.IMCMD_ENTER_LIVE), "", null); // 通知有用户进入
                    }
                }), BackpressureStrategy.BUFFER)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner)))
                .subscribe(roomCreateResponseBean -> Log.w(TAG, roomCreateResponseBean.getErrInfo()));
    }

    /**
     * 退出房间  不需要关心回调返回
     * @param mLiveRoom
     */
    public void exitRoom(MLVBLiveRoom mLiveRoom) {
        Flowable.create((FlowableOnSubscribe<RoomCreateResponseBean>) emitter ->
                mLiveRoom.exitRoom(new IMLVBLiveRoomListener.ExitRoomCallback() {
                    @Override
                    public void onError(int errCode, String errInfo) {
                        Log.w(TAG, "exit room error : " + errInfo);
                        responseBean.setErrInfo(errInfo);
                        responseBean.setReturnCode(errCode);
                        emitter.onNext(responseBean);
                    }

                    @Override
                    public void onSuccess() {
                        TXLog.d(TAG, "exit room success ");
                    }
                }), BackpressureStrategy.BUFFER)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner)))
                .subscribe(new FlowableSubscriber<RoomCreateResponseBean>() {
                    @Override
                    public void onSubscribe(@NonNull Subscription s) {

                    }

                    @Override
                    public void onNext(RoomCreateResponseBean roomCreateResponseBean) {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
