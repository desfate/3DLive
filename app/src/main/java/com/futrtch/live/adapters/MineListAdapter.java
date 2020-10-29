package com.futrtch.live.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.futrtch.live.R;
import com.futrtch.live.databinding.ListviewMineItemBinding;
import com.futrtch.live.tencent.live.TCVideoInfo;

import java.util.Random;


/**
 * 我的页面 列表适配器
 */
public class MineListAdapter extends ListAdapter<TCVideoInfo, MineListAdapter.MineVideoHolder> {


    public MineListAdapter(@NonNull DiffUtil.ItemCallback<TCVideoInfo> diffCallback) {
        super(diffCallback);
    }

    protected MineListAdapter(@NonNull AsyncDifferConfig<TCVideoInfo> config) {
        super(config);
    }

    @NonNull
    @Override
    public MineListAdapter.MineVideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListviewMineItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.listview_mine_item, parent, false);
        return new MineVideoHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull MineListAdapter.MineVideoHolder holder, int position) {
        TCVideoInfo info = getItem(position);
        ListviewMineItemBinding binding = DataBindingUtil.getBinding(holder.itemView);
        if(binding == null) return;
        //直播封面
        String cover = info.frontCover;
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


    static class MineVideoHolder extends RecyclerView.ViewHolder {
        public MineVideoHolder(@NonNull View view) {
            super(view);
        }
    }
}
