package com.futrtch.live.adapters;

import android.content.Context;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.futrtch.live.R;
import com.futrtch.live.beans.SettingBean;
import com.futrtch.live.databinding.LayoutSettingItemBinding;
import com.futrtch.live.databinding.LayoutSettingTitleBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SettingAdpater extends BaseMultiItemQuickAdapter<SettingBean, BaseDataBindingHolder<ViewDataBinding>> {

    private static final String TAG = "SettingAdpater";

    Context context;

    public SettingAdpater(Context context, List<SettingBean> mList) {
        super(mList);
        addItemType(SettingBean.CLICK_ITEM_VIEW, R.layout.layout_setting_title);
        addItemType(SettingBean.CLICK_ITEM_CHILD_VIEW, R.layout.layout_setting_item);
        this.context = context;
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder<ViewDataBinding> viewDataBindingBaseDataBindingHolder, SettingBean settingBean) {
        switch (viewDataBindingBaseDataBindingHolder.getItemViewType()) {
            case SettingBean.CLICK_ITEM_VIEW:
                LayoutSettingTitleBinding binding = DataBindingUtil.getBinding(viewDataBindingBaseDataBindingHolder.itemView);
                if (binding == null || settingBean == null) return;
                binding.settingTitle.setText(settingBean.getSettingName());
                break;
            case SettingBean.CLICK_ITEM_CHILD_VIEW:
                LayoutSettingItemBinding bindingItem = DataBindingUtil.getBinding(viewDataBindingBaseDataBindingHolder.itemView);
                if (bindingItem == null || settingBean == null) return;
                bindingItem.settingText.setText(settingBean.getSettingName());
                bindingItem.settingImg.setImageResource(settingBean.getSettingPic());
                if (settingBean.isShowLine()) {
                    bindingItem.line.setVisibility(View.VISIBLE);
                } else {
                    bindingItem.line.setVisibility(View.GONE);
                }
                break;
        }
    }
}
