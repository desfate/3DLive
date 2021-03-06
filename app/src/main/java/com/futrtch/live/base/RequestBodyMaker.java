package com.futrtch.live.base;

import androidx.core.util.Pair;

import com.futrtch.live.tencent.common.utils.TCUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 请求包构造器
 */
public class RequestBodyMaker {
    /**
     * 生成普通RequestBody
     *
     * @param paramsMap 请求的key value
     * @return body
     */
    public static RequestBody getRequestBodyForParams(Map<String, String> paramsMap) {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        JSONObject jo = new JSONObject();
        if (paramsMap != null) {
            for (String key : paramsMap.keySet()) {
                try {
                    jo.put(key, paramsMap.get(key));
                } catch (JSONException ignored) {
                }
            }
        }
        return RequestBody.create(mediaType, jo.toString());
    }

    /**
     * 生成RequestBody with token  鉴权模式下的body
     *
     * @param paramsMap 请求的key value
     * @param userid    请求账户id
     * @return body
     */
    public static Pair<RequestBody, String> getRequestBodyAndToken(Map<String, String> paramsMap, String userid, String token) {
        if(paramsMap == null) paramsMap = new HashMap<>();
        paramsMap.put("userid", userid);
        paramsMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        paramsMap.put("expires", "10");
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        JSONObject jo = new JSONObject();
        for (String key : paramsMap.keySet()) {
            try {
                jo.put(key, paramsMap.get(key));
            } catch (JSONException ignored) {
            }
        }
        String headSign = getRequestSig(jo, token, userid);
        return new Pair<>(RequestBody.create(mediaType, jo.toString()), headSign);
    }

    /**
     * 计算请求的 Sign,用于确认是合法用户访问。
     *
     * @param body
     * @return
     */
    public static String getRequestSig(JSONObject body, String mToken, String mUserId) {
        String strBody = null;
        try {
            strBody = body.put("userid", mUserId)
                    .put("timestamp", System.currentTimeMillis() / 1000)
                    .put("expires", 10)
                    .toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String sig = TCUtils.md5(mToken + TCUtils.md5(strBody));
        return sig;
    }
}
