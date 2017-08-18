package com.example.presentation;

import android.os.Handler;
import android.os.Looper;

import com.example.domain.executor.PostExecutionThread;

/**
 * Created by plnc on 2017-06-28.
 */

public class UIThread implements PostExecutionThread {

    private static class LazyHolder {
        private static final UIThread INSTANCE = new UIThread();
    }

    public static UIThread getInstance() {
        return LazyHolder.INSTANCE;
    }

    private final Handler handler;

    public UIThread() {
        this.handler = new android.os.Handler(Looper.getMainLooper());
    }

    @Override
    public void post(Runnable runnable) {
        handler.post(runnable);
    }
}
