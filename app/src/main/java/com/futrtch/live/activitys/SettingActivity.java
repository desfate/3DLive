package com.futrtch.live.activitys;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.futrtch.live.R;
import com.futrtch.live.adapters.SettingAdpater;
import com.futrtch.live.databinding.ActivitySettingBinding;
import com.futrtch.live.databinding.LayoutSettingFooterBinding;
import com.futrtch.live.mvvm.MVVMActivity;
import com.futrtch.live.mvvm.vm.SettingViewModel;
import com.futrtch.live.tencent.login.TCUserMgr;
import com.futrtch.live.utils.ActivityUtils;
import com.futrtch.live.utils.VersionUtils;
import com.jakewharton.rxbinding4.view.RxView;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

public class SettingActivity extends MVVMActivity {

    ActivitySettingBinding mDataBinding;
    SettingViewModel mViewModel;
    SettingAdpater mAdapter;

    @Override
    public void initViewModel() {
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        ViewModelProvider.Factory factory = new ViewModelProvider.AndroidViewModelFactory(getApplication());
        mViewModel = ViewModelProviders.of(this, factory).get(SettingViewModel.class);
    }

    @Override
    public void init() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mDataBinding.recyclerList.setLayoutManager(manager);
        mAdapter = new SettingAdpater(this, mViewModel.getmListData());
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
//                System.out.println(mViewModel.getmListData().get(position));
                if(mViewModel.getmListData().get(position).getSettingName().equals("退出登录")){
                    TCUserMgr.getInstance().logout();
                    Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        });
        mAdapter.addFooterView(getFootView());
        mDataBinding.recyclerList.setAdapter(mAdapter);
        mViewModel.prepare();
    }

    @Override
    public void bindUi() {
        RxView.clicks(mDataBinding.backImg)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> finish());
    }

    @Override
    public void subscribeUi() {

    }

    @Override
    public void initRequest() {

    }

    private View getFootView(){
        LayoutSettingFooterBinding footerBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.layout_setting_footer, mDataBinding.recyclerList, false);
        footerBinding.footView.setText(VersionUtils.getAppVersionName(this));
        return footerBinding.getRoot();
    }
}
