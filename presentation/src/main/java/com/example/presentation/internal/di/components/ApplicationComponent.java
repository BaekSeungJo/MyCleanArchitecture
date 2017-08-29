package com.example.presentation.internal.di.components;

import com.example.presentation.AndroidApplication;
import com.example.presentation.internal.di.modules.ActivityModule;
import com.example.presentation.internal.di.modules.ApplicationModule;
import com.example.presentation.internal.di.modules.UserModule;
import com.example.presentation.view.activity.BaseActivity;
import com.example.presentation.view.fragment.UserDetailsFragment;
import com.example.presentation.view.fragment.UserListFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by plnc on 2017-08-29.
 */
@Singleton
@Component(modules = {ApplicationModule.class, ActivityModule.class, UserModule.class})
public interface ApplicationComponent {
    void inject(AndroidApplication androidApplication);
    void inject(BaseActivity activity);
    void inject(UserListFragment userListFragment);
    void inject(UserDetailsFragment userDetailsFragment);
}
