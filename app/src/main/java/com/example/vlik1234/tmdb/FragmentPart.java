package com.example.vlik1234.tmdb;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.vlik1234.tmdb.bo.DescriptionOfTheFilm;
import com.example.vlik1234.tmdb.bo.Film;
import com.example.vlik1234.tmdb.helper.DataManager;
import com.example.vlik1234.tmdb.image.ImageLoader;
import com.example.vlik1234.tmdb.processing.FilmArrayProcessor;
import com.example.vlik1234.tmdb.source.HttpDataSource;
import com.example.vlik1234.tmdb.source.TMDBDataSource;

import java.util.List;

/**
 * Created by VLIK on 12.01.2015.
 */
//TODO rename
public class FragmentPart extends Fragment implements DataManager.Callback<List<Film>>{

    static class ViewHolder {
        TextView title;
    }

    //TODO accessors
    ViewHolder holder = new ViewHolder();

    String mUrl = ApiTMDB.getNowPlayingGet(1);

    private Long selectItemID;

    private ArrayAdapter mAdapter;
    private FilmArrayProcessor mFilmArrayProcessor = new FilmArrayProcessor();
    private ImageLoader mImageLoader;

    SwipeRefreshLayout mSwipeRefreshLayout;
    AbsListView listView;
    TextView err;
    TextView empty;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        err = (TextView)v.findViewById(R.id.errorr);
        empty = (TextView)v.findViewById(R.id.emptyr);
        progressBar = (ProgressBar)v.findViewById(R.id.progressr);
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_containerr);
        listView = (AbsListView)v.findViewById(R.id.listr);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Now playing");

    }

    @Override
    public void onResume() {
        super.onResume();
        //TODO move to onCreate
        mImageLoader = ImageLoader.get(getActivity().getApplicationContext());

        final HttpDataSource dataSource = getHttpDataSource();
        final FilmArrayProcessor processor = getProcessor();
        //TODO move to onCreate
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                update(dataSource, processor);
            }
        });

        update(dataSource, processor);
    }

    private FilmArrayProcessor getProcessor() {
        return mFilmArrayProcessor;
    }

    private HttpDataSource getHttpDataSource() {
        return  new TMDBDataSource();
    }

    private void update(HttpDataSource dataSource, FilmArrayProcessor processor) {
        DataManager.loadData(this,
                getUrl(),
                dataSource,
                processor);
    }

    private String getUrl() {
        return mUrl;
    }

    @Override
    public void onDataLoadStart() {
        if (!mSwipeRefreshLayout.isRefreshing()) {
            progressBar.setVisibility(View.VISIBLE);
        }
        empty.setVisibility(View.GONE);
    }

    private List<Film> mData;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onDone(List<Film> data) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        progressBar.setVisibility(View.GONE);
        if (data == null || data.isEmpty()) {
            empty.setVisibility(View.VISIBLE);
        }
        if (mAdapter == null) {
            mData = data;

            mAdapter = new ArrayAdapter<Film>(getActivity().getApplicationContext(), R.layout.adapter_item, android.R.id.text1, data) {

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    if (convertView == null) {
                        convertView = View.inflate(getActivity().getApplicationContext(), R.layout.adapter_item, null);
                    }
                    Film item = getItem(position);
                    holder.title = (TextView) convertView.findViewById(R.id.title);

                    //TODO create variable
                    final SpannableString text = new SpannableString(item.getTitle()+" "+item.getReleaseDate());
                    //TODO magic number
                    text.setSpan(new RelativeSizeSpan(0.8f), text.length() - item.getReleaseDate().length(), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    text.setSpan(new TypefaceSpan("serif"), text.length() - item.getReleaseDate().length(), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    holder.title.setText(text);

                    convertView.setTag(item.getId());
                    final ImageView poster = (ImageView) convertView.findViewById(R.id.poster);
                    final String url = item.getPosterPath(ApiTMDB.SizePoster.w185);
                    mImageLoader.loadAndDisplay(url, poster);
                    return convertView;
                }

            };
            listView.setAdapter(mAdapter);
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    switch (scrollState) {
                        case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                            mImageLoader.resume();
                            break;
                        case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                            mImageLoader.pause();
                            break;
                        case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                            mImageLoader.pause();
                            break;
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }
            });
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Film item = (Film) mAdapter.getItem(position);
                    selectItemID = item.getId();

                    DescriptionOfTheFilm description = new DescriptionOfTheFilm(ApiTMDB.getMovie(selectItemID));
                    Intent intent = new Intent(getActivity().getApplicationContext(), DetailsActivity.class);
                    intent.putExtra(DescriptionOfTheFilm.class.getCanonicalName(), description);
                    startActivity(intent);
                }
            });
        } else {
            mData.clear();
            mData.addAll(data);
            mAdapter.notifyDataSetChanged();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
