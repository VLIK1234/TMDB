package github.tmdb.listener;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import github.tmdb.R;
import github.tmdb.adapter.FilmAdapter;
import github.tmdb.api.ApiTMDB;
import github.tmdb.bo.Film;
import github.tmdb.helper.DataManager;
import github.tmdb.processing.FilmArrayProcessor;
import github.tmdb.processing.Processor;
import github.tmdb.source.DataSource;
import github.tmdb.source.TMDBDataSource;

/**
 * @author Ivan Bakach
 * @version on 08.08.2015
 */
public class RecyclerViewScrollListner extends RecyclerView.OnScrollListener {

    private LinearLayoutManager mLayoutManager;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    public static final int COUNT = 100;
    public List<Film> mData = new ArrayList<>();
    public ListView mListView;
    public FilmAdapter mAdapter;
    private boolean mIsPagingEnabled = true;
    private View mFooterProgress;
    public String mUrl;
    private int mPage = 1;

    public RecyclerViewScrollListner(LinearLayoutManager layoutManager, String url, FilmAdapter adapter) {
        mLayoutManager = layoutManager;
        mUrl = url;
        mAdapter = adapter;
    }

    public String getUrl(int page) {
        StringBuilder controlUrl = new StringBuilder(mUrl);
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

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        visibleItemCount = mLayoutManager.getChildCount();
        totalItemCount = mLayoutManager.getItemCount();
        pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
//        if (mFooterProgress == null) {
//            mFooterProgress = View.inflate(mContext, R.layout.view_footer_progress, null);
//        }
        Log.d("TAG", visibleItemCount +" "+ pastVisiblesItems+" "+ (visibleItemCount+pastVisiblesItems)+ " " + totalItemCount);

        if (loading) {
            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                loading = false;
                mPage++;
                DataManager.loadData(new DataManager.Callback<ArrayList<Film>>() {
                                         @Override
                                         public void onDataLoadStart() {
                                             refreshFooter();
                                         }

                                         @Override
                                         public void onDone(ArrayList<Film> data) {
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
    }

    private void updateAdapter(List<Film> data) {
//        if (data != null && data.size() == COUNT) {
//            mIsPagingEnabled = true;
////            mListView.addFooterView(mFooterProgress, null, false);
//        } else {
//            mIsPagingEnabled = false;
////            mListView.removeFooterView(mFooterProgress);
//        }
        mAdapter.addAll((ArrayList<Film>)data);
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
