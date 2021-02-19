package com.futrtch.live.http.apis;

import com.futrtch.live.base.BaseResponBean;
import com.futrtch.live.beans.AccountInfoBean;

import io.reactivex.rxjava3.core.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * 账户接口
 */
public interface AccountService {
    /**
     * 请求账户信息
     * @param requestBody 自己封装请求包
     * @return
     */
    @POST("/get_user_info")
    Flowable<BaseResponBean<AccountInfoBean>> accountInfo(@Header ("Liteav-Sig") String sign, @Body RequestBody requestBody);

    /**
     * 更新账户信息
     * @param sign token
     * @param requestBody 请求信息
     * @return 
     */
    @POST("/upload_user_info")
    Flowable<BaseResponBean> updateInfo(@Header ("Liteav-Sig") String sign, @Body RequestBody requestBody);
}
