package com.example.data.exception;

/**
 * Created by plnc on 2017-06-09.
 */

public class NetworkConnectionException extends Exception {

    public NetworkConnectionException() {
    }

    public NetworkConnectionException(String message) {
        super(message);
    }

    public NetworkConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public NetworkConnectionException(Throwable cause) {
        super(cause);
    }
}
