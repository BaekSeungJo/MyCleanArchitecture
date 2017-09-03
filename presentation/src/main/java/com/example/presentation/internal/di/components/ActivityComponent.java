package com.example.presentation.internal.di.components;

import android.app.Activity;

import com.example.presentation.internal.di.PerActivity;
import com.example.presentation.internal.di.modules.ActivityModule;

import dagger.Component;

/**
 * Created by 1 on 2017-09-04.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    // Exposed to sub-graphs.
    Activity activity();
}

