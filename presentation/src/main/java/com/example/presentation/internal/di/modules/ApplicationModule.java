package com.example.presentation.internal.di.modules;

import android.content.Context;
import android.view.LayoutInflater;

import com.example.data.cache.UserCache;
import com.example.data.cache.UserCacheImpl;
import com.example.data.executor.JobExecutor;
import com.example.data.repository.UserDataRepository;
import com.example.domain.executor.PostExecutionThread;
import com.example.domain.executor.ThreadExecutor;
import com.example.domain.repository.UserRepository;
import com.example.presentation.AndroidApplication;
import com.example.presentation.UIThread;
import com.example.presentation.navigation.Navigator;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 1 on 2017-08-29.
 */

@Module
public class ApplicationModule {

    private final AndroidApplication application;

    public ApplicationModule(AndroidApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return this.application;
    }

    @Provides
    @Singleton
    Navigator provideNavigator() {
        return new Navigator();
    }

    @Provides
    @Singleton
    ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
        return jobExecutor;
    }

    @Provides
    @Singleton
    PostExecutionThread providePostExecutionThread(UIThread uiThread) {
        return uiThread;
    }

    @Provides
    @Singleton
    UserCache provideUserCache(UserCacheImpl userCache) {
        return userCache;
    }

    @Provides
    @Singleton
    UserRepository provideUserRepository(UserDataRepository userDataRepository) {
        return userDataRepository;
    }
}
