package com.futrtch.live.base;

import androidx.lifecycle.MutableLiveData;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * 网络请求转换类
 * @param <T>
 */
public class BaseHttpSubscriber<T> implements Subscriber<BaseResponBean<T>>{


    //异常类
    private ApiException ex;
    // 把网络请求返回的数据转换成LiveData
    private MutableLiveData<BaseResponBean<T>> data;

    public BaseHttpSubscriber() {
        data = new MutableLiveData();
    }

    public MutableLiveData<BaseResponBean<T>> get() {
        return data;
    }

    public void set(BaseResponBean<T> t) {
        this.data.setValue(t);
    }

    public void onFinish(BaseResponBean<T> t) {
        set(t);
    }


    @Override
    public void onSubscribe(Subscription s) {
        // 观察者接收事件 = 1个
        s.request(1);
    }

    @Override
    public void onNext(BaseResponBean<T> tBaseResponBean) {
        if (tBaseResponBean.getCode() == 200) {
            onFinish(tBaseResponBean);
        } else{
            getErrorDto(ex);
        }
    }

    @Override
    public void onError(Throwable t) {
        getErrorDto(ex);
    }

    @Override
    public void onComplete() {

    }

    /**
     * 初始化错误的dto
     *
     * @param ex
     */
    private void getErrorDto(ApiException ex) {
        BaseResponBean baseResponBean = new BaseResponBean(ex.getStatusCode(), ex.getMessage());
        onFinish(baseResponBean);
    }
}
