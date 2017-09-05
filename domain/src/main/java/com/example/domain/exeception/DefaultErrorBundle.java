package com.example.domain.exeception;

/**
 * Created by 1 on 2017-09-06.
 */

public class DefaultErrorBundle implements ErrorBundle {

    private final Exception exception;

    public DefaultErrorBundle(Exception exception) {
        this.exception = exception;
    }

    @Override
    public Exception getException() {
        return exception;
    }

    @Override
    public String getErrorMessage() {
        String message = "";
        if(this.exception != null) {
            message = this.exception.getMessage();
        }
        return message;
    }
}
