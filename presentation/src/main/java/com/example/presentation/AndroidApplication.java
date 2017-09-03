package com.example.presentation;

import android.app.Application;

import com.example.presentation.internal.di.components.ApplicationComponent;
import com.example.presentation.internal.di.components.DaggerApplicationComponent;
import com.example.presentation.internal.di.modules.ApplicationModule;
import com.example.presentation.view.activity.BaseActivity;
import com.example.presentation.view.fragment.UserDetailsFragment;
import com.example.presentation.view.fragment.UserListFragment;

/**
 * Created by plnc on 2017-08-29.
 */

public class AndroidApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        this.initializeInjector();
    }

    private void initializeInjector() {
        this.applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        this.applicationComponent.inject(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    public void inject(BaseActivity baseActivity) {
        this.applicationComponent.inject(baseActivity);
    }
}
