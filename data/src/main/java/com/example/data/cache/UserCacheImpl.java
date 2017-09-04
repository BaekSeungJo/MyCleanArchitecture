package com.example.data.cache;

import android.content.Context;

import com.example.data.cache.serializer.JsonSerializer;
import com.example.data.entity.UserEntity;
import com.example.data.exception.UserNotFoundException;
import com.example.domain.executor.ThreadExecutor;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * {@link UserCache} implementation.
 */
@Singleton
public class UserCacheImpl implements UserCache {

    private static final String SETTINGS_FILE_NAME = "com.fernandocejas.android10.SETTINGS";
    private static final String SETTINGS_LAST_CACHE_UPDATE = "last_cache_update";

    private static final String DEFAULT_FILE_NAME = "user_";
    private static final long EXPIRATION_TIME = 60 * 10 * 1000;

    private final Context context;
    private final File cacheDir;
    private final JsonSerializer serializer;
    private final FileManager fileManager;
    private final ThreadExecutor threadExecutor;

    /**
     * Constructor of the class {@link UserCacheImpl}
     *
     * @param context A
     * @param userCacheSerializer {@link JsonSerializer} for object serialization.
     * @param fileManager {@link FileManager} for saving serialized objects to the file system.
     * @param executor
     */
    @Inject
    public UserCacheImpl(Context context, JsonSerializer userCacheSerializer,
                          FileManager fileManager, ThreadExecutor executor) {
        if(context == null || userCacheSerializer == null || fileManager == null || executor == null) {
            throw new IllegalArgumentException("Invalid null parametaer");
        }
        this.context = context;
        this.cacheDir = this.context.getCacheDir();
        this.serializer = userCacheSerializer;
        this.fileManager = fileManager;
        this.threadExecutor = executor;
    }

    @Override
    public synchronized void get(int userId, UserCacheCallback callback) {
        File userEntityFile = buildFile(userId);
        String fileContent = fileManager.readFileContent(userEntityFile);
        UserEntity userEntity = serializer.deserialize(fileContent);

        if(userEntity != null) {
            callback.onUserEntityLoaded(userEntity);
        } else {
            callback.onError(new UserNotFoundException());
        }
    }

    @Override
    public void put(UserEntity userEntity) {
        if(userEntity != null) {
            File userEntityFile = buildFile(userEntity.getUserId());
            if(!isCached(userEntity.getUserId())) {
                String jsonString = serializer.serialize(userEntity);
                executeAsynchronously(new CacheWriter(this.fileManager, userEntityFile, jsonString));
                setLastCacheUpdateTimeMillis();
            }
        }
    }

    @Override
    public boolean isCached(int userId) {
        File userEntityFile = buildFile(userId);
        return fileManager.exists(userEntityFile);
    }

    @Override
    public boolean isExpired() {
        long currentTime = System.currentTimeMillis();
        long lastUpdateTime = getLastCacheUpdateTimeMillis();

        boolean expired = ((currentTime - lastUpdateTime) > EXPIRATION_TIME);

        if(expired) {
            evictAll();
        }

        return expired;
    }

    @Override
    public void evictAll() {
        executeAsynchronously(new CacheEvictor(fileManager, cacheDir));
    }

    private File buildFile(int userId) {
        StringBuilder fileNameBuilder = new StringBuilder();
        fileNameBuilder.append(this.cacheDir.getPath());
        fileNameBuilder.append(File.separator);
        fileNameBuilder.append(DEFAULT_FILE_NAME);
        fileNameBuilder.append(userId);

        return new File(fileNameBuilder.toString());
    }

    private void executeAsynchronously(Runnable runnable) {
        threadExecutor.execute(runnable);
    }

    private void setLastCacheUpdateTimeMillis() {
        long currentMillis = System.currentTimeMillis();
        fileManager.writeToPreference(context, SETTINGS_FILE_NAME, SETTINGS_LAST_CACHE_UPDATE, currentMillis);
    }

    private long getLastCacheUpdateTimeMillis() {
        return fileManager.getFromPreference(context, SETTINGS_FILE_NAME, SETTINGS_LAST_CACHE_UPDATE);
    }

    private static class CacheWriter implements Runnable {
        private final FileManager fileManager;
        private final File fileToWrite;
        private final String fileContent;

        public CacheWriter(FileManager fileManager, File fileToWrite, String fileContent) {
            this.fileManager = fileManager;
            this.fileToWrite = fileToWrite;
            this.fileContent = fileContent;
        }

        @Override
        public void run() {
            this.fileManager.writeToFile(fileToWrite, fileContent);
        }
    }

    private static class CacheEvictor implements Runnable {
        private final FileManager fileManager;
        private final File cacheDir;

        public CacheEvictor(FileManager fileManager, File cacheDir) {
            this.fileManager = fileManager;
            this.cacheDir = cacheDir;
        }

        @Override
        public void run() {
            this.fileManager.cleanDirectory(cacheDir);
        }
    }
}
