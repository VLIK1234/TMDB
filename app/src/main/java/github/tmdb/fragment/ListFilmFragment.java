package github.tmdb.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import github.tmdb.R;
import github.tmdb.adapter.CustomAdapter;
import github.tmdb.api.ApiTMDB;
import github.tmdb.app.DetailsActivity;
import github.tmdb.bo.DescriptionOfTheFilm;
import github.tmdb.bo.Film;
import github.tmdb.helper.DataManager;
import github.tmdb.image.ImageLoader;
import github.tmdb.listener.ListViewListener;
import github.tmdb.processing.FilmArrayProcessor;
import github.tmdb.source.HttpDataSource;
import github.tmdb.source.TMDBDataSource;

/**
 * Created by ASUS on 31.01.2015.
 */
public class ListFilmFragment extends BaseFragment implements DataManager.Callback<List<Film>> {
    public static final String EXTRA_KEY = "extra_lang";

    private int page = 1;
    private String url = "";
    private Long selectItemID;
    private ArrayAdapter adapter;
    private ImageLoader imageLoader;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private TextView err;
    private TextView empty;
    private Activity activity;
    private ProgressBar progressBar;
    private List<Film> data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        err = (TextView) v.findViewById(R.id.error);
        empty = (TextView) v.findViewById(R.id.empty);
        progressBar = (ProgressBar) v.findViewById(R.id.progress);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
        listView = (ListView) v.findViewById(R.id.list);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if ((activity = getActivity()) != null) {
            imageLoader = ImageLoader.get(activity.getApplicationContext());
        }
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
        ListFilmFragment fragmentPart = new ListFilmFragment();
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
        controlUrl.append(ApiTMDB.getLanguage(controlUrl.toString())).append(Locale.getDefault().getLanguage());
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
    public void onDone(List<Film> data) {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        progressBar.setVisibility(View.GONE);
        if (data == null || data.isEmpty()) {
            empty.setVisibility(View.VISIBLE);
        }
        if (adapter == null) {
            this.data = data;
            adapter = new CustomAdapter(activity.getApplicationContext(), R.layout.adapter_item, android.R.id.text1, data, imageLoader);
            listView.setAdapter(adapter);
            listView.setOnScrollListener(new ListViewListener(activity.getApplicationContext(), listView, imageLoader, data, adapter, url));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Film item = (Film) adapter.getItem(position);
                    selectItemID = item.getId();

                    DescriptionOfTheFilm description = new DescriptionOfTheFilm(ApiTMDB.getMovie(selectItemID));
                    Intent intent = new Intent(activity.getApplicationContext(), DetailsActivity.class);
                    intent.putExtra(DescriptionOfTheFilm.class.getCanonicalName(), description);
                    startActivity(intent);
                }
            });
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

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
        progressBar.setVisibility(View.GONE);
        empty.setVisibility(View.GONE);
        err.setVisibility(View.VISIBLE);
        err.setText(err.getText() + "\n" + e.getMessage());
    }
}
