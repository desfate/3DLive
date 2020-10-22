package com.futrtch.live.activitys;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.futrtch.live.R;
import com.futrtch.live.databinding.ActivityRecordBinding;
import com.futrtch.live.tencent.common.msg.TCChatMsgListAdapter;
import com.futrtch.live.tencent.common.widget.RTCUserAvatarListAdapter;
import com.futrtch.live.viewmodel.LiveRecordViewModel;
import com.futrtch.live.viewmodel.LiveRecordViewModelFactory;

/**
 * 主播录制页面
 */
public class LiveRecordActivity extends BaseIMLVBActivity{

    private static final String TAG = "LiveRecordActivity";

    private LiveRecordViewModel mViewModel;
    private ActivityRecordBinding mDataBinding;

    private RTCUserAvatarListAdapter mAvatarListAdapter;  // 头像列表适配器
    private TCChatMsgListAdapter mChatMsgListAdapter;    // 消息列表的Adapter

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider.Factory factory = new LiveRecordViewModelFactory(getApplication());
        mViewModel = ViewModelProviders.of(this,factory).get(LiveRecordViewModel.class);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_record);



    }
}
