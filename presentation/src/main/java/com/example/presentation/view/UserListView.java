package com.example.presentation.view;

import com.example.presentation.model.UserModel;

import java.util.Collection;

/**
 * Created by plnc on 2017-06-27.
 */

public interface UserListView extends LoadDataView {

    void renderUserList(Collection<UserModel> userModelCollection);

    void viewUser(UserModel userModel);
}
