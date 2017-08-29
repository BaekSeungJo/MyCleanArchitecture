package com.example.data.cache.serializer;

import com.example.data.entity.UserEntity;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by plnc on 2017-06-07.
 */

@Singleton
public class JsonSerializer {

    private final Gson gson = new Gson();

    @Inject
    public JsonSerializer() {

    }

    public String serialize(UserEntity userEntity) {
        String jsonString = gson.toJson(userEntity);
        return jsonString;
    }

    public UserEntity deserialize(String jsonString) {
        UserEntity userEntity = gson.fromJson(jsonString, UserEntity.class);
        return userEntity;
    }
}
