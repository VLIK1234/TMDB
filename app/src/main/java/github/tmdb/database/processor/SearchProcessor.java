package github.tmdb.database.processor;

import android.content.ContentValues;
import android.os.Parcel;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import by.istin.android.xcore.ContextHolder;
import by.istin.android.xcore.db.IDBConnection;
import by.istin.android.xcore.db.impl.DBHelper;
import by.istin.android.xcore.model.ParcelableModel;
import by.istin.android.xcore.processor.impl.AbstractGsonBatchProcessor;
import by.istin.android.xcore.provider.IDBContentProviderSupport;
import by.istin.android.xcore.source.DataSourceRequest;
import by.istin.android.xcore.utils.StringUtil;
import github.tmdb.database.model.MovieItemEntity;
import github.tmdb.database.model.SearchItem;


public class SearchProcessor extends AbstractGsonBatchProcessor<SearchProcessor.Response> {

    public static final String APP_SERVICE_KEY = "core:search:processor";

    public static class Response extends ParcelableModel {

        @SerializedName("results")
        private List<ContentValues> results;

        public static final Creator<Response> CREATOR = new Creator<Response>() {
            public Response createFromParcel(Parcel in) {
                return new Response(in);
            }

            public Response[] newArray(int size) {
                return new Response[size];
            }
        };

        public Response() {
            super();
        }

        public Response(Parcel in) {
            super();
            results = readContentValuesList(in);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            if (results != null) {
                writeContentValuesArray(dest, results.toArray(new ContentValues[results.size()]));
            }
        }

        public List<ContentValues> getData() {
            return results;
        }
    }

    public SearchProcessor(IDBContentProviderSupport contentProviderSupport) {
        super(SearchItem.class, Response.class, contentProviderSupport);
    }

    @Override
    public String getAppServiceKey() {
        return APP_SERVICE_KEY;
    }

    @Override
    protected void onStartProcessing(DataSourceRequest dataSourceRequest, IDBConnection dbConnection) {
        super.onStartProcessing(dataSourceRequest, dbConnection);
        dbConnection.delete(DBHelper.getTableName(SearchItem.class), null, null);
    }

    @Override
    protected void onProcessingFinish(DataSourceRequest dataSourceRequest, Response response) throws Exception {
        super.onProcessingFinish(dataSourceRequest, response);
        notifyChange(ContextHolder.get(), SearchItem.class);
    }

    @Override
    protected int getListBufferSize() {
        return 0;
    }
}
