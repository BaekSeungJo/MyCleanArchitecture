package com.example.presentation;

import android.os.Handler;
import android.os.Looper;

import com.example.domain.executor.PostExecutionThread;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by plnc on 2017-06-28.
 */
@Singleton
public class UIThread implements PostExecutionThread {

    private final Handler handler;

    @Inject
    public UIThread() {
        this.handler = new android.os.Handler(Looper.getMainLooper());
    }

    @Override
    public void post(Runnable runnable) {
        handler.post(runnable);
    }
}
