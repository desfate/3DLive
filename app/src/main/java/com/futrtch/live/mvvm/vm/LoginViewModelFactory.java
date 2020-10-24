package com.futrtch.live.mvvm.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.futrtch.live.mvvm.repository.LoginRepository;

public class LoginViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    LoginRepository loginRepository;
    LifecycleOwner lifecycleOwner;
    /**
     * Creates a {@code AndroidViewModelFactory}
     *
     * @param application an application to pass in {@link AndroidViewModel}
     */
    public LoginViewModelFactory(@NonNull Application application, @NonNull LifecycleOwner lifecycleOwner) {
        super(application);
        this.lifecycleOwner = lifecycleOwner;
        loginRepository = LoginRepository.getInstance();
        loginRepository.setmContext(application);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new LoginViewModel(loginRepository, lifecycleOwner);
    }
}
