package com.example.presentation;

import dagger.Component;

/**
 * Created by plnc on 2017-08-29.
 */

@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    AndroidApplication inject(AndroidApplication androidApplication);
}
