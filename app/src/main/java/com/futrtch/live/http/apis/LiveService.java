package com.futrtch.live.http.apis;

import com.futrtch.live.base.BaseResponBean;
import com.futrtch.live.tencent.live.TCVideoInfo;

import java.util.ArrayList;

import io.reactivex.rxjava3.core.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface LiveService {

    /**
     * 获取回看列表
     * @param sign 需要token
     * @param requestBody 请求主体
     * @return Flowable<BaseResponBean<ArrayList<TCVideoInfo>>>
     */
    @POST("/get_vod_list")
    Flowable<BaseResponBean<ArrayList<TCVideoInfo>>> vodList(@Header("Liteav-Sig") String sign, @Body RequestBody requestBody);
}
