package com.futrtch.live.views;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public class ViewsClient{

    private LayoutInflater inflater;

    private int layoutId;

    private ViewGroup parent;

    private boolean attachToParent;

    private ViewDataBinding databing;

    public ViewsClient(LayoutInflater inflater, int layoutId, ViewGroup parent, boolean attachToParent) {
        this.inflater = inflater;
        this.layoutId = layoutId;
        this.parent = parent;
        this.attachToParent = attachToParent;
        this.databing = DataBindingUtil.inflate(inflater, layoutId, parent, attachToParent);
    }

    public ViewDataBinding getDataBinding(){
       return databing;
    }
}
