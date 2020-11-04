package com.futrtch.live.views;

import android.view.LayoutInflater;
import android.view.ViewGroup;

public class ViewsBuilder {

    private LayoutInflater inflater;

    private int layoutId;

    private ViewGroup parent;

    private boolean attachToParent;

    public ViewsBuilder setInflater(LayoutInflater inflater) {
        if (inflater == null) {
            throw new NullPointerException("inflater == null");
        }
        this.inflater = inflater;
        return this;
    }

    public ViewsBuilder setLayoutId(int layoutId) {
        if (layoutId <= 0) {
            throw new NullPointerException("layoutId == null");
        }
        this.layoutId = layoutId;
        return this;
    }

    public ViewsBuilder setParent(ViewGroup parent) {
        if (parent == null) {
            throw new NullPointerException("ViewGroup == null");
        }
        this.parent = parent;
        return this;
    }

    public ViewsBuilder setAttachToParent(boolean attachToParent) {
        this.attachToParent = attachToParent;
        return this;
    }

    public ViewsClient build(){
        return new ViewsClient(this.inflater, this.layoutId, this.parent, this.attachToParent);
    }
}
