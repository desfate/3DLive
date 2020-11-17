package com.futrtch.live.adapters;

import android.content.Context;
import android.text.TextUtils;

import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.futrtch.live.databinding.ListviewVideoItemBinding;
import com.futrtch.live.tencent.common.utils.TCUtils;
import com.futrtch.live.tencent.live.TCVideoInfo;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class LiveListAdapter extends BaseQuickAdapter<TCVideoInfo, BaseDataBindingHolder<ListviewVideoItemBinding>>{
    private final Context mContext;

    public LiveListAdapter(int layoutResId, Context context, List<TCVideoInfo> mList) {
        super(layoutResId, mList);
        this.mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder<ListviewVideoItemBinding> bindingBaseDataBindingHolder, TCVideoInfo info) {
        ListviewVideoItemBinding binding = DataBindingUtil.getBinding(bindingBaseDataBindingHolder.itemView);
        if(binding == null || info == null) return;
        ViewCompat.setTransitionName(binding.anchorTvTitle, "title-" + info.userId);
        ViewCompat.setTransitionName(binding.anchorBtnCover, "btn");
        ViewCompat.setTransitionName(binding.hostName, "host-" + info.userId);
        ViewCompat.setTransitionName(binding.liveLbs, "libs-" + info.userId);
        ViewCompat.setTransitionName(binding.liveMembers, "members-" + info.userId);
        //直播封面
        String cover = info.frontCover;
        binding.anchorBtnCover.setImageResource(Integer.parseInt(info.frontCover));

        //主播头像
        //主播昵称
        if (TextUtils.isEmpty(info.nickname)) {
            binding.hostName.setText(TCUtils.getLimitString(info.userId, 10));
        } else {
            binding.hostName.setText(TCUtils.getLimitString(info.nickname, 10));
        }
        //主播地址
        if (TextUtils.isEmpty(info.location)) {
//            binding.liveLbs.setText(mContext.getString(R.string.live_unknown));
        } else {
            binding.liveLbs.setText(TCUtils.getLimitString(info.location, 9));
        }
        //直播标题
        binding.anchorTvTitle.setText(TCUtils.getLimitString(info.title, 10));
        //直播观看人数
        binding.liveMembers.setText("" + info.viewerCount);
    }
}
