package com.futrtch.live.activitys;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.futrtch.live.R;
import com.futrtch.live.databinding.ActivityMainBinding;
import com.futrtch.live.databinding.LayoutToastViewBinding;
import com.futrtch.live.interfaces.LiveRoomCallBack;
import com.futrtch.live.mvvm.vm.MainViewModel;
import com.futrtch.live.mvvm.vm.MainViewModelFactory;
import com.futrtch.live.tencent.common.msg.TCSimpleUserInfo;
import com.futrtch.live.tencent.common.utils.TCUtils;
import com.futrtch.live.tencent.liveroom.roomutil.commondef.MLVBCommonDef;
import com.futrtch.live.utils.ToastUtil;
import com.futrtch.live.views.ViewsBuilder;

import java.util.List;
import java.util.Optional;

public class MainActivity extends BaseIMLVBActivity implements LiveRoomCallBack {

    MainViewModel mViewModel;
    LayoutToastViewBinding mToastBinding;
    ActivityMainBinding mDataBinding;
    Toast mToast;
    private int lastIndex;

    @Override
    public void initViewModel() {
        super.setLiveRoomCallBack(this);
        ViewModelProvider.Factory factory = new MainViewModelFactory(getApplication(), this);
        mViewModel = ViewModelProviders.of(this, factory).get(MainViewModel.class);
    }

    @Override
    public void init() {
        mViewModel.prepare();
        mViewModel.setExitListener(this);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mToastBinding = (LayoutToastViewBinding) new ViewsBuilder()
                .setParent(mDataBinding.rootLayout)
                .setLayoutId(R.layout.layout_toast_view)
                .setInflater(getLayoutInflater())
                .setAttachToParent(false)
                .build()
                .getDataBinding();
        mToast = ToastUtil.getCustomToast(getApplicationContext(),"",mToastBinding.getRoot());
        setFragmentPosition(0);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void bindUi() {
        mDataBinding.bvBottomNavigation.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.menu_live:
                    setFragmentPosition(0);
                    break;
                case R.id.menu_replay:
                    setFragmentPosition(1);
                    break;
                case R.id.menu_message:
                    setFragmentPosition(2);
                    break;
                case R.id.menu_mine:
                    setFragmentPosition(3);
                    break;
                default:
                    break;
            }
            return true;
        });
    }

    @Override
    public void subscribeUi() {

    }

    @Override
    public void initRequest() {

    }

    private void setFragmentPosition(int position) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment currentFragment = mViewModel.getFragments().get(position);
        Fragment lastFragment = mViewModel.getFragments().get(lastIndex);
        lastIndex = position;
        ft.hide(lastFragment);
        if (!currentFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
            ft.add(R.id.contentPanel, currentFragment);
        }
        ft.show(currentFragment);
        ft.commitAllowingStateLoss();
    }

    private long time;
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - time > 2000) {
                Optional.ofNullable(mToast).ifPresent(Toast::show);
                time = System.currentTimeMillis();
            } else {
                Optional.ofNullable(mToast).ifPresent(Toast::cancel);
                exitAPP();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void exitAPP() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.AppTask> appTaskList = activityManager.getAppTasks();
        for (ActivityManager.AppTask appTask : appTaskList) {
            appTask.finishAndRemoveTask();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mToastBinding.unbind();
        mViewModel.release();
    }

    @Override
    public void messageCallBack(TCSimpleUserInfo info, int type, String message) {

    }

    @Override
    public void roomClosed(String roomID) {

    }

    @Override
    public void roomError(int errorCode, String errorMessage, Bundle extraInfo) {
        if (errorCode == MLVBCommonDef.LiveRoomErrorCode.ERROR_IM_FORCE_OFFLINE) { // IM 被强制下线。
            TCUtils.showKickOut(MainActivity.this);
        }
    }
}