package com.example.vlik1234.tmdb;

import android.app.Application;
import android.content.Context;

import com.example.vlik1234.tmdb.image.ImageLoader;
import com.example.vlik1234.tmdb.source.CachedDataSource;
import com.example.vlik1234.tmdb.source.HttpDataSource;
import com.example.vlik1234.tmdb.source.TMDBDataSource;
import com.example.vlik1234.tmdb.source.VkDataSource;

public class CoreApplication extends Application {

    private HttpDataSource mHttpDataSource;
    private VkDataSource mVkDataSource;
    private TMDBDataSource mTMDBDataSource;
    private ImageLoader mImageLoader;
    private CachedDataSource mCachedDataSource;

    @Override
    public void onCreate() {
        super.onCreate();
        mHttpDataSource = new HttpDataSource();
        mVkDataSource = new VkDataSource();
        mTMDBDataSource = new TMDBDataSource();
    }

    @Override
    public Object getSystemService(String name) {
        if (ImageLoader.KEY.equals(name)) {
            //for android kitkat +
            if (mImageLoader == null) {
                mImageLoader = new ImageLoader(this);
            }
            return mImageLoader;
        }
        if (CachedDataSource.KEY.equals(name)) {
            //for android kitkat +
            if (mCachedDataSource == null) {
                mCachedDataSource = new CachedDataSource(this);
            }
            return mCachedDataSource;
        }
        if (HttpDataSource.KEY.equals(name)) {
            //for android kitkat +
            if (mHttpDataSource == null) {
                mHttpDataSource = new HttpDataSource();
            }
            return mHttpDataSource;
        }
        if (VkDataSource.KEY.equals(name)) {
            //for android kitkat +
            if (mVkDataSource == null) {
                mVkDataSource = new VkDataSource();
            }
            return mVkDataSource;
        }
        if (TMDBDataSource.KEY.equals(name)) {
            //for android kitkat +
            if (mTMDBDataSource == null) {
                mTMDBDataSource = new  TMDBDataSource();
            }
            return  mTMDBDataSource;
        }
        return super.getSystemService(name);
    }

    public static <T> T get(Context context, String key) {
        if (context == null || key == null){
            throw new IllegalArgumentException("Context and key must not be null");
        }
        T systemService = (T) context.getSystemService(key);
        if (systemService == null) {
            context = context.getApplicationContext();
            systemService = (T) context.getSystemService(key);
        }
        if (systemService == null) {
            throw new IllegalStateException(key + " not available");
        }
        return systemService;
    }
}
