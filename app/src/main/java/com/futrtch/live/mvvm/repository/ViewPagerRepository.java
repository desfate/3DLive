package com.futrtch.live.mvvm.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * viewpager页面数据仓库
 */
public class ViewPagerRepository {

    private static volatile ViewPagerRepository singleton = null;

    private final String[] mFriendTitles = new String[]{"朋友列表", "发现朋友"};
    private final String[] mMineTitles = new String[]{"作品", "动态", "喜欢"};
    private final String[] mLiveTitles = new String[]{"推荐", "关注", "回放"};

    private final List<Integer> mIndexTwo = new ArrayList<>(Arrays.asList(0, 1));
    private final List<Integer> mIndexThree = new ArrayList<>(Arrays.asList(0, 1, 2));


    public String[] getFriendTitles() {
        return mFriendTitles;
    }

    public String[] getMineTitles() {
        return mMineTitles;
    }

    public String[] getLiveTitles() {
        return mLiveTitles;
    }

    public List<Integer> getIndexThree() {
        return mIndexThree;
    }

    public List<Integer> getIndexTwo() {
        return mIndexTwo;
    }

    public static ViewPagerRepository getInstance() {
        if (singleton == null) {
            synchronized (ViewPagerRepository.class) {
                if (singleton == null) {
                    singleton = new ViewPagerRepository();
                }
            }
        }
        return singleton;
    }
}
