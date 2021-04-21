package com.futrtch.live.activitys;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.futrtch.live.R;
import com.futrtch.live.base.BaseResponBean;
import com.futrtch.live.beans.AccountInfoBean;
import com.futrtch.live.databinding.ActivityEditBinding;
import com.futrtch.live.databinding.LayoutAccountDialogBinding;
import com.futrtch.live.databinding.LayoutPicDialogBinding;
import com.futrtch.live.http.RequestTags;
import com.futrtch.live.mvvm.MVVMActivity;
import com.futrtch.live.mvvm.repository.LoginRepository;
import com.futrtch.live.mvvm.vm.EditViewModel;
import com.futrtch.live.mvvm.vm.EditViewModelFactory;
import com.futrtch.live.mvvm.vm.MainViewModel;
import com.futrtch.live.mvvm.vm.MainViewModelFactory;
import com.futrtch.live.utils.ToastUtil;
import com.futrtch.live.views.BottomDialog;
import com.futrtch.live.widgets.LoadingDialog;
import com.jakewharton.rxbinding4.view.RxView;
import com.jeremyliao.liveeventbus.LiveEventBus;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import io.reactivex.rxjava3.functions.Consumer;
import kotlin.Unit;

/**
 * 编辑资料页面  2级页面
 */
public class EditActivity extends MVVMActivity {

    ActivityEditBinding mDataBinding;
    LayoutPicDialogBinding mDialogBinding;
    LayoutAccountDialogBinding mAccountBinding;
    BottomDialog mDialog;
    BottomDialog mAccountDialog;
    LoadingDialog.Builder mLoading; // 加载页面

    EditViewModel mViewModel;

    @Override
    public void initViewModel() {
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit);
        mDialogBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.layout_pic_dialog, null , false);
        mAccountBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.layout_account_dialog, null , false);
        ViewModelProvider.Factory factory = new EditViewModelFactory(getApplication(), this);
        mViewModel = ViewModelProviders.of(this, factory).get(EditViewModel.class);
    }

    public void init() {
        mDialog = new BottomDialog(this, mDialogBinding);
        mAccountDialog = new BottomDialog(this, mAccountBinding);
        mLoading = new LoadingDialog.Builder(EditActivity.this);
        mLoading.setMessage(getString(R.string.request_text));
        mLoading.create();
    }

    public void bindUi() {
        RxView.clicks(mDataBinding.backImg)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> finish());

        RxView.clicks(mDataBinding.nameLay)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> mAccountDialog.showDialog());

        RxView.clicks(mDataBinding.cameraHeadBtn)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> mDialog.showDialog());

        // 头像 dialog

        RxView.clicks(mDialogBinding.cancelTv)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> mDialog.cancel());

        RxView.clicks(mDialogBinding.takePicTv)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> mDialog.cancel());

        RxView.clicks(mDialogBinding.picStoreTv)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> mDialog.cancel());

        // edit dialog
        RxView.clicks(mAccountBinding.submitBtn)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(unit -> {
                    if(mAccountBinding.nameEdt.getText().toString().trim().length() > 0){
                        mLoading.getObj().show();
                        // 请求修改用户名称
                        mViewModel.requestEditUserInfoName(mAccountBinding.nameEdt.getText().toString().trim());
                    }
                    mAccountDialog.cancel();
                });

    }
    public void subscribeUi() {
        mViewModel.getAccountInfo().observe(this, accountInfoBean -> {
            mDataBinding.numTv.setText(accountInfoBean.getUserid());
            mDataBinding.nameTv.setText(accountInfoBean.getNickname());
        });

        // 编辑账户信息返回
        LiveEventBus.get(RequestTags.EDIT_ACCOUNTINFO, BaseResponBean.class)
                .observe(this, baseResponBean -> {
                    mLoading.getObj().dismiss();
                    if(baseResponBean.getCode() == 200){
                        ToastUtil.showToast(EditActivity.this, "修改成功");
                        mLoading.getObj().show();
                        LoginRepository.getInstance().getAccountInfo(EditActivity.this);
                    }
                });
        // 请求账户信息返回
        LiveEventBus.get(RequestTags.ACCOUNT_INFO_REQ, BaseResponBean.class)
                .observe(this, baseResponBean -> {
                    mViewModel.getAccountInfo().postValue(((AccountInfoBean)baseResponBean.getData()));
                    mLoading.getObj().dismiss();
                });
    }

    @Override
    public void initRequest() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mViewModel.onActivityResult(requestCode, resultCode, data);
    }
}
