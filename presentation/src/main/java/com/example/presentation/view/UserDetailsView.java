package com.example.presentation.view;

import com.example.presentation.model.UserModel;

/**
 * Created by plnc on 2017-06-28.
 */

public interface UserDetailsView extends LoadDataView {

    void renderUser(UserModel userModel);
}
