package github.tmdb.listener;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.List;
import java.util.Locale;

import by.istin.android.xcore.ContextHolder;
import by.istin.android.xcore.service.DataSourceService;
import by.istin.android.xcore.service.StatusResultReceiver;
import by.istin.android.xcore.source.DataSourceRequest;
import by.istin.android.xcore.source.impl.http.HttpDataSource;
import github.tmdb.api.ApiTMDB;
import github.tmdb.database.processor.MovieEntityProcessor;
import github.tmdb.source.DataSource;
import github.tmdb.source.TMDBDataSource;

/**
 * @author Ivan Bakach
 * @version on 08.08.2015
 */
public class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    private final LinearLayoutManager mLayoutManager;
    private final String mUrl;
    private int mPage = 1;

    public RecyclerViewScrollListener(LinearLayoutManager layoutManager, String url) {
        mLayoutManager = layoutManager;
        mUrl = url;
    }

    private String getUrl(int page) {
        StringBuilder controlUrl = new StringBuilder(mUrl.split("[?]")[0]);
        controlUrl.append("?api_key="+ApiTMDB.API_KEY);
        controlUrl.append(ApiTMDB.getPage(controlUrl.toString(), page));
        controlUrl.append(ApiTMDB.getLanguage(controlUrl.toString())).append(Locale.getDefault().getLanguage());
        return controlUrl.toString();
    }

    private boolean isUpdate = true;
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        int visibleItemCount = mLayoutManager.getChildCount();
        final int totalItemCount = mLayoutManager.getItemCount();
        int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
            if (isUpdate) {

                final DataSourceRequest dataSourceRequest = new DataSourceRequest(getUrl(mPage));
                dataSourceRequest.putParam("range", String.valueOf(mPage * 20));
                DataSourceService.execute(
                        ContextHolder.get(),
                        dataSourceRequest,
                        MovieEntityProcessor.APP_SERVICE_KEY,
                        HttpDataSource.APP_SERVICE_KEY,
                        new StatusResultReceiver(new Handler(Looper.getMainLooper())) {
                            @Override
                            public void onStart(Bundle resultData) {
                                isUpdate = false;
                            }

                            @Override
                            public void onDone(Bundle resultData) {
                                mPage++;
                                isUpdate = true;
                            }

                            @Override
                            public void onError(Exception exception) {
                            }
                        }
                );
            }
        }
    }
}
