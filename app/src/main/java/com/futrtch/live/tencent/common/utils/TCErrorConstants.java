package com.futrtch.live.tencent.common.utils;

/**
 * 错误码映射类
 */
public class TCErrorConstants {

    public final static int SUCCESS = 200; //                   成功

    /**
     * 来自网络的错误码
     */
    public final static int ERROR_CODE_NO_USER = 620; //                用户不存在
    public final static int ERROR_CODE_PASSWORD_ERR = 621;//            密码错误
    public final static int ERROR_CODE_PASSWORD_FORMAT_ERR = 611; //    密码格式错误
    public final static int ERROR_CODE_USERNAME_FORMAT_ERR =610; //     用户名格式错误
    public final static int ERROR_CODE_REPEAT_USER_ERR =612; //         用户已存在

    /**
     * 本地错误码
     */
    public final static int ERROR_CUSTOMER_SUCCESS_PASS = 20000; //   通过校验
    public final static int ERROR_CUSTOMER_USERNAME_ERROR = 20001; // 用户名不符合规范！
    public final static int ERROR_CUSTOMER_PASSWORD_ERROR = 20002; // 密码长度应为8-16位
    public final static int ERROR_CUSTOMER_REPEAT_ERROR = 20003;   // 两次输入密码不一致


    /**
     * 错误码转提示语
     * @param errorCode 错误码
     * @return 提示语
     */
    public static String getErrorInfo(int errorCode){
        switch (errorCode){
            case ERROR_CODE_PASSWORD_FORMAT_ERR:
                return "密码格式错误";
            case ERROR_CODE_USERNAME_FORMAT_ERR:
                return "用户名格式错误";
            case ERROR_CODE_REPEAT_USER_ERR:
                return "用户已存在";
            case ERROR_CUSTOMER_SUCCESS_PASS:
                return "通过校验";
            case ERROR_CODE_NO_USER:
                return "用户不存在";
            case ERROR_CODE_PASSWORD_ERR:
                return "密码错误";
            case ERROR_CUSTOMER_USERNAME_ERROR:
                return "用户名不符合规范";
            case ERROR_CUSTOMER_PASSWORD_ERROR:
                return "密码长度应为8-16位";
            case ERROR_CUSTOMER_REPEAT_ERROR:
                return "两次输入密码不一致";
        }
        return "找不到对应的错误码";
    }
}
