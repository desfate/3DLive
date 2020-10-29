package com.futrtch.live.mvvm.repository;

import androidx.lifecycle.LifecycleOwner;

public class MineRepository {

    LifecycleOwner lifecycleOwner;

    public MineRepository(LifecycleOwner lifecycleOwner){
        this.lifecycleOwner = lifecycleOwner;
    }
}
