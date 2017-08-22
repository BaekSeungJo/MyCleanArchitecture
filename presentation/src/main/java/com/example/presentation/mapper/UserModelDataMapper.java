package com.example.presentation.mapper;

import com.example.domain.User;
import com.example.presentation.model.UserModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by plnc on 2017-06-26.
 */

public class UserModelDataMapper {

    public UserModelDataMapper() {
        // empty
    }

    public UserModel transform(User user) {
        if(user == null) {
            throw new IllegalArgumentException("Cannot transform a null value");
        }
        UserModel userModel = new UserModel(user.getUserId());
        userModel.setCoverUrl(user.getCoverUrl());
        userModel.setFullName(user.getFullName());
        userModel.setEmail(user.getEmail());
        userModel.setDescription(user.getDescription());
        userModel.setFollowers(user.getFollowers());

        return userModel;
    }

    public Collection<UserModel> transform(Collection<User> userCollection) {
        Collection<UserModel> userModelCollection;

        if (userCollection != null && !userCollection.isEmpty()) {
            userModelCollection = new ArrayList<>();
            for (User user : userCollection) {
                userModelCollection.add(transform(user));
            }
        } else {
            userModelCollection = Collections.emptyList();
        }

        return userModelCollection;
    }
}
