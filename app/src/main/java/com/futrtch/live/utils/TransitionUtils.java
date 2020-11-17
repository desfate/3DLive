package com.futrtch.live.utils;

import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.Fade;
import android.transition.TransitionSet;
import android.view.Window;
import android.widget.ImageView;

import androidx.core.view.ViewCompat;

/**
 * 转场动画
 */
public class TransitionUtils {

    public static TransitionSet transitionTogether(TransitionSet transitionSet){
        return transitionSet.setOrdering(TransitionSet.ORDERING_TOGETHER);
    }

    /**
     * 过场动画 （用于列表）
     * @param imageView 点击的图片
     * @param window 当前页面的window
     * @param tag 传递时的tag
     */
    public static void setTransitionAnim(ImageView imageView, Window window, String tag) {
        ViewCompat.setTransitionName(imageView, tag);
        /**
         * 2、设置WindowTransition,除指定的ShareElement外，其它所有View都会执行这个Transition动画
         */
//        window.setEnterTransition(new Fade());
//        window.setExitTransition(new Fade());
        /**
         * 3、设置ShareElementTransition,指定的ShareElement会执行这个Transiton动画
         */
        TransitionSet transitionSet = new TransitionSet();
        transitionSet.addTransition(new Fade());
        transitionSet.addTransition(new ChangeImageTransform());
        transitionSet.addTransition(new ChangeBounds());  //        捕获共享元素的layout bound，然后播放layout bound变化动画
        transitionSet.addTransition(new ChangeTransform()); //      捕获共享元素的缩放（scale）与旋转（rotation）属性 ，然后播放缩放（scale）与旋转（rotation）属性变化动画
        transitionSet.addTarget(imageView);
        window.setSharedElementEnterTransition(transitionSet);  // A启动B，B中的View共享元素的transition(代码所在位置B)
        window.setSharedElementReturnTransition(transitionSet);   // A启动B，A中的View共享元素transition(代码所在位置A)
    }
}
