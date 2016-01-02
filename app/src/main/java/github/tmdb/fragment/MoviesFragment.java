package github.tmdb.fragment;

import android.graphics.Movie;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.net.HttpURLConnection;

import by.istin.android.xcore.callable.ISuccess;
import by.istin.android.xcore.model.CursorModel;
import by.istin.android.xcore.provider.ModelContract;
import by.istin.android.xcore.source.DataSourceRequest;
import by.istin.android.xcore.source.impl.http.exception.IOStatusException;
import by.istin.android.xcore.fragment.collection.RecyclerViewFragment;
import github.tmdb.R;
import github.tmdb.adapter.FilmAdapter;
import github.tmdb.api.ApiTMDB;
import github.tmdb.core.cursor.FollowListCursor;
import github.tmdb.core.model.MovieEntity;
import github.tmdb.core.processor.MovieEntityProcessor;

/**
 * @author IvanBakach
 * @version on 13.11.2015
 */
public class MoviesFragment extends RecyclerViewFragment<FilmAdapter.ViewHolder, FilmAdapter, FollowListCursor> implements FilmAdapter.ITouch{

    public static final int SPAN_COUNT = 2;
    private View mEmptyView;

    @Override
    public FilmAdapter createAdapter(FragmentActivity fragmentActivity, FollowListCursor cursor) {
        return new FilmAdapter(getContext(), cursor, this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view != null) {
            mEmptyView = view.findViewById(android.R.id.empty);
        }
        return view;
    }

    @Override
    public RecyclerView getCollectionView() {
        RecyclerView recyclerView = super.getCollectionView();
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), SPAN_COUNT));
        return recyclerView;
    }

    @Override
    public void swap(FilmAdapter personListAdapter, FollowListCursor cursor) {
        personListAdapter.swapCursor(cursor);
    }

    @Override
    public int getViewLayout() {
        return R.layout.fragment_recycler_main;
    }

    @Override
    public long getCacheExpiration() {
        return 1L;
    }

    @Override
    public void onError(Exception exception, DataSourceRequest dataSourceRequest) {
        if (exception instanceof by.istin.android.xcore.source.impl.http.exception.IOStatusException) {
            if (((IOStatusException) exception).getStatusCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                hideProgress();
                mEmptyView.setVisibility(View.VISIBLE);
            } else {
                super.onError(exception, dataSourceRequest);
            }
        }
    }

    @Override
    public CursorModel.CursorModelCreator<FollowListCursor> getCursorModelCreator() {
        return FollowListCursor.CREATOR;
    }

    @Override
    public Uri getUri() {
        return ModelContract.getUri(MovieEntity.class);
    }

    @Override
    public String getUrl() {
        return ApiTMDB.getNowPlayingGet();
    }

    @Override
    public String getProcessorKey() {
        return MovieEntityProcessor.APP_SERVICE_KEY;
    }

    @Override
    public String getOrder() {
        return MovieEntity.ID;
    }

    @Override
    public void touchAction(long idItem) {

    }
}
