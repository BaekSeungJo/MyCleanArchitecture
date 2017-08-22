package com.example.presentation.view.component;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by plnc on 2017-07-25.
 */

public class AutoLoadImageView extends AppCompatImageView {

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

    private void cacheBitmap(Bitmap bitmap, String fileName) {
        if(this.cache != null) {
            this.cache.put(bitmap, fileName);
        }
    }

    private boolean isThereInternetConnection() {
        boolean isConnected;

        ConnectivityManager connectivityManager =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = (networkInfo != null && networkInfo.isConnectedOrConnecting());

        return isConnected;
    }

    private String getFileNameFromUrl(String imageUrl) {
        String hash = String.valueOf(imageUrl.hashCode());
        if(hash.startsWith("-")) {
            hash = hash.substring(1);
        }
        return BASE_IMAGE_NAME_CACHED + hash;
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

        DiskCache(File cacheDir) {
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

        synchronized void put(Bitmap bitmap, String fileName) {
            File file = buildFileFromFilename(fileName);
            if(!file.exists()) {
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (FileNotFoundException e) {
                    Log.e(TAG, e.getMessage());
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }

        void evictAll() {
            if(cacheDir.exists()) {
                for(File file : cacheDir.listFiles()) {
                    file.delete();
                }
            }
        }

        private File buildFileFromFilename(String fileName) {
            String fullPath = this.cacheDir.getPath() + File.separator + fileName;
            return new File(fullPath);
        }
    }
}
