package com.futrtch.live.views;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

import com.futrtch.live.R;

import java.util.Objects;

import github.com.desfate.livekit.utils.ScreenUtils;

public class BottomDialog extends Dialog {

    public BottomDialog(@NonNull Context context, ViewDataBinding mDataBinding) {
        super(context, R.style.BottomDialogStyle);
        setContentView(mDataBinding.getRoot());
    }

    public void showDialog(){
        Objects.requireNonNull(getWindow()).setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = (int) (ScreenUtils.getScreenSize(getContext()).getWidth()*0.95);
        getWindow().setAttributes(lp);
        show();
    }

}
