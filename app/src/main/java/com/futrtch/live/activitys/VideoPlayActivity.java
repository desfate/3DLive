package com.futrtch.live.activitys;

import android.content.res.Configuration;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.futrtch.live.R;
import com.futrtch.live.databinding.ActivityVideoPlayerBinding;
import com.futrtch.live.mvvm.MVVMActivity;
import com.futrtch.live.mvvm.vm.LivePlayViewModel;
import com.futrtch.live.mvvm.vm.LivePlayViewModelFactory;
import com.futrtch.live.mvvm.vm.VideoPlayViewModel;
import com.futrtch.live.mvvm.vm.VideoPlayViewModelFactory;

/**
 * 播放视频
 */
public class VideoPlayActivity extends MVVMActivity {

    ActivityVideoPlayerBinding mDataBinding;
    VideoPlayViewModel mViewModel;

    @Override
    public void initViewModel() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //                      全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_video_player);
        ViewModelProvider.Factory factory = new VideoPlayViewModelFactory(this.getApplication());
        mViewModel = ViewModelProviders.of(this, factory).get(VideoPlayViewModel.class);
    }

    @Override
    public void init() {
        mDataBinding.videoPlayView.init(getIntent().getStringExtra("play_url"));
        mDataBinding.videoPlayView.setPreviewSurface(mDataBinding.anchorPlayView.getSurface());  // 绑定播放器和本地绘制部分
    }

    @Override
    public void bindUi() {

    }

    @Override
    public void subscribeUi() {

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
}
