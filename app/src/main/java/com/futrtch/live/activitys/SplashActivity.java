package com.futrtch.live.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.futrtch.live.R;
import com.futrtch.live.databinding.ActivitySplashBinding;

/**
 * 广告页面
 */
public class SplashActivity extends AppCompatActivity {

    ActivitySplashBinding mDataBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        new Handler().postDelayed(this::jumpActivity, 800);
    }

    private void jumpActivity(){
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        // 禁止使用返回键
    }
}
