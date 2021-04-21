package com.futrtch.live.adapters;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.PagerSnapHelper;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.futrtch.live.databinding.ItemVideoListBinding;
import com.github.desfate.videokit.dates.VideoInfoDate;

import java.util.List;

public class VideoPlayListAdapter extends BaseQuickAdapter<VideoInfoDate, BaseDataBindingHolder<ItemVideoListBinding>> {

    public VideoPlayListAdapter(int layoutResId, List<VideoInfoDate> dates) {
        super(layoutResId, dates);
    }

    @Override
    protected void convert(@NonNull BaseDataBindingHolder<ItemVideoListBinding> holder, VideoInfoDate s) {
        ItemVideoListBinding mDataBinding = DataBindingUtil.getBinding(holder.itemView);
        mDataBinding.videoPlayView.init(s.getVideoPlayUrl());
        mDataBinding.videoPlayView.setPreviewSurface(mDataBinding.anchorPlayView.getSurface());  // 绑定播放器和本地绘制部分
        mDataBinding.videoPlayView.getControl().hideAllView();
    }
}
