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
import github.tmdb.processing.Processor;
import github.tmdb.source.DataSource;

/**
 @author IvanBakach
 @version on 02.02.2015
 */
public abstract class AbstractScrollListener implements AbsListView.OnScrollListener {

    public static final int COUNT = 100;
    public List mData;
    public ListView mListView;
    public ArrayAdapter mAdapter;
    private boolean mIsPagingEnabled = true;
    private View mFooterProgress;
    public Context mContext;
    public String mUrl;
    private int mPage = 1;

    private int mPreviousTotal = 0;

    public abstract String getUrl(int page);

    public abstract DataSource getDataSource();

    public abstract Processor getProcessor();

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        ListAdapter adapter = view.getAdapter();
        int count = getRealAdapterCount(adapter);
        if (count == 0) {
            return;
        }
        if (mFooterProgress == null) {
            mFooterProgress = View.inflate(mContext, R.layout.view_footer_progress, null);
        }
        int visibleThreshold = 10;
        if (mPreviousTotal != totalItemCount && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            mPreviousTotal = totalItemCount;
            mPage++;
            DataManager.loadData(new DataManager.Callback<List<Film>>() {
                                     @Override
                                     public void onDataLoadStart() {
                                         refreshFooter();
                                     }

                                     @Override
                                     public void onDone(List<Film> data) {
                                         updateAdapter(data);
                                     }

                                     @Override
                                     public void onError(Exception e) {
                                     }
                                 },
                    getUrl(mPage),
                    getDataSource(),
                    getProcessor());
        }
    }

    private void updateAdapter(List<Film> data) {
        if (data != null && data.size() == COUNT) {
            mIsPagingEnabled = true;
            mListView.addFooterView(mFooterProgress, null, false);
        } else {
            mIsPagingEnabled = false;
            mListView.removeFooterView(mFooterProgress);
        }
        if (data != null) {
            mData.addAll(data);
        }
        mAdapter.notifyDataSetChanged();
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
        if (mFooterProgress != null) {
            if (mIsPagingEnabled) {
                mFooterProgress.setVisibility(View.VISIBLE);
            } else {
                mFooterProgress.setVisibility(View.GONE);
            }
        }
    }
}
