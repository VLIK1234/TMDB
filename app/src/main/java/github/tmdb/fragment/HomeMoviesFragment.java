package github.tmdb.fragment;

import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import by.istin.android.xcore.fragment.collection.RecyclerViewFragment;
import by.istin.android.xcore.model.CursorModel;
import by.istin.android.xcore.provider.ModelContract;
import by.istin.android.xcore.source.DataSourceRequest;
import github.tmdb.R;
import github.tmdb.adapter.FilmAdapter;
import github.tmdb.adapter.HomeMoviesAdapter;
import github.tmdb.api.ApiTMDB;
import github.tmdb.app.MainScreenActivity;
import github.tmdb.database.cursor.MoviesListCursor;
import github.tmdb.database.model.MovieItemEntity;
import github.tmdb.database.processor.MovieEntityProcessor;
import github.tmdb.listener.IClickCallback;

/**
 * @author Ivan Bakach
 * @version on 27.03.2016
 */
public class HomeMoviesFragment extends RecyclerViewFragment<HomeMoviesAdapter.ViewHolder, HomeMoviesAdapter, MoviesListCursor> implements FilmAdapter.ITouch, IClickCallback {

    @Override
    public HomeMoviesAdapter createAdapter(FragmentActivity fragmentActivity, MoviesListCursor cursor) {
        return new HomeMoviesAdapter(cursor, this);
    }

    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        return layoutManager;
    }

    @Override
    public void swap(HomeMoviesAdapter castAdapter, MoviesListCursor cursor) {
        castAdapter.swapCursor(cursor);
    }

    @Override
    public int getViewLayout() {
        return R.layout.fragment_recycler_main;
    }

    @Override
    public long getCacheExpiration() {
        return 0L;
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
        return ModelContract.getUri(MovieItemEntity.class);
    }

    @Override
    public String getUrl() {
        return ApiTMDB.getMoviePopular()+"?api_key=f413bc4bacac8dff174a909f8ef535ae";
    }

    @Override
    public String getProcessorKey() {
        return MovieEntityProcessor.APP_SERVICE_KEY;
    }

    @Override
    public String getOrder() {
        return null;
    }

    @Override
    public void touchAction(long idItem) {
        ((MainScreenActivity) getActivity()).setCurrentFragment(MovieDetailFragment.newInstance(idItem), true);
    }

    @Override
    public void onClickCallback(View view) {
        long personId = (long) view.getTag();
        ((MainScreenActivity)getActivity()).setCurrentFragment(PersonFragment.newInstance(personId), true);
//        Intent intent = new Intent(getActivity(), PersonActivity.class);
//        intent.putExtra(PersonFragment.PERSON_ID, personId);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
    }
}