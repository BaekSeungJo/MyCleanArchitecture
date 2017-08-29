package com.example.presentation.internal.di.modules;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 1 on 2017-08-29.
 */

@Module
public final class ApplicationModule {

    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides Context provideApplication() {
        return this.application;
    }

    @Provides
    LayoutInflater provideLayoutInflater() {
        return LayoutInflater.from(application);
    }

}
