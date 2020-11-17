package com.futrtch.live.utils;

import android.animation.ObjectAnimator;
import android.view.View;

import java.util.Optional;

/**
 * 属性动画
 */
public class AnimatorUtils {

    private ObjectAnimator mObjAnim;               // 动画
    private View view;                             // 展示动画的view

    public AnimatorUtils(View view){
        this.view = view;
    }

    /**
     *  alpha 动画
     */
    public void startAlphaChange() {
        mObjAnim = ObjectAnimator.ofFloat(this.view, "alpha", 1f, 0f, 1f);
        mObjAnim.setDuration(1000);
        mObjAnim.setRepeatCount(-1);
        mObjAnim.start();
    }

    /**
     * 渐隐动画
     */
    public void AlphaHide(){
        mObjAnim = ObjectAnimator.ofFloat(this.view, "alpha", 1f, 0f);
        mObjAnim.setDuration(1000);
        mObjAnim.setRepeatCount(1);
        mObjAnim.start();
    }

    /**
     * 渐显动画
     */
    public void AlphaShow(){
        mObjAnim = ObjectAnimator.ofFloat(this.view, "alpha",  0f, 1f);
        mObjAnim.setDuration(1000);
        mObjAnim.setRepeatCount(1);
        mObjAnim.start();
    }

    public void release(){
        Optional.ofNullable(mObjAnim).ifPresent(objectAnimator -> mObjAnim.cancel());
    }
}
