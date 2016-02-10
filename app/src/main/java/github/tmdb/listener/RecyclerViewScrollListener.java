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
import github.tmdb.bo.Film;
import github.tmdb.core.processor.MovieEntityProcessor;
import github.tmdb.processing.FilmArrayProcessor;
import github.tmdb.source.DataSource;
import github.tmdb.source.TMDBDataSource;

/**
 * @author Ivan Bakach
 * @version on 08.08.2015
 */
public class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    private final LinearLayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private View mFooterProgress;
    private final String mUrl;
    private int mPage = 1;

    public RecyclerViewScrollListener(LinearLayoutManager layoutManager, String url, RecyclerView.Adapter adapter) {
        mLayoutManager = layoutManager;
        mUrl = url;
        mAdapter = adapter;
    }

    private String getUrl(int page) {
        StringBuilder controlUrl = new StringBuilder(mUrl.split("[?]")[0]);
        controlUrl.append("?api_key=f413bc4bacac8dff174a909f8ef535ae");
        controlUrl.append(ApiTMDB.getPage(controlUrl.toString(), page));
        controlUrl.append(ApiTMDB.getLanguage(controlUrl.toString())).append(Locale.getDefault().getLanguage());
        return controlUrl.toString();
    }

    public DataSource getDataSource() {
        return new TMDBDataSource();
    }

    public FilmArrayProcessor getProcessor() {
        return new FilmArrayProcessor();
    }

    private boolean isUpdate = true;
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        int visibleItemCount = mLayoutManager.getChildCount();
        final int totalItemCount = mLayoutManager.getItemCount();
        int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
//        if (mFooterProgress == null) {
//            mFooterProgress = View.inflate(mContext, R.layout.view_footer_progress, null);
//        }
        Log.d("TAG", visibleItemCount + " " + pastVisiblesItems+ " " + totalItemCount + " " + ((visibleItemCount + pastVisiblesItems)>=totalItemCount) );

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
//                            mAdapter.notifyDataSetChanged();
                                mPage++;
                                isUpdate = true;
                            }

                            @Override
                            public void onError(Exception exception) {
                            }
                        }
                );
            }
//            DataManager.loadData(new DataManager.Callback<ArrayList<Film>>() {
//                                     @Override
//                                     public void onDataLoadStart() {
//                                         refreshFooter();
//                                     }
//
//                                     @Override
//                                     public void onDone(ArrayList<Film> data) {
//                                         updateAdapter(data);
//                                     }
//
//                                     @Override
//                                     public void onError(Exception e) {
//                                     }
//                                 },
//                    getUrl(mPage),
//                    getDataSource(),
//                    getProcessor());
        }
    }

    private void updateAdapter(List<Film> data) {
//        if (data != null && data.size() == COUNT) {
//            mIsPagingEnabled = true;
////            mListView.addFooterView(mFooterProgress, null, false);
//        } else {
//            mIsPagingEnabled = false;
////            mListView.removeFooterView(mFooterProgress);
//        }
//        mAdapter.addAll((ArrayList<Film>) data);
        mAdapter.notifyDataSetChanged();
    }

    private void refreshFooter() {
//        if (mFooterProgress != null) {
//            if (mIsPagingEnabled) {
//                mFooterProgress.setVisibility(View.VISIBLE);
//            } else {
//                mFooterProgress.setVisibility(View.GONE);
//            }
//        }
    }
}
