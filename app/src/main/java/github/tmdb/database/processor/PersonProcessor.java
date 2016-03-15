package github.tmdb.database.processor;

import android.content.ContentValues;

import by.istin.android.xcore.db.IDBConnection;
import by.istin.android.xcore.db.impl.DBHelper;
import by.istin.android.xcore.processor.impl.AbstractGsonBatchProcessor;
import by.istin.android.xcore.provider.IDBContentProviderSupport;
import by.istin.android.xcore.source.DataSourceRequest;
import github.tmdb.database.model.Cast;
import github.tmdb.database.model.Crew;
import github.tmdb.database.model.Genre;
import github.tmdb.database.model.MovieDetailEntity;
import github.tmdb.database.model.Person;
import github.tmdb.database.model.ProductionCompany;
import github.tmdb.database.model.ProductionCountry;

/**
 * @author IvanBakach
 * @version on 13.11.2015
 */

public class PersonProcessor extends AbstractGsonBatchProcessor<ContentValues> {

    public static final String SYSTEM_SERVICE_KEY = "xcore:person:processor";

    public PersonProcessor(IDBContentProviderSupport contentProviderSupport) {
        super(Person.class, ContentValues.class, contentProviderSupport);
    }

    @Override
    protected void onStartProcessing(DataSourceRequest dataSourceRequest, IDBConnection dbConnection) {
        super.onStartProcessing(dataSourceRequest, dbConnection);
//        dbConnection.delete(DBHelper.getTableName(Person.class), null, null);
    }

    @Override
    protected void onProcessingFinish(DataSourceRequest dataSourceRequest, ContentValues followListResponse) throws Exception {
        super.onProcessingFinish(dataSourceRequest, followListResponse);
        notifyChange(getHolderContext(), Person.class);
    }

    @Override
    public String getAppServiceKey() {
        return SYSTEM_SERVICE_KEY;
    }

}
