package github.tmdb.database.processor;

import android.content.ContentValues;
import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import by.istin.android.xcore.ContextHolder;
import by.istin.android.xcore.db.IDBConnection;
import by.istin.android.xcore.db.impl.DBHelper;
import by.istin.android.xcore.model.ParcelableModel;
import by.istin.android.xcore.processor.impl.AbstractGsonBatchProcessor;
import by.istin.android.xcore.provider.IDBContentProviderSupport;
import by.istin.android.xcore.source.DataSourceRequest;
import github.tmdb.database.model.Content;
import github.tmdb.database.model.SampleEntity;

public class ContentEntityProcessor extends AbstractGsonBatchProcessor<ContentEntityProcessor.Response> {

    public static final String APP_SERVICE_KEY = "core:advancedentity:processor";

    public static class Response extends ParcelableModel {

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
            data.updates = readContentValuesList(in);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            List<ContentValues> updates = data.updates;
            if (updates != null) {
                writeContentValuesArray(dest, updates.toArray(new ContentValues[updates.size()]));
            }
        }

        public static class Data {

            @SerializedName("updates")
            private List<ContentValues> updates;

            public List<ContentValues> getUpdates() {
                return updates;
            }
        }

        @SerializedName("data")
        private Data data = new Data();

        public Data getData() {
            return data;
        }
    }

    public ContentEntityProcessor(IDBContentProviderSupport contentProviderSupport) {
        super(Content.class, Response.class, contentProviderSupport);
    }

    @Override
    public String getAppServiceKey() {
        return APP_SERVICE_KEY;
    }

    @Override
    protected void onStartProcessing(DataSourceRequest dataSourceRequest, IDBConnection dbConnection) {
        super.onStartProcessing(dataSourceRequest, dbConnection);
        dbConnection.delete(DBHelper.getTableName(SampleEntity.class), null, null);
    }

    @Override
    protected void onProcessingFinish(DataSourceRequest dataSourceRequest, Response response) throws Exception {
        super.onProcessingFinish(dataSourceRequest, response);
        notifyChange(ContextHolder.get(), Content.class);
    }

    @Override
    protected int getListBufferSize() {
        return 0;
    }
}
