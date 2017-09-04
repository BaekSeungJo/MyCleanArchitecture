package com.example.data.entity.mapper;

import com.example.data.entity.UserEntity;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

/**
 * Class used to transform from Strings representing json to valid objects.
 */

public class UserEntityJsonMapper {

    private final Gson gson;

    @Inject
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

    public List<UserEntity> transformUserEntityCollection(String userListJsonResponse)
        throws JsonSyntaxException {
        List<UserEntity> userEntityCollection;

        try {
            Type listOfUserEntityType = new TypeToken<List<UserEntity>>(){}.getType();
            userEntityCollection = this.gson.fromJson(userListJsonResponse, listOfUserEntityType);

            return userEntityCollection;
        } catch (JsonSyntaxException jsonException) {
            throw jsonException;
        }
    }
}
