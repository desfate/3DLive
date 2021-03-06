package com.futrtch.live.widgets;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.futrtch.live.R;

public class LoadingDialog extends Dialog {

    public LoadingDialog(Context context) {
        super(context);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        public static final int LOADING = 0, PROGRESS = 1;

        private Context mContext;
        private String mMessage;
        private int mType = LOADING;
        private LoadingDialog mDialog;

        private View mLoadingView;
        private FadeProgressView mProgressView;
        private TextView mTextView;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setMessage(String message) {
            this.mMessage = message;
            return this;
        }

        public Builder setMessage(int message) {
            this.mMessage = (String) mContext.getText(message);
            return this;
        }

        public Builder setType(int type) {
            this.mType = type;
            return this;
        }

        /**
         * 在0到100之间
         *
         * @param progress
         */
        public void setProgress(long progress) {
            mProgressView.setCurrent(progress);
            mProgressView.flush();
            if (progress > mProgressView.getTotal())
                progress = mProgressView.getTotal();
            mTextView.setText(progress + "%");
        }

        public long getProgress() {
            return mProgressView.getCurrent();
        }

        public LoadingDialog create() {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mDialog = new LoadingDialog(mContext, R.style.Dialog);
            View v = inflater.inflate(R.layout.widget_custom_progress_dialog, null);
            mLoadingView = v.findViewById(R.id.loading);
            mProgressView = (FadeProgressView) v.findViewById(R.id.progress);
            mTextView = (TextView) v.findViewById(R.id.text);

            if (mType == PROGRESS) {
                mLoadingView.setVisibility(View.INVISIBLE);
                mProgressView.setVisibility(View.VISIBLE);
                mTextView.setVisibility(View.VISIBLE);
            }

            mDialog.addContentView(v, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ((TextView) v.findViewById(R.id.message)).setText(mMessage);
            mDialog.setContentView(v);
            return mDialog;
        }

        public LoadingDialog getObj() {
            return mDialog;
        }

    }
}
