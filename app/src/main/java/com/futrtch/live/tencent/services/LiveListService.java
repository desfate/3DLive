package com.futrtch.live.tencent.services;

import android.text.TextUtils;

import com.futrtch.live.base.BaseResponBean;
import com.futrtch.live.tencent.live.TCVideoInfo;
import com.futrtch.live.tencent.liveroom.IMLVBLiveRoomListener;
import com.futrtch.live.tencent.liveroom.MLVBLiveRoom;
import com.futrtch.live.tencent.liveroom.roomutil.commondef.AnchorInfo;
import com.futrtch.live.tencent.liveroom.roomutil.commondef.RoomInfo;
import com.tencent.rtmp.TXLog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;


public class LiveListService {
    private static final String TAG = "LiveListService";
    private static final int PAGE_SIZE = 200;

    /**
     * 获取正在直播的用户列表
     * @return 返回观察者
     */
    public static Observable<BaseResponBean<List<TCVideoInfo>>> fetchLiveList(MLVBLiveRoom liveRoom) {
        return Observable.create(emitter -> liveRoom.getRoomList(0, PAGE_SIZE, new IMLVBLiveRoomListener.GetRoomListCallback() {
            @Override
            public void onError(int errCode, String errInfo) {
                emitter.onNext(new BaseResponBean<>(errCode, errInfo));
                TXLog.w(TAG, "xzb_process: get_live_list error, code:"+errCode+", errInfo:"+errInfo);
            }

            @Override
            public void onSuccess(ArrayList<RoomInfo> data) {
                ArrayList<TCVideoInfo> infos = new ArrayList();
                if (data != null && data.size() > 0) {
                    for (RoomInfo value : data) {
                        List<AnchorInfo> pushers = value.pushers;

                        TCVideoInfo info = new TCVideoInfo();
                        info.playUrl = value.mixedPlayURL;
                        info.title      = value.roomName;
                        info.userId = value.roomCreator;
                        info.groupId = value.roomID;
                        info.viewerCount= value.audienceCount;
                        info.livePlay   = true;
                        if (pushers != null && !pushers.isEmpty()) {
                            AnchorInfo pusher = pushers.get(0);
                            info.nickname   = pusher.userName;
                            info.avatar     = pusher.userAvatar;
                        }

                        try {
                            JSONObject jsonRoomInfo = new JSONObject(value.roomInfo);
                            info.title      = jsonRoomInfo.optString("title");
                            info.frontCover = jsonRoomInfo.optString("frontcover");
                            info.location   = jsonRoomInfo.optString("location");
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (!TextUtils.isEmpty(value.roomInfo)) {
                                info.title = value.roomInfo;
                            }
                        }

                        try {
                            JSONObject jsonCunstomInfo = new JSONObject(value.custom);
                            info.likeCount   = jsonCunstomInfo.optInt("praise");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        infos.add(info);
                    }
                }
                BaseResponBean resBean = new BaseResponBean<>(0, "success");
                resBean.setData(infos);
                emitter.onNext(resBean);
                TXLog.w(TAG, "xzb_process: get_live_list success");
            }
        }));
    }
}
