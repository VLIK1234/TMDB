package github.tmdb.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.widget.ImageView;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import github.tmdb.CoreApplication;
import github.tmdb.helper.DataManager;
import github.tmdb.os.assist.LIFOLinkedBlockingDeque;
import github.tmdb.processing.BitmapProcessor;
import github.tmdb.processing.Processor;
import github.tmdb.source.CachedDataSource;
import github.tmdb.source.DataSource;

public class ImageLoaderIstin {

    public static final String KEY = "ImageLoaderIstin";
    int MAX_MEMORY = (int) (Runtime.getRuntime().maxMemory() / 1024);
    final int MAX_SIZE = MAX_MEMORY / 8;

    private AtomicBoolean isPause = new AtomicBoolean(false);

    public static ImageLoaderIstin get(Context context) {
        return CoreApplication.get(context, KEY);
    }

    private class BitmapLoader implements Runnable {

        private Handler handler;
        private DataManager.Callback<Bitmap> callback;
        private String string;
        private DataSource<InputStream, String> dataSource;
        private Processor<Bitmap, InputStream> processor;

        public BitmapLoader(Handler handler, DataManager.Callback<Bitmap> callback, String s, DataSource<InputStream, String> DataSource, Processor<Bitmap, InputStream> Processor) {
            this.handler = handler;
            this.callback = callback;
            this.string = s;
            this.dataSource = DataSource;
            this.processor = Processor;
        }

        @Override
        public void run() {
            try {
                InputStream result = dataSource.getResult(string);
                final Bitmap bitmap = processor.process(result);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onDone(bitmap);
                    }
                });
            } catch (final Exception e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onError(e);
                    }
                });
            }
        }
    }

    private Context context;
    private DataSource<InputStream, String> dataSource;
    private Processor<Bitmap, InputStream> processor;
    private DataManager.Loader<Bitmap, InputStream, String> loader;

    private LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(MAX_SIZE) {

        @Override
        protected int sizeOf(String key, Bitmap value) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
                return value.getRowBytes() * value.getHeight();
            } else {
                return value.getByteCount();
            }
        }
    };

    public ImageLoaderIstin(Context context, DataSource<InputStream, String> mDataSource, Processor<Bitmap, InputStream> mProcessor) {
        this.context = context;
        this.dataSource = mDataSource;
        this.processor = mProcessor;

        this.loader = new DataManager.Loader<Bitmap, InputStream, String>() {

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

    public ImageLoaderIstin(Context context) {
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
        Bitmap bitmap = lruCache.get(url);
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
                        lruCache.put(url, bitmap);
                    }
                    if (url.equals(imageView.getTag())) {
                        imageView.setImageBitmap(bitmap);
                    }
                }

                @Override
                public void onError(Exception e) {

                }

            }, url, dataSource, processor, loader);
        }
    }
}
