package github.tmdb.listener;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;

import github.tmdb.R;
import github.tmdb.bo.Film;
import github.tmdb.helper.DataManager;
import github.tmdb.image.ImageLoader;
import github.tmdb.processing.Processor;
import github.tmdb.source.DataSource;

/**
 * Created by ASUS on 02.02.2015.
 */
public abstract class AbstractScrollListener implements AbsListView.OnScrollListener {

    public static final int COUNT = 100;
    public List data;
    public ListView listView;
    public ArrayAdapter adapter;
    private boolean isPagingEnabled = true;
    private boolean isImageLoaderControlledByDataManager = false;
    private View footerProgress;
    public ImageLoader imageLoader;
    public Context context;
    public String url;
    private int page = 1;

    private int previousTotal = 0;

    public abstract String getUrl(int page);

    public abstract DataSource getDataSource();

    public abstract Processor getProcessor();

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                if (!isImageLoaderControlledByDataManager) {
                    imageLoader.resume();
                }
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                if (!isImageLoaderControlledByDataManager) {
                    imageLoader.pause();
                }
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                if (!isImageLoaderControlledByDataManager) {
                    imageLoader.pause();
                }
                break;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        ListAdapter adapter = view.getAdapter();
        int count = getRealAdapterCount(adapter);
        if (count == 0) {
            return;
        }
        if (footerProgress == null) {
            footerProgress = View.inflate(context, R.layout.view_footer_progress, null);
        }
        int visibleThreshold = 10;
        if (previousTotal != totalItemCount && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            previousTotal = totalItemCount;
            isImageLoaderControlledByDataManager = true;
            page++;
            DataManager.loadData(new DataManager.Callback<List<Film>>() {
                                     @Override
                                     public void onDataLoadStart() {
                                         imageLoader.pause();
                                         refreshFooter();
                                     }

                                     @Override
                                     public void onDone(List<Film> data) {
                                         updateAdapter(data);
                                         imageLoader.resume();
                                         isImageLoaderControlledByDataManager = false;
                                     }

                                     @Override
                                     public void onError(Exception e) {
                                         imageLoader.resume();
                                         isImageLoaderControlledByDataManager = false;
                                     }
                                 },
                    getUrl(page),
                    getDataSource(),
                    getProcessor());
        }
    }

    private void updateAdapter(List<Film> data) {
        if (data != null && data.size() == COUNT) {
            isPagingEnabled = true;
            listView.addFooterView(footerProgress, null, false);
        } else {
            isPagingEnabled = false;
            listView.removeFooterView(footerProgress);
        }
        if (data != null) {
            this.data.addAll(data);
        }
        adapter.notifyDataSetChanged();
    }

    public static int getRealAdapterCount(ListAdapter adapter) {
        if (adapter == null) {
            return 0;
        }
        int count = adapter.getCount();

        if (adapter instanceof HeaderViewListAdapter) {
            HeaderViewListAdapter headerViewListAdapter = (HeaderViewListAdapter) adapter;
            count = count - headerViewListAdapter.getFootersCount() - headerViewListAdapter.getHeadersCount();
        }
        return count;
    }

    private void refreshFooter() {
        if (footerProgress != null) {
            if (isPagingEnabled) {
                footerProgress.setVisibility(View.VISIBLE);
            } else {
                footerProgress.setVisibility(View.GONE);
            }
        }
    }
}
