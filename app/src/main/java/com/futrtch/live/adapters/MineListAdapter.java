package com.futrtch.live.adapters;

import android.content.Context;
import android.text.TextUtils;

import androidx.databinding.DataBindingUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.futrtch.live.R;
import com.futrtch.live.databinding.ListviewMineItemBinding;
import com.futrtch.live.tencent.live.TCVideoInfo;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;


/**
 * 我的页面 列表适配器
 */
public class MineListAdapter extends BaseQuickAdapter<TCVideoInfo, BaseDataBindingHolder<ListviewMineItemBinding>> {

    Context context;

    public MineListAdapter(int layoutResId, Context context, List<TCVideoInfo> mList) {
        super(layoutResId, mList);
        this.context = context;
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder<ListviewMineItemBinding> listviewMineItemBindingBaseDataBindingHolder, TCVideoInfo tcVideoInfo) {
        ListviewMineItemBinding binding = DataBindingUtil.getBinding(listviewMineItemBindingBaseDataBindingHolder.itemView);
        if(binding == null || tcVideoInfo == null) return;
        //直播封面
        String cover = tcVideoInfo.frontCover;
        if (TextUtils.isEmpty(cover)) {
            switch (new Random().nextInt(3)){
                case 0:
                    binding.anchorBtnCover.setImageResource(R.mipmap.liveroom1);
                    break;
                case 1:
                    binding.anchorBtnCover.setImageResource(R.mipmap.liveroom2);
                    break;
                case 2:
                    binding.anchorBtnCover.setImageResource(R.mipmap.liveroom3);
                    break;
            }
        }
    }
}
