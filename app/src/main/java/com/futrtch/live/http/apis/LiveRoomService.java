package com.futrtch.live.http.apis;

import com.futrtch.live.base.BaseResponBean;

import io.reactivex.rxjava3.core.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface LiveRoomService {
    /**
     * 直播间创建成功后的鉴权
     * @param requestBody 自己封装请求包
     * @return
     */
    @POST("/upload_room")
    Flowable<BaseResponBean> roomSuccess(@Header("Liteav-Sig") String sign, @Body RequestBody requestBody);
}
