package com.example.presentation;

import android.app.Application;

import com.example.presentation.internal.di.components.ApplicationComponent;
import com.example.presentation.internal.di.components.DaggerApplicationComponent;
import com.example.presentation.internal.di.modules.ApplicationModule;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by plnc on 2017-08-29.
 */

public class AndroidApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        this.initializeInjector();
        this.initializeLeakDetection();
    }

    private void initializeLeakDetection() {
//        if(BuildConfig.DEBUG) {
//            LeakCanary.install(this);
//        }
        if(LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }

    private void initializeInjector() {
        this.applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
