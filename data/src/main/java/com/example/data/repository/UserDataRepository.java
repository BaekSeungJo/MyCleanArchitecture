package com.example.data.repository;

import com.example.domain.repository.UserRepository;

/**
 * Created by plnc on 2017-06-08.
 */

public class UserDataRepository implements UserRepository {

    private static UserDataRepository INSTANCE;


    @Override
    public void getUserList(UserListCallback userListCallback) {

    }

    @Override
    public void getUserDetail(int userId, UserDetailsCallback userDetailCallback) {

    }
}
