package com.futrtch.live.http;

import androidx.core.util.Pair;

import com.futrtch.live.base.BaseResponBean;
import com.futrtch.live.base.RequestBodyMaker;
import com.futrtch.live.base.RetrofitTools;
import com.futrtch.live.http.apis.LiveRoomService;

import java.util.HashMap;

import io.reactivex.rxjava3.core.Flowable;
import okhttp3.RequestBody;

public class RoomRequestBuilder {
    /**
     * 直播间建权
     * @param mUserId 账户id
     * @param mTitle 直播间标题
     * @param mCoverPicUrl 直播间图片
     * @param mLocation 直播位置
     * @param userName 账号
     * @param token 令牌
     * @return 被观察者
     */
    public static Flowable<BaseResponBean> roomSuccessFlowable(String mUserId, String mTitle, String mCoverPicUrl, String mLocation, String userName, String token){
        HashMap<String, String> requestParam = new HashMap<>();
        requestParam.put("userId", mUserId);
        requestParam.put("frontCover", mCoverPicUrl);
        requestParam.put("location", mLocation);
        Pair<RequestBody, String> requestInfo =
                RequestBodyMaker.getRequestBodyAndToken(requestParam, userName, token);
        return RetrofitTools.getInstance(LiveRoomService.class)
                .roomSuccess(requestInfo.second, requestInfo.first);
    }

}
