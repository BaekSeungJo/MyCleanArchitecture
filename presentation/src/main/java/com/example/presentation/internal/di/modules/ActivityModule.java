package com.example.presentation.internal.di.modules;

import android.app.Activity;

import com.example.presentation.internal.di.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by plnc on 2017-08-29.
 */

@Module
public class ActivityModule {
    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @PerActivity
    Activity activity() {
        return this.activity;
    }
}
