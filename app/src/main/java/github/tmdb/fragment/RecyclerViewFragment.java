package github.tmdb.fragment;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import github.tmdb.R;
import github.tmdb.adapter.FilmAdapter;
import github.tmdb.api.ApiTMDB;
import github.tmdb.api.Language;
import github.tmdb.bo.Film;
import github.tmdb.helper.DataManager;
import github.tmdb.listener.RecyclerViewScrollListner;
import github.tmdb.processing.FilmArrayProcessor;
import github.tmdb.source.HttpDataSource;
import github.tmdb.source.TMDBDataSource;
import github.tmdb.utils.UIUtil;

/**
 * @author Ivan Bakach
 * @version on 09.08.2015
 */
public class RecyclerViewFragment extends BaseFragment implements DataManager.Callback<ArrayList<Film>> {
    public static final String EXTRA_KEY = "extra_lang";
    private static final int SPAN_COUNT = 3;

    private int page = 1;
    private String url = "";
    private Long selectItemID;
    private FilmAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private TextView err;
    private TextView empty;
    private ProgressBar progressBar;
    private List<Film> data;
    private LinearLayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recycler_main, container, false);
        err = (TextView) v.findViewById(R.id.tv_error);
        empty = (TextView) v.findViewById(R.id.tv_empty);
        progressBar = (ProgressBar) v.findViewById(R.id.pb_progress);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.srl_swipe_container);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.rv_list_films);
        mLayoutManager = getLayoutManger();
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final HttpDataSource dataSource = getHttpDataSource();
        final FilmArrayProcessor processor = getProcessor();

        page = 1;
        url = getUrlData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                update(dataSource, processor);
                adapter = null;
            }
        });
        update(dataSource, processor);
    }

    public static Fragment newInstance(String language) {
        RecyclerViewFragment fragmentPart = new RecyclerViewFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_KEY, language);
        fragmentPart.setArguments(args);
        return fragmentPart;
    }

    protected String getUrlData() {
        return getArguments().getString(EXTRA_KEY);
    }

    private FilmArrayProcessor getProcessor() {
        return new FilmArrayProcessor();
    }

    private HttpDataSource getHttpDataSource() {
        return new TMDBDataSource();
    }

    private void update(HttpDataSource dataSource, FilmArrayProcessor processor) {
        DataManager.loadData(this,
                getUrl(page),
                dataSource,
                processor);
    }

    private String getUrl(int page) {
        StringBuilder controlUrl = new StringBuilder(url);
        controlUrl.append(ApiTMDB.getPage(controlUrl.toString(), page));
        controlUrl.append(ApiTMDB.getLanguage(controlUrl.toString())).append(Language.getLanguage());
        return controlUrl.toString();
    }

    @Override
    public void onDataLoadStart() {
        if (!swipeRefreshLayout.isRefreshing()) {
            progressBar.setVisibility(View.VISIBLE);
        }
        empty.setVisibility(View.GONE);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onDone(ArrayList<Film> data) {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        progressBar.setVisibility(View.GONE);
        if (data == null || data.isEmpty()) {
            empty.setVisibility(View.VISIBLE);
        }
        if (adapter == null) {
            this.data = data;
            adapter = new FilmAdapter(data);
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.addOnScrollListener(new RecyclerViewScrollListner(mLayoutManager, url, adapter));
        } else {
            this.data.clear();
            updateAdapter(data);
        }
    }

    private void updateAdapter(List<Film> data) {
        if (data != null) {
            this.data.addAll(data);
        }
        adapter.notifyDataSetChanged();
    }

    private LinearLayoutManager getLayoutManger() {
        if (UIUtil.getOrientation() == Configuration.ORIENTATION_PORTRAIT) {
            return new LinearLayoutManager(getActivity());
        } else {
            return new GridLayoutManager(getActivity(), SPAN_COUNT);
        }
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
        progressBar.setVisibility(View.GONE);
        empty.setVisibility(View.GONE);
        err.setVisibility(View.VISIBLE);
        err.setText(err.getText() + "\n" + e.getMessage());
    }
}