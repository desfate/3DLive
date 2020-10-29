package com.futrtch.live.activitys;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.futrtch.live.R;
import com.futrtch.live.databinding.ActivityEditBinding;
import com.futrtch.live.utils.ToastUtil;
import com.jakewharton.rxbinding4.view.RxView;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

/**
 * 编辑资料页面  2级页面
 */
public class EditActivity extends AppCompatActivity {

    ActivityEditBinding mDataBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit);
        RxView.clicks(mDataBinding.nameLay).to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> ToastUtil.showToast(getApplicationContext(), "名字"));
    }
}
