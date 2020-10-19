package com.futrtch.live.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.futrtch.live.repositorys.LoginRepository;

public class RegisterViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    LoginRepository loginRepository;
    LifecycleOwner lifecycleOwner;
    /**
     * Creates a {@code AndroidViewModelFactory}
     *
     * @param application an application to pass in {@link AndroidViewModel}
     */
    public RegisterViewModelFactory(@NonNull Application application, @NonNull LifecycleOwner lifecycleOwner) {
        super(application);
        loginRepository = LoginRepository.getInstance();
        loginRepository.setmContext(application);
        this.lifecycleOwner = lifecycleOwner;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RegisterViewModel(loginRepository, lifecycleOwner);
    }
}
