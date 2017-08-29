package com.example.presentation.navigation;

import android.content.Context;
import android.content.Intent;

import com.example.presentation.view.activity.UserDetailsActivity;
import com.example.presentation.view.activity.UserListActivity;

import javax.inject.Inject;

/**
 * Created by plnc on 2017-06-26.
 */

public class Navigator {

    @Inject
    public Navigator() {
        // empty
    }

    public void navigateToUserList(Context context) {
        if(context != null) {
            Intent intentToLaunch = UserListActivity.getCallingIntent(context);
            context.startActivity(intentToLaunch);
        }
    }

    public void navigateToUserDetails(Context context, int userId) {
        if(context != null) {
            Intent intentToLaunch = UserDetailsActivity.getCallingIntent(context, userId);
            context.startActivity(intentToLaunch);
        }
    }
}
