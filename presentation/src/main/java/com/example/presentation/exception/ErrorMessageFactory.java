package com.example.presentation.exception;

import android.content.Context;

import com.example.data.exception.NetworkConnectionException;
import com.example.data.exception.UserNotFoundException;
import com.example.presentation.R;

/**
 * Created by plnc on 2017-06-26.
 */

public class ErrorMessageFactory {

    private ErrorMessageFactory() {
        // empty
    }

    public static String create(Context context, Exception exception) {
        String message = context.getString(R.string.exception_message_generic);

        if(exception instanceof NetworkConnectionException) {
            message = context.getString(R.string.exception_message_no_connection);
        } else if(exception instanceof UserNotFoundException) {
            message = context.getString(R.string.exception_message_user_not_found);
        }

        return message;
    }
}
