package com.example.domain.exeception;

/**
 * Created by plnc on 2017-05-31.
 */

public interface ErrorBundle {
    Exception getException();

    String getErrorMessage();
}
