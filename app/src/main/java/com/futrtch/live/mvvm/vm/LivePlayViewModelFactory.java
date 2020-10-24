package com.futrtch.live.mvvm.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.futrtch.live.mvvm.repository.LiveRoomRepository;

public class LivePlayViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {
    LifecycleOwner lifecycleOwner;
    /**
     * Creates a {@code AndroidViewModelFactory}
     *
     * @param application an application to pass in {@link androidx.lifecycle.AndroidViewModel}
     */
    public LivePlayViewModelFactory(@NonNull Application application, LifecycleOwner lifecycleOwner) {
        super(application);
        this.lifecycleOwner = lifecycleOwner;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new LivePlayViewModel(lifecycleOwner, new LiveRoomRepository(lifecycleOwner));
    }
}