package com.example.data.cache.serializer;

import com.example.data.entity.UserEntity;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Json Serializer/Deserializer.
 */

@Singleton
public class Serializer {

    private final Gson gson = new Gson();

    @Inject
    Serializer() {

    }

    /**
     * Serialize an object to Json
     *
     * @param object to serialize.
     * @param clazz
     * @return
     */
    public String serialize(Object object, Class clazz) {
        return gson.toJson(object, clazz);
    }

    public<T> T deserialize(String jsonString, Class<T> clazz) {
        return gson.fromJson(jsonString, clazz);
    }
}
