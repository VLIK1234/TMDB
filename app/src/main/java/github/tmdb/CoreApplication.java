package github.tmdb;

import android.app.Application;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import github.tmdb.image.ImageLoader;
import github.tmdb.source.CachedDataSource;
import github.tmdb.source.HttpDataSource;
import github.tmdb.source.TMDBDataSource;
import github.tmdb.source.VkDataSource;

public class CoreApplication extends Application {

    Map<String,Object> serviceMap = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        serviceMap.put(HttpDataSource.KEY, new HttpDataSource());
        serviceMap.put(VkDataSource.KEY, new VkDataSource(this));
        serviceMap.put(CachedDataSource.KEY, new CachedDataSource(this));
        serviceMap.put(TMDBDataSource.KEY, new TMDBDataSource());
        serviceMap.put(ImageLoader.KEY, new ImageLoader(this));
    }

    @Override
    public Object getSystemService(String name) {
        if (serviceMap.containsKey(name)) {
            return serviceMap.get(name);
        }
        return super.getSystemService(name);
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
