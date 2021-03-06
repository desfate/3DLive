package com.futrtch.live.base;

import com.futrtch.live.beans.LoginResponBean;
import com.jeremyliao.liveeventbus.core.LiveEvent;

import java.io.Serializable;

/**
 * 网络请求回应  这个根据你请求的服务端定义进行修改
 */
public class BaseResponBean<T> implements Serializable, LiveEvent {

    public BaseResponBean(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private T data;
    private String message;

    public void setData(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        if(message == null) message = "";
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
