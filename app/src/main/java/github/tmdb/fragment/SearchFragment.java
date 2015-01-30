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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import github.tmdb.R;
import github.tmdb.api.ApiTMDB;
import github.tmdb.app.DetailsActivity;
import github.tmdb.bo.DescriptionOfTheFilm;
import github.tmdb.bo.Film;
import github.tmdb.helper.DataManager;
import github.tmdb.image.ImageLoader;
import github.tmdb.processing.FilmArrayProcessor;
import github.tmdb.source.HttpDataSource;
import github.tmdb.source.TMDBDataSource;

/**
 * Created by VLIK on 12.01.2015.
 */
public class SearchFragment extends Fragment implements DataManager.Callback<List<Film>> {

    public static final String EXTRA_LANG = "extra_lang";
    private int PAGE;

    static class ViewHolder {
        TextView title;
        TextView date;
        RatingBar rating;
        TextView ratingText;
    }

    private ViewHolder holder = new ViewHolder();

    private String url = "";

    private Long selectItemID;

    private ArrayAdapter adapter;
    private FilmArrayProcessor filmArrayProcessor = new FilmArrayProcessor();
    private ImageLoader imageLoader;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private TextView err;
    private TextView empty;
    private ProgressBar progressBar;
    private Activity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

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
        PAGE = 1;

        url = getLanguage();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PAGE = 1;
                update(dataSource, processor);
            }
        });

        update(dataSource, processor);
    }

    public static Fragment newInstance(String language) {
        SearchFragment fragmentPart = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_LANG, language);
        fragmentPart.setArguments(args);
        return fragmentPart;
    }

    private String getLanguage() {
        return getArguments().getString(EXTRA_LANG);
    }

    private FilmArrayProcessor getProcessor() {
        return filmArrayProcessor;
    }

    private HttpDataSource getHttpDataSource() {
        return new TMDBDataSource();
    }

    private void update(HttpDataSource dataSource, FilmArrayProcessor processor) {
        DataManager.loadData(this,
                getUrl(PAGE),
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

    private List<Film> data;
    private boolean isPagingEnabled = true;
    private View footerProgress;
    private boolean isImageLoaderControlledByDataManager = false;

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
        if (footerProgress == null)
            footerProgress = View.inflate(activity.getApplicationContext(), R.layout.view_footer_progress, null);
        refreshFooter();
        if (adapter == null) {
            this.data = data;
            adapter = new ArrayAdapter<Film>(activity.getApplicationContext(), R.layout.adapter_item, android.R.id.text1, data) {

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    if (convertView == null) {
                        convertView = View.inflate(activity.getApplicationContext(), R.layout.adapter_item, null);
                    }
                    Film item = getItem(position);
                    holder.title = (TextView) convertView.findViewById(R.id.title);
                    holder.date = (TextView) convertView.findViewById(R.id.date);
                    holder.rating = (RatingBar) convertView.findViewById(R.id.rating);
                    holder.ratingText = (TextView) convertView.findViewById(R.id.rating_text);

                    holder.title.setText(item.getTitle());
                    holder.date.setText(item.getReleaseDate());
                    holder.rating.setRating(Float.valueOf(item.getVoteAverage()));
                    holder.ratingText.setText(item.getVoteAverage() + "/10" + " (" + item.getVoteCount() + ")");

                    convertView.setTag(item.getId());
                    final ImageView poster = (ImageView) convertView.findViewById(R.id.poster);
                    final String url = item.getPosterPath(ApiTMDB.SizePoster.w185);
                    imageLoader.loadAndDisplay(url, poster);
                    return convertView;
                }

            };
            listView.setFooterDividersEnabled(true);
            listView.addFooterView(footerProgress, null, false);
            listView.setAdapter(adapter);
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {

                private int previousTotal = 0;
                private int visibleThreshold = 5;

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
                    if (previousTotal != totalItemCount && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                        previousTotal = totalItemCount;
                        isImageLoaderControlledByDataManager = true;
                        PAGE++;
                        DataManager.loadData(new DataManager.Callback<List<Film>>() {
                                                 @Override
                                                 public void onDataLoadStart() {
                                                     imageLoader.pause();
                                                 }

                                                 @Override
                                                 public void onDone(List<Film> data) {
                                                     updateAdapter(data);
                                                     refreshFooter();
                                                     imageLoader.resume();
                                                     isImageLoaderControlledByDataManager = false;
                                                 }

                                                 @Override
                                                 public void onError(Exception e) {
                                                     SearchFragment.this.onError(e);
                                                     imageLoader.resume();
                                                     isImageLoaderControlledByDataManager = false;
                                                 }
                                             },
                                getUrl(PAGE),
                                getHttpDataSource(),
                                getProcessor());
                    }
                }
            });
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
            isPagingEnabled = data != null && data.size() == 100;
        } else {
            this.data.clear();
            updateAdapter(data);
        }
        refreshFooter();
    }

    private void updateAdapter(List<Film> data) {
        if (data != null && data.size() == 100) {
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

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
        progressBar.setVisibility(View.GONE);
        empty.setVisibility(View.GONE);
        err.setVisibility(View.VISIBLE);
        err.setText(err.getText() + "\n" + e.getMessage());
    }
}