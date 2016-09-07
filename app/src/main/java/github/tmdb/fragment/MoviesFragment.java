package github.tmdb.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import by.istin.android.xcore.db.impl.DBHelper;
import by.istin.android.xcore.fragment.collection.RecyclerViewFragment;
import by.istin.android.xcore.model.CursorModel;
import by.istin.android.xcore.provider.ModelContract;
import by.istin.android.xcore.source.DataSourceRequest;
import by.istin.android.xcore.utils.StringUtil;
import github.tmdb.R;
import github.tmdb.adapter.FilmAdapter;
import github.tmdb.api.ApiTMDB;
import github.tmdb.app.MainScreenActivity;
import github.tmdb.database.cursor.MoviesListCursor;
import github.tmdb.database.model.MovieItemEntity;
import github.tmdb.database.processor.MovieEntityProcessor;
import github.tmdb.listener.RecyclerViewScrollListener;

/**
 * @author IvanBakach
 * @version on 13.11.2015
 */
public class MoviesFragment extends RecyclerViewFragment<FilmAdapter.ViewHolder, FilmAdapter, MoviesListCursor> implements FilmAdapter.ITouch {

    private static final int SPAN_COUNT = 2;

    private static final String KEY_URL = "URL";
    private SwipeRefreshLayout swipeRefreshLayout;

    public static MoviesFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString(KEY_URL, url);
        MoviesFragment fragment = new MoviesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private String getUrlForLoad() {
        Bundle bundle = getArguments();
        return bundle !=null ? bundle.getString(KEY_URL) : ApiTMDB.getMoviePopular();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (ApiTMDB.getMovieNowPlaying().equals(getUrlForLoad())) {
            getActivity().setTitle(getString(R.string.now_playing));
        } else {
            getActivity().setTitle("Popular");
        }
    }

    @Override
    public FilmAdapter createAdapter(FragmentActivity fragmentActivity, MoviesListCursor cursor) {
        return new FilmAdapter(cursor, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view != null) {
            swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl_swipe_container);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refresh();
                }
            });
        }
//        if (view != null) {
//            mEmptyView = view.findViewById(android.R.id.empty);
//        }
        return view;
    }

    @Override
    public RecyclerView getCollectionView() {
        RecyclerView recyclerView = super.getCollectionView();
        LinearLayoutManager layoutManager = new GridLayoutManager(getContext(), SPAN_COUNT);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerViewScrollListener(layoutManager, ApiTMDB.getMovieNowPlaying()));
        return recyclerView;
    }

    @Override
    public void swap(FilmAdapter personListAdapter, MoviesListCursor cursor) {
        personListAdapter.swapCursor(cursor);
    }

    @Override
    public int getViewLayout() {
        return R.layout.fragment_recycler_main;
    }

    @Override
    public long getCacheExpiration() {
        return 0;
    }

    @Override
    public void onError(Exception exception, DataSourceRequest dataSourceRequest) {

    }

    @Override
    public CursorModel.CursorModelCreator<MoviesListCursor> getCursorModelCreator() {
        return MoviesListCursor.CREATOR;
    }

    @Override
    public Uri getUri() {
        return ModelContract.getSQLQueryUri("SELECT * FROM " + DBHelper.getTableName(MovieItemEntity.class)
                + " as m WHERE m." + MovieItemEntity.CATEGORY + " = \"" + getUrlForLoad()+"\"", ModelContract.getUri(MovieItemEntity.class));
    }

    @Override
    public String getUrl() {
        return getUrlForLoad()+"?api_key=f413bc4bacac8dff174a909f8ef535ae&page=1";
    }

    @Override
    public String getProcessorKey() {
        return MovieEntityProcessor.APP_SERVICE_KEY;
    }

    @Override
    public String getOrder() {
        return MovieItemEntity.POSITION;
    }

    @Override
    public void touchAction(long idItem) {
        ((MainScreenActivity) getActivity()).setCurrentFragment(MovieDetailFragment.newInstance(idItem), true);
    }

    @Override
    public void onReceiverOnDone(Bundle resultData) {
        super.onReceiverOnDone(resultData);
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
