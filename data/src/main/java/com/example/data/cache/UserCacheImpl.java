package com.example.data.cache;

import android.content.Context;

import com.example.data.cache.serializer.Serializer;
import com.example.data.entity.UserEntity;
import com.example.data.exception.UserNotFoundException;
import com.example.domain.executor.ThreadExecutor;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

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
    private final Serializer serializer;
    private final FileManager fileManager;
    private final ThreadExecutor threadExecutor;

    /**
     * Constructor of the class {@link UserCacheImpl}
     *
     * @param context A
     * @param serializer {@link Serializer} for object serialization.
     * @param fileManager {@link FileManager} for saving serialized objects to the file system.
     * @param executor
     */
    @Inject
    UserCacheImpl(Context context, Serializer serializer,
                          FileManager fileManager, ThreadExecutor executor) {
        if(context == null || serializer == null || fileManager == null || executor == null) {
            throw new IllegalArgumentException("Invalid null parametaer");
        }
        this.context = context;
        this.cacheDir = this.context.getCacheDir();
        this.serializer = serializer;
        this.fileManager = fileManager;
        this.threadExecutor = executor;
    }

    /**
     * Get an {@link Observable} which will emit a {@link UserEntity}.
     *
     * @param userId The user id  to retrive data
     */
    @Override
    public Observable<UserEntity> get(final int userId) {
        return Observable.create(emitter -> {
            final File userEntityFile = buildFile(userId);
            final String fileContent = UserCacheImpl.this.fileManager.readFileContent(userEntityFile);
            final UserEntity userEntity =
                    UserCacheImpl.this.serializer.deserialize(fileContent, UserEntity.class);

            if(userEntity != null) {
                emitter.onNext(userEntity);
                emitter.onComplete();
            } else {
                emitter.onError(new UserNotFoundException());
            }
        });
    }

    @Override
    public void put(UserEntity userEntity) {
        if(userEntity != null) {
            final File userEntityFile = buildFile(userEntity.getUserId());
            if(!isCached(userEntity.getUserId())) {
                String jsonString = serializer.serialize(userEntity, UserEntity.class);
                executeAsynchronously(new CacheWriter(this.fileManager, userEntityFile, jsonString));
                setLastCacheUpdateTimeMillis();
            }
        }
    }

    @Override
    public boolean isCached(int userId) {
        final File userEntityFile = buildFile(userId);
        return this.fileManager.exists(userEntityFile);
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
        final StringBuilder fileNameBuilder = new StringBuilder();
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
        final long currentMillis = System.currentTimeMillis();
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
