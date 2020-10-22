package com.futrtch.live.tencent.common.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.futrtch.live.R;
import com.futrtch.live.tencent.liveroom.roomutil.commondef.MLVBCommonDef;


/**
 * Created by Administrator on 2016/9/26.
 */
public class ErrorDialogFragment extends DialogFragment {

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE,R.style.AppTheme);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int errorCode = getArguments().getInt("errorCode");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.ConfirmDialogStyle)
                .setCancelable(true)
                .setPositiveButton("确定", (dialog, which) -> {
                    dialog.dismiss();
                    getActivity().finish();
                });
        if (errorCode == MLVBCommonDef.LiveRoomErrorCode.ERROR_LICENSE_INVALID) {
            String errInfo = "License 校验失败";
            int start = (errInfo + " 详情请点击[").length();
            int end = (errInfo + " 详情请点击[License 使用指南").length();
            SpannableStringBuilder spannableStrBuidler = new SpannableStringBuilder(errInfo + " 详情请点击[License 使用指南]");
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse("https://cloud.tencent.com/document/product/454/34750");
                    intent.setData(content_url);
                    startActivity(intent);
                }
            };
            spannableStrBuidler.setSpan(new ForegroundColorSpan(Color.BLUE), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableStrBuidler.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            TextView tv = new TextView(this.getActivity());
            tv.setMovementMethod(LinkMovementMethod.getInstance());
            tv.setText(spannableStrBuidler);
            tv.setPadding(20, 50, 20, 0);
            builder.setView(tv).setTitle("推流失败");
        } else {
            String errInfo = getArguments().getString("errorMsg");
            builder.setTitle(errInfo);
        }
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }
}
