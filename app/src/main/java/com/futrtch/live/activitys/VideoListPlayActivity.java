package com.futrtch.live.activitys;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Configuration;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.futrtch.live.R;
import com.futrtch.live.adapters.VideoPlayListAdapter;
import com.futrtch.live.databinding.ActivityVideoListPlayBinding;
import com.futrtch.live.databinding.ActivityVideoPlayerBinding;
import com.futrtch.live.databinding.ItemVideoListBinding;
import com.futrtch.live.mvvm.MVVMActivity;
import com.futrtch.live.mvvm.vm.VideoListPlayViewModel;
import com.futrtch.live.mvvm.vm.VideoListPlayViewModelFactory;
import com.futrtch.live.mvvm.vm.VideoPlayViewModel;
import com.futrtch.live.mvvm.vm.VideoPlayViewModelFactory;
import com.github.desfate.videokit.dates.VideoInfoDate;
import com.github.desfate.videokit.ui.VideoPlayView;
import com.jakewharton.rxbinding4.view.RxView;

import java.util.List;
import java.util.Objects;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import github.com.desfate.livekit.ui.DualLivePlayView;
import github.com.desfate.livekit.utils.ScreenUtils;

/**
 * 类似抖音的列表播放
 */
public class VideoListPlayActivity extends MVVMActivity implements android.view.GestureDetector.OnGestureListener{

    ActivityVideoPlayerBinding mDataBinding;
    VideoListPlayViewModel mViewModel;
    // 定义手势检测器实例
    GestureDetector detector;


    @Override
    public void initViewModel() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //                      全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_video_player);
        ViewModelProvider.Factory factory = new VideoListPlayViewModelFactory(this.getApplication());
        mViewModel = ViewModelProviders.of(this, factory).get(VideoListPlayViewModel.class);
    }


    @Override
    public void init() {
        mViewModel.init(getIntent());
        mDataBinding.videoPlayView.init(mViewModel.getPlayUrls().get(mViewModel.getCurrentPosition()).getVideoPlayUrl());
        mDataBinding.videoPlayView.setPreviewSurface(mDataBinding.anchorPlayView.getSurface());  // 绑定播放器和本地绘制部分
        mDataBinding.videoPlayView.getControl().hideAllViewExceptTitle();
        mDataBinding.videoPlayView.getBackImg().setVisibility(View.VISIBLE);
        mDataBinding.videoPlayView.getTitleView().setVisibility(View.VISIBLE);
        // 创建手势检测器
        detector = new GestureDetector(this, this);
    }

    @Override
    public void bindUi() {
        RxView.clicks( mDataBinding.videoPlayView.getBackImg())
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> finish());

        RxView.clicks( mDataBinding.videoPlayView.getTitleView())
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> finish());

    }

    @Override
    public void subscribeUi() {
        mViewModel.getCurrentVideoName().observe(this, s -> mDataBinding.videoPlayView.getTitleView().setText(s));
    }

    @Override
    public void initRequest() {

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams)mDataBinding.videoPlayView.getLayoutParams();
        ConstraintLayout.LayoutParams params2 = (ConstraintLayout.LayoutParams)mDataBinding.anchorPlayView.getLayoutParams();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) { // 横屏
            params.dimensionRatio = "16:9";
//            params2.dimensionRatio = "16:9";
            mDataBinding.videoPlayView.setLayoutParams(params);
//            mDataBinding.anchorPlayView.setLayoutParams(params2);
        } else {
            params.dimensionRatio = "9:16";
//            params2.dimensionRatio = "9:16";
            mDataBinding.videoPlayView.setLayoutParams(params);
//            mDataBinding.anchorPlayView.setLayoutParams(params2);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDataBinding.videoPlayView.release();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        float minMove = 120; // 最小滑动距离
        float minVelocity = 0; // 最小滑动速度
        float beginX = e1.getX();
        float endX = e2.getX();
        float beginY = e1.getY();
        float endY = e2.getY();

        if (beginX - endX > minMove && Math.abs(velocityX) > minVelocity) { // 左滑
//            Toast.makeText(this, velocityX + "左滑", Toast.LENGTH_SHORT).show();
            translationRight();
            mDataBinding.videoPlayView.playOther( mViewModel.getNextUrl());
        } else if (endX - beginX > minMove && Math.abs(velocityX) > minVelocity) { // 右滑
//            Toast.makeText(this, velocityX + "右滑", Toast.LENGTH_SHORT).show();
            translationLeft();
            mDataBinding.videoPlayView.playOther( mViewModel.getNextUrl());
        } else if (beginY - endY > minMove && Math.abs(velocityY) > minVelocity) { // 上滑
//            Toast.makeText(this, velocityX + "上滑", Toast.LENGTH_SHORT).show();
            translationUp();
//            mViewModel.getNextUrl()
            mDataBinding.videoPlayView.playOther( mViewModel.getNextUrl());
        } else if (endY - beginY > minMove && Math.abs(velocityY) > minVelocity) { // 下滑
            translationDown();
            mDataBinding.videoPlayView.playOther( mViewModel.getNextUrl());
//            Toast.makeText(this, velocityX + "下滑", Toast.LENGTH_SHORT).show();
        }

        return false;
    }


    public void translationUp(){
        ObjectAnimator animator = ObjectAnimator.ofFloat( mDataBinding.anchorPlayView,"translationY", ScreenUtils.getScreenSize(this).getHeight(),0);//文字标签Y轴平移
        animator.setDuration(600);
        AnimatorSet animatorSet= new AnimatorSet();//创建动画集
        animatorSet.play(animator);
        animatorSet.start();
    }

    public void translationDown(){
        ObjectAnimator animator = ObjectAnimator.ofFloat( mDataBinding.anchorPlayView,"translationY",-ScreenUtils.getScreenSize(this).getHeight(), 0);//文字标签Y轴平移
        animator.setDuration(600);
        AnimatorSet animatorSet= new AnimatorSet();//创建动画集
        animatorSet.play(animator);
        animatorSet.start();
    }

    public void translationRight(){
        ObjectAnimator animator = ObjectAnimator.ofFloat( mDataBinding.anchorPlayView,"translationX",ScreenUtils.getScreenSize(this).getWidth(),0);//文字标签Y轴平移
        animator.setDuration(600);
        AnimatorSet animatorSet= new AnimatorSet();//创建动画集
        animatorSet.play(animator);
        animatorSet.start();
    }

    public void translationLeft(){
        ObjectAnimator animator = ObjectAnimator.ofFloat( mDataBinding.anchorPlayView,"translationX",-ScreenUtils.getScreenSize(this).getWidth(),0);//文字标签Y轴平移
        animator.setDuration(600);
        AnimatorSet animatorSet= new AnimatorSet();//创建动画集
        animatorSet.play(animator);
        animatorSet.start();
    }
}
