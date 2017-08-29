package com.example.presentation;

import com.example.presentation.view.activity.BaseActivity;
import dagger.Component;

/**
 * Created by plnc on 2017-08-29.
 */

@Component(modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(BaseActivity activity);
}
