package github.tmdb;

import android.content.Context;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import by.istin.android.xcore.XCoreHelper;
import by.istin.android.xcore.error.ErrorHandler;
import by.istin.android.xcore.provider.IDBContentProviderSupport;
import by.istin.android.xcore.source.impl.http.HttpDataSource;
import github.tmdb.database.model.Cast;
import github.tmdb.database.model.Content;
import github.tmdb.database.model.Crew;
import github.tmdb.database.model.Genre;
import github.tmdb.database.model.MovieDetailEntity;
import github.tmdb.database.model.MovieItemEntity;
import github.tmdb.database.model.Person;
import github.tmdb.database.model.ProductionCompany;
import github.tmdb.database.model.ProductionCountry;
import github.tmdb.database.model.SampleEntity;
import github.tmdb.database.model.SearchItem;
import github.tmdb.database.model.Series;
import github.tmdb.database.model.SeriesDetailEntity;
import github.tmdb.database.model.SpokenLanguage;
import github.tmdb.database.model.Video;
import github.tmdb.database.processor.CastProcessor;
import github.tmdb.database.processor.ContentEntityProcessor;
import github.tmdb.database.processor.MovieDetailProcessor;
import github.tmdb.database.processor.MovieEntityProcessor;
import github.tmdb.database.processor.PeopleProcessor;
import github.tmdb.database.processor.PersonProcessor;
import github.tmdb.database.processor.SampleEntityProcessor;
import github.tmdb.database.processor.SearchProcessor;
import github.tmdb.database.processor.SeriesDetailProcessor;
import github.tmdb.database.processor.SeriesProcessor;

public class XcoreAppModule extends XCoreHelper.BaseModule {

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
            Video.class,
            Person.class,
            Series.class,
            SeriesDetailEntity.class,
            SearchItem.class
    };

    private static final DisplayImageOptions BITMAP_DISPLAYER_OPTIONS = new DisplayImageOptions.Builder()
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
        registerAppService(new HttpDataSource(new HttpDataSource.DefaultHttpRequestBuilder(),
                           new HttpDataSource.DefaultResponseStatusHandler()));
        registerAppService(new ContentEntityProcessor(dbContentProviderSupport));
        registerAppService(new MovieEntityProcessor(dbContentProviderSupport));
        registerAppService(new MovieDetailProcessor(dbContentProviderSupport));
        registerAppService(new CastProcessor(dbContentProviderSupport));
        registerAppService(new PersonProcessor(dbContentProviderSupport));
        registerAppService(new SeriesProcessor(dbContentProviderSupport));
        registerAppService(new SeriesDetailProcessor(dbContentProviderSupport));
        registerAppService(new PeopleProcessor(dbContentProviderSupport));
        registerAppService(new SearchProcessor(dbContentProviderSupport));
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
