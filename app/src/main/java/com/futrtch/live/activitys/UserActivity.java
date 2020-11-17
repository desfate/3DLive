package com.futrtch.live.activitys;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.futrtch.live.R;
import com.futrtch.live.activitys.fragments.UserFragment;
import com.futrtch.live.databinding.ActivityUserBinding;
import com.futrtch.live.mvvm.MVVMActivity;
import com.futrtch.live.mvvm.vm.UserViewModel;
import com.futrtch.live.mvvm.vm.UserViewModelFactory;

/**
 * 用户主页
 */
public class UserActivity extends MVVMActivity {

    ActivityUserBinding mDataBinding;
    UserViewModel mViewModel;
    UserFragment mFragment;

    @Override
    public void initViewModel() {
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_user);
        ViewModelProvider.Factory factory = new UserViewModelFactory(getApplication(), this);
        mViewModel = ViewModelProviders.of(this, factory).get(UserViewModel.class);
    }

    @Override
    public void init() {
        mFragment = new UserFragment();
        FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();
        fragmentManager.add(R.id.content_lay, mFragment);
        fragmentManager.show(mFragment);
        fragmentManager.commitAllowingStateLoss();
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
}
