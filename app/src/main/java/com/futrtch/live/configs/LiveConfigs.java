package com.futrtch.live.configs;

public class LiveConfigs {

    /**
     * 使用的直播SDK类型
     */
   public enum LiveSDKType{
        TENCENT // 腾讯
        , QINIU // 七牛
    }

    public static LiveSDKType liveSDKType = LiveConfigs.LiveSDKType.TENCENT;

}
