package com.futrtch.live.base;

/**
 * 自定义异常类
 */
public class ApiException extends Exception {
    private int code;
    private String message;

    public ApiException(Throwable throwable, int statusCode) {
        super(throwable);
        this.code = statusCode;
    }

    public ApiException(int statusCode, String statusDesc) {
        this.code = statusCode;
        this.message = statusDesc;
    }

    public int getStatusCode() {
        return code;
    }

    public void setStatusCode(int statusCode) {
        this.code = statusCode;
    }

    public String getStatusDesc() {
        return message;
    }

    public void setStatusDesc(String statusDesc) {
        this.message = statusDesc;
    }
}