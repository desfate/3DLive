package com.futrtch.live.utils;

import com.futrtch.live.R;

import java.util.Random;

public class RandomUtils {

    /**
     * 获得随机图片
     * @return 随机图片id
     */
    public static int RandomPic(){
        switch (new Random().nextInt(3)){
            case 0:
                return R.mipmap.liveroom1;
            case 1:
                return R.mipmap.liveroom2;
            case 2:
                return R.mipmap.liveroom3;
        }
        return R.mipmap.liveroom1;
    }

    /**
     * 获得随机图片
     * @return 随机图片id
     */
    public static String RandomStringPic(){
        int picId = R.mipmap.liveroom1;
        switch (new Random().nextInt(3)){
            case 0:
                picId = R.mipmap.liveroom1;
                break;
            case 1:
                picId = R.mipmap.liveroom2;
                break;
            case 2:
                picId = R.mipmap.liveroom3;
                break;
        }
        return String.valueOf(picId);
    }
}
