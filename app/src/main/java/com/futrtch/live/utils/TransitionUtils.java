package com.futrtch.live.utils;

import android.transition.TransitionSet;

public class TransitionUtils {

    public static TransitionSet transitionTogether(TransitionSet transitionSet){
        return transitionSet.setOrdering(TransitionSet.ORDERING_TOGETHER);
    }
}
