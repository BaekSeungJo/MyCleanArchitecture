package com.example.data.entity.mapper;

import com.example.data.entity.UserEntity;
import com.example.domain.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Mapper class used to transform {@link UserEntity} in data layer to {@link User} in the
 * domain layer.
 */
@Singleton
public class UserEntityDataMapper {

    @Inject
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

    /**
     * Transform a List of {@link UserEntity} into a Collection of {@link User}.
     *
     * @param userEntityCollection Object Collection to be transformed.
     * @return {@link User} if valid {@link UserEntity} otherwise null.
     */
    public List<User> transform(Collection<UserEntity> userEntityCollection) {
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
