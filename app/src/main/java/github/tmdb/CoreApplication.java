package github.tmdb;

import android.app.Application;
import android.content.Context;

import github.tmdb.image.ImageLoader;
import github.tmdb.source.CachedDataSource;
import github.tmdb.source.HttpDataSource;
import github.tmdb.source.TMDBDataSource;
import github.tmdb.source.VkDataSource;

public class CoreApplication extends Application {

    private HttpDataSource httpDataSource;
    private VkDataSource vkDataSource;
    private TMDBDataSource tmdbDataSource;
    private ImageLoader imageLoader;
    private CachedDataSource cachedDataSource;

    @Override
    public void onCreate() {
        super.onCreate();
        httpDataSource = new HttpDataSource();
        vkDataSource = new VkDataSource();
        tmdbDataSource = new TMDBDataSource();
    }

    @Override
    public Object getSystemService(String name) {
        if (ImageLoader.KEY.equals(name)) {
            //for android kitkat +
            if (imageLoader == null) {
                imageLoader = new ImageLoader(this);
            }
            return imageLoader;
        }
        if (CachedDataSource.KEY.equals(name)) {
            //for android kitkat +
            if (cachedDataSource == null) {
                cachedDataSource = new CachedDataSource(this);
            }
            return cachedDataSource;
        }
        if (HttpDataSource.KEY.equals(name)) {
            //for android kitkat +
            if (httpDataSource == null) {
                httpDataSource = new HttpDataSource();
            }
            return httpDataSource;
        }
        if (VkDataSource.KEY.equals(name)) {
            //for android kitkat +
            if (vkDataSource == null) {
                vkDataSource = new VkDataSource();
            }
            return vkDataSource;
        }
        if (TMDBDataSource.KEY.equals(name)) {
            //for android kitkat +
            if (tmdbDataSource == null) {
                tmdbDataSource = new TMDBDataSource();
            }
            return tmdbDataSource;
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
