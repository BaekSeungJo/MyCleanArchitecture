package com.example.data.entity.mapper;

import com.example.data.entity.UserEntity;
import com.example.domain.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by plnc on 2017-05-31.
 */

public class UserEntityDataMapper {

    public UserEntityDataMapper() {
    }

    public User transform(UserEntity userEntity) {
        User user = null;
        if(userEntity != null) {
            user = new User(userEntity.getUserId());
            user.setCoverUrl(userEntity.getCoverUrl());
            user.setDescription(userEntity.getDescription());
            user.setEmail(userEntity.getEmail());
            user.setFollowers(userEntity.getFollowers());
            user.setFullName(userEntity.getFullname());
        }

        return user;
    }

    public Collection<User> transform(Collection<UserEntity> userEntityCollection) {
        List<User> userList = new ArrayList<>();
        User user;
        for(UserEntity userEntity : userEntityCollection) {
            user = transform(userEntity);
            if(user != null) {
                userList.add(user);
            }
        }

        return userList;
    }
}
