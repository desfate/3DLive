package com.futrtch.live.http;

import org.json.JSONException;
import org.json.JSONObject;

public class LiveRoomReqUtils {

    /**
     * 生成用户上传的直播房间信息
     * @param roomTitle 房间标题
     * @param location 当前主播位置
     * @param roomImg 房间图片
     * @return 房间信息的json对象
     */
    public static String toRoomInfoString(String roomTitle, String location, String roomImg){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("title", roomTitle);
            jsonObject.put("location", location);
            jsonObject.put("frontcover", roomImg);
            return jsonObject.toString();
        } catch (JSONException e) {
            return roomTitle;
        }
    }
}
