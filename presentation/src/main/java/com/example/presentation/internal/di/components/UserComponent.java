package com.example.presentation.internal.di.components;

import com.example.presentation.internal.di.PerActivity;
import com.example.presentation.internal.di.modules.ActivityModule;
import com.example.presentation.internal.di.modules.UserModule;
import com.example.presentation.view.fragment.UserDetailsFragment;
import com.example.presentation.view.fragment.UserListFragment;

import dagger.Component;

/**
 * Created by 1 on 2017-09-04.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, UserModule.class})
public interface UserComponent {
    void inject(UserListFragment userListFragment);
    void inject(UserDetailsFragment userDetailsFragment);
}
