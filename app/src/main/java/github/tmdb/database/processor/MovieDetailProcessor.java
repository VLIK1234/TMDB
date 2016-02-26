package github.tmdb.database.processor;

import android.content.ContentValues;

import by.istin.android.xcore.db.IDBConnection;
import by.istin.android.xcore.processor.impl.AbstractGsonBatchProcessor;
import by.istin.android.xcore.provider.IDBContentProviderSupport;
import by.istin.android.xcore.source.DataSourceRequest;
import github.tmdb.database.model.Cast;
import github.tmdb.database.model.Crew;
import github.tmdb.database.model.Genre;
import github.tmdb.database.model.MovieDetailEntity;
import github.tmdb.database.model.ProductionCompany;
import github.tmdb.database.model.ProductionCountry;

/**
 * @author IvanBakach
 * @version on 13.11.2015
 */

public class MovieDetailProcessor extends AbstractGsonBatchProcessor<ContentValues> {

    public static final String SYSTEM_SERVICE_KEY = "xcore:moviedetail:processor";

    public MovieDetailProcessor(IDBContentProviderSupport contentProviderSupport) {
        super(MovieDetailEntity.class, ContentValues.class, contentProviderSupport);
    }

    @Override
    protected void onStartProcessing(DataSourceRequest dataSourceRequest, IDBConnection dbConnection) {
        super.onStartProcessing(dataSourceRequest, dbConnection);
//        dbConnection.delete(DBHelper.getTableName(MovieDetailEntity.class), null, null);
//        dbConnection.delete(DBHelper.getTableName(Cast.class), null, null);
//        dbConnection.delete(DBHelper.getTableName(Crew.class), null, null);
//        dbConnection.delete(DBHelper.getTableName(Genre.class), null, null);
//        dbConnection.delete(DBHelper.getTableName(ProductionCompany.class), null, null);
//        dbConnection.delete(DBHelper.getTableName(ProductionCountry.class), null, null);
    }

    @Override
    protected void onProcessingFinish(DataSourceRequest dataSourceRequest, ContentValues followListResponse) throws Exception {
        super.onProcessingFinish(dataSourceRequest, followListResponse);
        notifyChange(getHolderContext(), MovieDetailEntity.class);
        notifyChange(getHolderContext(), Cast.class);
        notifyChange(getHolderContext(), Crew.class);
        notifyChange(getHolderContext(), Genre.class);
        notifyChange(getHolderContext(), ProductionCompany.class);
        notifyChange(getHolderContext(), ProductionCountry.class);
    }

    @Override
    public String getAppServiceKey() {
        return SYSTEM_SERVICE_KEY;
    }

}
