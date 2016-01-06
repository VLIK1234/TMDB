package github.tmdb;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.HashMap;
import java.util.Map;

import github.tmdb.source.CachedDataSource;
import github.tmdb.source.HttpDataSource;
import github.tmdb.source.TMDBDataSource;
import github.tmdb.source.VkDataSource;

public class CoreApplication extends Application {

    private Map<String, Object> serviceMap = new HashMap<>();
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getBaseContext();
        // Create global configuration and initialize ImageLoader with this config
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCache(new WeakMemoryCache())
                .build();
        ImageLoader.getInstance().init(config);
        serviceMap.put(HttpDataSource.KEY, new HttpDataSource());
        serviceMap.put(VkDataSource.KEY, new VkDataSource(this));
        serviceMap.put(CachedDataSource.KEY, new CachedDataSource(this));
        serviceMap.put(TMDBDataSource.KEY, new TMDBDataSource());
    }

    @Override
    public Object getSystemService(String name) {
        if (serviceMap.containsKey(name)) {
            return serviceMap.get(name);
        }
        return super.getSystemService(name);
    }

    public static Context getAppContext() {
        return sContext;
    }

    public static <T> T get(Context context, String key) {
        if (context == null || key == null) {
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
