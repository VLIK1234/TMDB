package github.tmdb.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import by.istin.android.xcore.fragment.collection.RecyclerViewFragment;
import by.istin.android.xcore.model.CursorModel;
import by.istin.android.xcore.provider.ModelContract;
import by.istin.android.xcore.source.DataSourceRequest;
import github.tmdb.R;
import github.tmdb.adapter.FilmAdapter;
import github.tmdb.api.ApiTMDB;
import github.tmdb.app.MainScreenActivity;
import github.tmdb.core.cursor.MoviesListCursor;
import github.tmdb.core.model.MovieItemEntity;
import github.tmdb.core.processor.MovieEntityProcessor;
import github.tmdb.listener.RecyclerViewScrollListener;

/**
 * @author IvanBakach
 * @version on 13.11.2015
 */
public class MoviesFragment extends RecyclerViewFragment<FilmAdapter.ViewHolder, FilmAdapter, MoviesListCursor> implements FilmAdapter.ITouch {

    public static final int SPAN_COUNT = 2;
//    private View mEmptyView;

    @Override
    public FilmAdapter createAdapter(FragmentActivity fragmentActivity, MoviesListCursor cursor) {
        return new FilmAdapter(cursor, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
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
        recyclerView.addOnScrollListener(new RecyclerViewScrollListener(layoutManager, ApiTMDB.getNowPlayingGet(), recyclerView.getAdapter()));
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
        return 1L;
    }

    @Override
    public void onError(Exception exception, DataSourceRequest dataSourceRequest) {
//        if (exception instanceof by.istin.android.xcore.source.impl.http.exception.IOStatusException) {
//            if (((IOStatusException) exception).getStatusCode() == HttpURLConnection.HTTP_NOT_FOUND) {
//                hideProgress();
//                mEmptyView.setVisibility(View.VISIBLE);
//            } else {
//                super.onError(exception, dataSourceRequest);
//            }
//        }
    }

    @Override
    public CursorModel.CursorModelCreator<MoviesListCursor> getCursorModelCreator() {
        return MoviesListCursor.CREATOR;
    }

    @Override
    public Uri getUri() {
        return ModelContract.getUri(MovieItemEntity.class);
    }

    @Override
    public String getUrl() {
        return ApiTMDB.getNowPlayingGet()+"?api_key=f413bc4bacac8dff174a909f8ef535ae&page=1";
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
}
