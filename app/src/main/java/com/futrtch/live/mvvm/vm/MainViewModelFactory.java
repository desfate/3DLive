package com.futrtch.live.mvvm.vm;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MainViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    LifecycleOwner lifecycleOwner;
    Context context;
    /**
     * Creates a {@code AndroidViewModelFactory}
     *
     * @param application an application to pass in {@link AndroidViewModel}
     */
    public MainViewModelFactory(@NonNull Application application, LifecycleOwner lifecycleOwner) {
        super(application);
        this.context = application.getApplicationContext();
        this.lifecycleOwner = lifecycleOwner;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainViewModel();
    }
}