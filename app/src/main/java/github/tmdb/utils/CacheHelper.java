package github.tmdb.utils;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.text.format.DateUtils;

import by.istin.android.xcore.Core;
import by.istin.android.xcore.service.DataSourceService;
import by.istin.android.xcore.service.StatusResultReceiver;
import by.istin.android.xcore.source.DataSourceRequest;
import by.istin.android.xcore.source.impl.http.HttpDataSource;
import by.istin.android.xcore.utils.StringUtil;
import github.tmdb.api.Api;
import github.tmdb.api.ApiTMDB;
import github.tmdb.database.processor.PersonProcessor;

public class CacheHelper {

    protected static Core.SimpleDataSourceServiceListener getEmptyListener() {
        return new Core.SimpleDataSourceServiceListener() {
            @Override
            public void onDone(Bundle resultData) {

            }
        };
    }

    public static void prepareWatchList(Context context) {
//        preCache(context, PersonProcessor.SYSTEM_SERVICE_KEY, ApiTMDB.getWatchListLater(null), DateUtils.DAY_IN_MILLIS, true, getEmptyListener());
    }

    public static void preCache(Context context, String processor, String url, Long cacheExpiration, final Core.SimpleDataSourceServiceListener listener) {
        preCache(context, processor, url, cacheExpiration, false, listener);
    }

    public static void preCache(Context context, String processor, String url, Long cacheExpiration, boolean forceUpdate, final Core.SimpleDataSourceServiceListener listener) {
        preCache(context, processor, url, cacheExpiration, forceUpdate, null, listener);
    }

    public static void preCache(Context context, String processor, String url, Long cacheExpiration, boolean forceUpdate, String dataSourceKey, final Core.SimpleDataSourceServiceListener listener) {
        if (!TextUtils.isEmpty(url)) {
            DataSourceRequest dataSourceRequest = createDataSourceRequest(url, cacheExpiration, forceUpdate);

            if (StringUtil.isEmpty(dataSourceKey)) {
                dataSourceKey = HttpDataSource.APP_SERVICE_KEY;
            }

            execute(context, processor, dataSourceKey, listener, dataSourceRequest);
        }
    }

    public static void execute(Context context, String processor, String dataSourceKey, final Core.SimpleDataSourceServiceListener listener, DataSourceRequest dataSourceRequest) {
        DataSourceService.execute(context, dataSourceRequest, processor, dataSourceKey, new StatusResultReceiver(new Handler(Looper.getMainLooper())) {

            @Override
            public void onStart(Bundle resultData) {
                if (listener != null) {
                    listener.onStart(resultData);
                }
            }

            @Override
            public void onError(Exception exception) {
                if (listener != null) {
                    listener.onError(exception);
                }
            }

            @Override
            public void onDone(Bundle resultData) {
                if (listener != null) {
                    listener.onDone(resultData);
                }
            }

            @Override
            protected void onCached(Bundle resultData) {
                if (listener != null) {
                    listener.onCached(resultData);
                }
            }

        });
    }

    public static DataSourceRequest createDataSourceRequest(String url, Long cacheExpiration, boolean forceUpdate) {
        DataSourceRequest dataSourceRequest = new DataSourceRequest(url);
        dataSourceRequest.setCacheable(true);
        dataSourceRequest.setCacheExpiration(cacheExpiration);
        dataSourceRequest.setForceUpdateData(forceUpdate);
        return dataSourceRequest;
    }
}