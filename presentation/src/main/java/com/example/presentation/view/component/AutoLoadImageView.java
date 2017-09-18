package com.example.presentation.view.component;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcel;
import android.os.Parcelable;
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

    private String imageUrl = null;
    private int imagePlaceHolderResId = -1;
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

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.imagePlaceHolderResId = this.imagePlaceHolderResId;
        savedState.imageUrl = this.imageUrl;
        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.imagePlaceHolderResId = savedState.imagePlaceHolderResId;
        this.imageUrl = savedState.imageUrl;
        this.setImageUrl(this.imageUrl);
    }

    public void setImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
        AutoLoadImageView.this.loadImagePlaceHolder();
        if(this.imageUrl != null) {
            this.loadImageFromUrl(this.imageUrl);
        } else {
            this.loadImagePlaceHolder();
        }
    }

    public void setImagePlaceHolder(int resourceId) {
        this.imagePlaceHolderResId = resourceId;
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
        if(this.imagePlaceHolderResId != -1) {
            ((Activity) getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AutoLoadImageView.this.setImageResource(AutoLoadImageView.this.imagePlaceHolderResId);
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

    private static class SavedState extends BaseSavedState {
        int imagePlaceHolderResId;
        String imageUrl;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.imagePlaceHolderResId = in.readInt();
            this.imageUrl = in.readString();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.imagePlaceHolderResId);
            out.writeString(this.imageUrl);
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {

                    @Override
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    @Override
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };

    }
}
