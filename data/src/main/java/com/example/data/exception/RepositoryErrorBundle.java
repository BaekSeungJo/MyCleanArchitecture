package com.example.data.exception;

import com.example.domain.exeception.ErrorBundle;

/**
 * Created by plnc on 2017-06-09.
 */

class RepositoryErrorBundle implements ErrorBundle {

    private final Exception exception;

    RepositoryErrorBundle(Exception exception) {
        this.exception = exception;
    }

    @Override
    public Exception getException() {
        return null;
    }

    @Override
    public String getErrorMessage() {
        String message = "";
        if (this.exception != null) {
            message = this.exception.getMessage();
        }

        return message;
    }
}
