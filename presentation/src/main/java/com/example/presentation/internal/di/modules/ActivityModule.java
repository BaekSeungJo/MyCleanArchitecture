package com.example.presentation.internal.di.modules;

import com.example.presentation.navigation.Navigator;

import dagger.Module;
import dagger.Provides;

/**
 * Created by plnc on 2017-08-29.
 */

@Module
public class ActivityModule {
    @Provides
    Navigator provideNavigator() {
        return new Navigator();
    }
}
