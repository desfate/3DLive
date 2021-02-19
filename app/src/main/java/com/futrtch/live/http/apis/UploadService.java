package com.futrtch.live.http.apis;

import com.futrtch.live.base.BaseResponBean;

import io.reactivex.rxjava3.core.Flowable;

/**
 * 上傳接口
 */
public interface UploadService {

    public Flowable<BaseResponBean> uploadPic();

}
