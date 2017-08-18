package com.example.presentation.view.component;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by plnc on 2017-07-25.
 */

public class AutoLoadImageView extends ImageView {

    private static final String BASE_IMAGE_NAME_CACHED = "image_";

    private int imagePlaceHolderResourceId = -1;
    private DiskCache cache = new DiskCache(getContext().getCacheDir());


    public AutoLoadImageView(Context context) {
        super(context);
    }

    public AutoLoadImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoLoadImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setImageUrl(final String imageUrl) {
        AutoLoadImageView.this.loadImagePlaceHolder();
        if(imageUrl != null) {
            this.loadImageFromUrl(imageUrl);
        } else {
            this.loadImagePlaceHolder();
        }
    }

    public void setImagePlaceHolder(int resourceId) {
        this.imagePlaceHolderResourceId = resourceId;
        this.loadImagePlaceHolder();
    }

    public void invalidateImageCache() {
        if(this.cache != null) {
            this.cache.evictAll();
        }
    }

    private void loadImageFromUrl(final String imageUrl) {
        new Thread() {
            @Override
            public void run() {
                final Bitmap bitmap = AutoLoadImageView.this.getFromCache(getFileNameFromUrl(imageUrl));
                if(bitmap != null) {
                    AutoLoadImageView.this.loadBitmap(bitmap);
                } else {
                    if(isThereInternetConnection()) {
                        final ImageDownloader imageDownloader = new ImageDownloader();
                        imageDownloader.download(imageUrl, new ImageDownloader.Callback() {

                            @Override
                            public void onImageDownloaded(Bitmap bitmap) {
                                AutoLoadImageView.this.cacheBitmap(bitmap, getFileNameFromUrl(imageUrl));
                                AutoLoadImageView.this.loadBitmap(bitmap);
                            }

                            @Override
                            public void onError() {
                                AutoLoadImageView.this.loadImagePlaceHolder();
                            }
                        });
                    }
                }
            }
        }.start();
    }

    private void loadBitmap(final Bitmap bitmap) {
        ((Activity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AutoLoadImageView.this.setImageBitmap(bitmap);
            }
        });
    }

    private void loadImagePlaceHolder() {
        if(this.imagePlaceHolderResourceId != -1) {
            ((Activity) getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AutoLoadImageView.this.setImageResource(AutoLoadImageView.this.imagePlaceHolderResourceId);
                }
            });
        }
    }

    private Bitmap getFromCache(String fileName) {
        Bitmap bitmap = null;
        if(this.cache != null) {
            bitmap = this.cache.get(fileName);
        }
        return bitmap;
    }

    private static class ImageDownloader {
        interface Callback {
            void onImageDownloaded(Bitmap bitmap);

            void onError();
        }

        ImageDownloader() {}

        void download(String imageUrl, Callback callback) {
            try {
                URLConnection conn = new URL(imageUrl).openConnection();
                conn.connect();
                Bitmap bitmap = BitmapFactory.decodeStream(conn.getInputStream());
                if(callback != null) {
                    callback.onImageDownloaded(bitmap);
                }
            } catch (MalformedURLException e) {
                reportError(callback);
            } catch (IOException e) {
                reportError(callback);
            }
        }

        void reportError(Callback callback) {
            if(callback != null) {
                callback.onError();
            }
        }
    }


    private static class DiskCache {

        private static final String TAG = "DiskCache";

        private final File cacheDir;

        public DiskCache(File cacheDir) {
            this.cacheDir = cacheDir;
        }

        synchronized Bitmap get(String fileName) {
            Bitmap bitmap = null;
            File file = buildFileFromFilename(fileName);
            if(file.exists()) {
                bitmap = BitmapFactory.decodeFile(file.getPath());
            }
            return bitmap;
        }

        public void evictAll() {

        }
    }

    private File buildFileFromFilename(String fileName) {
        String fullPath = this.cacheDir
    }
}
