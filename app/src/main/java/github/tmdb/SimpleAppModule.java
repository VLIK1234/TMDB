package github.tmdb;

import android.content.Context;
import android.net.Uri;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.io.IOException;

import by.istin.android.xcore.XCoreHelper;
import by.istin.android.xcore.error.ErrorHandler;
import by.istin.android.xcore.provider.IDBContentProviderSupport;
import by.istin.android.xcore.source.DataSourceRequest;
import by.istin.android.xcore.source.impl.http.HttpDataSource;
import by.istin.android.xcore.source.impl.http.HttpRequest;
import by.istin.android.xcore.utils.Holder;
import github.tmdb.core.model.Cast;
import github.tmdb.core.model.Content;
import github.tmdb.core.model.Crew;
import github.tmdb.core.model.Genre;
import github.tmdb.core.model.MovieDetailEntity;
import github.tmdb.core.model.MovieItemEntity;
import github.tmdb.core.model.ProductionCompany;
import github.tmdb.core.model.ProductionCountry;
import github.tmdb.core.model.SampleEntity;
import github.tmdb.core.model.SpokenLanguage;
import github.tmdb.core.model.Video;
import github.tmdb.core.processor.CastProcessor;
import github.tmdb.core.processor.ContentEntityProcessor;
import github.tmdb.core.processor.MovieDetailProcessor;
import github.tmdb.core.processor.MovieEntityProcessor;
import github.tmdb.core.processor.SampleEntityProcessor;

public class SimpleAppModule extends XCoreHelper.BaseModule {

    private static final Class<?>[] ENTITIES = new Class<?>[]{
            SampleEntity.class,
            Content.class,
            MovieItemEntity.class,
            MovieDetailEntity.class,
            Cast.class,
            Crew.class,
            Genre.class,
            ProductionCompany.class,
            ProductionCountry.class,
            SpokenLanguage.class,
            Video.class
    };

    public static DisplayImageOptions BITMAP_DISPLAYER_OPTIONS = new DisplayImageOptions.Builder()
            .resetViewBeforeLoading(true)
            .delayBeforeLoading(300)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .displayer(new FadeInBitmapDisplayer(100))
            .build();

    @Override
    protected void onCreate(Context context) {
        IDBContentProviderSupport dbContentProviderSupport = registerContentProvider(ENTITIES);
        registerAppService(new SampleEntityProcessor(dbContentProviderSupport));
        registerAppService(new HttpDataSource(
                        new HttpDataSource.DefaultHttpRequestBuilder(){

                            @Override
                            protected HttpRequest createGetRequest(DataSourceRequest dataSourceRequest, String url, Uri uri) {
                                return super.createGetRequest(dataSourceRequest, url, uri);
                            }
                        },
                        new HttpDataSource.DefaultResponseStatusHandler(){
                            @Override
                            public void statusHandle(HttpDataSource client, DataSourceRequest dataSourceRequest, HttpRequest request, HttpRequest response, Holder<Boolean> isCached) throws IOException {
                                super.statusHandle(client, dataSourceRequest, request, response, isCached);
                            }
                        })
        );
        registerAppService(new ContentEntityProcessor(dbContentProviderSupport));
        registerAppService(new MovieEntityProcessor(dbContentProviderSupport));
        registerAppService(new MovieDetailProcessor(dbContentProviderSupport));
        registerAppService(new CastProcessor(dbContentProviderSupport));
        registerAppService(new ErrorHandler(
                "Error",
                "Check your internet connection",
                "Server error",
                "Developer error",
                "vlik1234@gmail.com"
        ));

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(BITMAP_DISPLAYER_OPTIONS).build();
        ImageLoader.getInstance().init(config);
    }

}
