package com.example.data.entity.mapper;

import com.example.data.entity.UserEntity;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;

/**
 * Created by plnc on 2017-05-31.
 */

public class UserEntityJsonMapper {

    private final Gson gson;

    public UserEntityJsonMapper() {
        gson = new Gson();
    }

    public UserEntity transformUserEntity(String userJsonResponse) throws JsonSyntaxException {
        try {
            Type userEntityType = new TypeToken<UserEntity>(){}.getType();
            UserEntity userEntity = gson.fromJson(userJsonResponse, userEntityType);

            return userEntity;
        } catch (JsonSyntaxException jsonException) {
            throw jsonException;
        }
    }

    public Collection<UserEntity> transformUserEntityCollection(String userListJsonResponse)
        throws JsonSyntaxException {
        Collection<UserEntity> userEntityCollection;

        try {
            Type listOfUserEntityType = new TypeToken<Collection<UserEntity>>(){}.getType();
            userEntityCollection = gson.fromJson(userListJsonResponse, listOfUserEntityType);

            return userEntityCollection;
        } catch (JsonSyntaxException jsonException) {
            throw jsonException;
        }
    }
}
