package com.example.vlik1234.tmdb.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.widget.ImageView;

import com.example.vlik1234.tmdb.CoreApplication;
import com.example.vlik1234.tmdb.helper.DataManager;
import com.example.vlik1234.tmdb.os.assist.LIFOLinkedBlockingDeque;
import com.example.vlik1234.tmdb.processing.BitmapProcessor;
import com.example.vlik1234.tmdb.processing.Processor;
import com.example.vlik1234.tmdb.source.CachedDataSource;
import com.example.vlik1234.tmdb.source.DataSource;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ImageLoader {

    public static final String KEY = "ImageLoader";
    int MAX_MEMORY = (int) (Runtime.getRuntime().maxMemory() / 1024);
    final int MAX_SIZE = MAX_MEMORY / 8;

    private AtomicBoolean isPause = new AtomicBoolean(false);

    public static ImageLoader get(Context context) {
        return CoreApplication.get(context, KEY);
    }

    private class BitmapLoader implements Runnable {

        private Handler mHandler;

        private DataManager.Callback<Bitmap> mCallback;
        private String mS;
        private DataSource<InputStream, String> mDataSource;
        private Processor<Bitmap, InputStream> mProcessor;

        public BitmapLoader(Handler handler, DataManager.Callback<Bitmap> callback, String s, DataSource<InputStream, String> DataSource, Processor<Bitmap, InputStream> Processor) {
            this.mHandler = handler;
            this.mCallback = callback;
            this.mS = s;
            this.mDataSource = DataSource;
            this.mProcessor = Processor;
        }

        @Override
        public void run() {
            try {
                InputStream result = mDataSource.getResult(mS);
                final Bitmap bitmap = mProcessor.process(result);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCallback.onDone(bitmap);
                    }
                });
            } catch (final Exception e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCallback.onError(e);
                    }
                });
            }
        }
    }

    private Context mContext;

    private DataSource<InputStream, String> mDataSource;

    private Processor<Bitmap, InputStream> mProcessor;

    private DataManager.Loader<Bitmap, InputStream, String> mLoader;

    private LruCache<String, Bitmap> mLruCache = new LruCache<String, Bitmap>(MAX_SIZE) {

        @Override
        protected int sizeOf(String key, Bitmap value) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
                return value.getRowBytes() * value.getHeight();
            } else {
                return value.getByteCount();
            }
        }
    };

    public ImageLoader(Context context, DataSource<InputStream, String> mDataSource, Processor<Bitmap, InputStream> mProcessor) {
        this.mContext = context;
        this.mDataSource = mDataSource;
        this.mProcessor = mProcessor;
        //TODO can be customizable
        this.mLoader = new DataManager.Loader<Bitmap, InputStream, String>() {

            final int COUNT_CORES = Runtime.getRuntime().availableProcessors();
            private ExecutorService executorService = new ThreadPoolExecutor(COUNT_CORES, COUNT_CORES, 0, TimeUnit.MILLISECONDS,
                    new LIFOLinkedBlockingDeque<Runnable>());

            @Override
            public void load(final DataManager.Callback<Bitmap> callback, final String s, final DataSource<InputStream, String> mDataSource, final Processor<Bitmap, InputStream> mProcessor) {
                callback.onDataLoadStart();
                final Looper looper = Looper.myLooper();
                final Handler handler = new Handler(looper);
                executorService.execute(new BitmapLoader(handler, callback, s, mDataSource, mProcessor));
            }

        };
    }

    public ImageLoader(Context context) {
        this(context, CachedDataSource.get(context), new BitmapProcessor());
    }

    public void pause() {
        isPause.set(true);
    }

    private final Object mDelayedLock = new Object();

    public void resume() {
        isPause.set(false);
        synchronized (mDelayedLock) {
            for (ImageView imageView : delayedImagesViews) {
                Object tag = imageView.getTag();
                if (tag != null) {
                    loadAndDisplay((String) tag, imageView);
                }
            }
            delayedImagesViews.clear();
        }
    }

    private Set<ImageView> delayedImagesViews = new HashSet<ImageView>();

    public void loadAndDisplay(final String url, final ImageView imageView) {
        Bitmap bitmap = mLruCache.get(url);
        imageView.setImageBitmap(bitmap);
        imageView.setTag(url);
        if (bitmap != null) {
            return;
        }
        if (isPause.get()) {
            synchronized (mDelayedLock) {
                delayedImagesViews.add(imageView);
            }
            return;
        }
        if (!TextUtils.isEmpty(url)) {

            DataManager.loadData(new DataManager.Callback<Bitmap>() {
                @Override
                public void onDataLoadStart() {

                }

                @Override
                public void onDone(Bitmap bitmap) {
                    if (bitmap != null) {
                        mLruCache.put(url, bitmap);
                    }
                    if (url.equals(imageView.getTag())) {
                        imageView.setImageBitmap(bitmap);
                    }
                }

                @Override
                public void onError(Exception e) {

                }

            }, url, mDataSource, mProcessor, mLoader);
        }
    }
}
