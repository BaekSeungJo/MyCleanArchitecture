package com.example.presentation;

import android.app.Application;

import dagger.Module;
import dagger.Provides;

/**
 * Created by plnc on 2017-08-29.
 */

@Module
final class ApplicationModule {

    private final Application application;

    ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides Application provideApplication() {
        return this.application;
    }
}
