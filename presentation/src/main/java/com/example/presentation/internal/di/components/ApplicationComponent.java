package com.example.presentation.internal.di.components;

import android.content.Context;

import com.example.data.cache.UserCache;
import com.example.domain.executor.PostExecutionThread;
import com.example.domain.executor.ThreadExecutor;
import com.example.domain.repository.UserRepository;
import com.example.presentation.AndroidApplication;
import com.example.presentation.internal.di.modules.ApplicationModule;
import com.example.presentation.view.activity.BaseActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by plnc on 2017-08-29.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(AndroidApplication androidApplication);
    void inject(BaseActivity baseActivity);

    // Exposed to sub-graphs.
    Context context();
    ThreadExecutor threadExecutor();
    PostExecutionThread postExecutionThread();
    UserCache userCache();
    UserRepository userRepository();
}
