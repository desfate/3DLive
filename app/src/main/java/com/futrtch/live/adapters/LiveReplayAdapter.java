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
import com.futrtch.live.utils.RandomUtils;
import com.github.desfate.videokit.controls.VideoRequestControls;
import com.github.desfate.videokit.dates.VideoInfoDate;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class LiveReplayAdapter extends BaseQuickAdapter<VideoInfoDate, BaseDataBindingHolder<ListviewVideoItemBinding>> {

    public LiveReplayAdapter(int layoutResId, Context context, List<VideoInfoDate> mList) {
        super(layoutResId, mList);
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder<ListviewVideoItemBinding> bindingBaseDataBindingHolder, VideoInfoDate info) {
        ListviewVideoItemBinding binding = DataBindingUtil.getBinding(bindingBaseDataBindingHolder.itemView);
        if(binding == null || info == null) return;

        ViewCompat.setTransitionName(binding.anchorBtnCover, "btn");

        //视频封面
//        String cover = info.frontCover;
        String cover = RandomUtils.RandomStringPic();
        if(!cover.isEmpty()) {
            binding.anchorBtnCover.setImageResource(RandomUtils.RandomPic());
        }

        //视频标题
        binding.anchorTvTitle.setText(TCUtils.getLimitString(info.getVideoName(), 10));
    }
}
