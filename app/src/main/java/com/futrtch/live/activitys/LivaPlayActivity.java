package com.futrtch.live.activitys;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionSet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.animation.PathInterpolatorCompat;
import androidx.databinding.DataBindingUtil;

import com.futrtch.live.R;
import com.futrtch.live.databinding.ActivityLiveBinding;

/**
 * 直播播放页面
 */
public class LivaPlayActivity extends AppCompatActivity {
    ActivityLiveBinding mDataBinding;
    final static Long LARGE_EXPAND_DURATION = 300L;
    final static Long LARGE_COLLAPSE_DURATION = 250L;

    final static String TRANSITION_NAME_IMAGE = "btn";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_live);
        ViewCompat.setTransitionName(mDataBinding.ivAvatarImg, TRANSITION_NAME_IMAGE);
        /**
         * 2、设置WindowTransition,除指定的ShareElement外，其它所有View都会执行这个Transition动画
         */
        getWindow().setEnterTransition(new Fade());
        getWindow().setExitTransition(new Fade());
        /**
         * 3、设置ShareElementTransition,指定的ShareElement会执行这个Transiton动画
         */
        TransitionSet transitionSet = new TransitionSet();
        transitionSet.addTransition(new ChangeBounds());
        transitionSet.addTransition(new ChangeTransform());
        transitionSet.addTarget(mDataBinding.ivAvatarImg);
        getWindow().setSharedElementEnterTransition(transitionSet);
        getWindow().setSharedElementExitTransition(transitionSet);
    }

}
