package com.futrtch.live.mvvm.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SettingViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    LifecycleOwner lifecycleOwner;
    /**
     * Creates a {@code AndroidViewModelFactory}
     *
     * @param application an application to pass in {@link AndroidViewModel}
     */
    public SettingViewModelFactory(@NonNull Application application, @NonNull LifecycleOwner lifecycleOwner) {
        super(application);
        this.lifecycleOwner = lifecycleOwner;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SettingViewModel();
    }
}
