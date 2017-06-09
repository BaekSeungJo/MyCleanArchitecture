package com.example.domain.executor;

/**
 * Created by plnc on 2017-05-31.
 */

public interface PostExecutionThread {

    void post(Runnable runnable);
}
