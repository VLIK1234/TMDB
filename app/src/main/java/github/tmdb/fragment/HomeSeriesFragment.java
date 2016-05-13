package github.tmdb.fragment;

import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import by.istin.android.xcore.fragment.collection.RecyclerViewFragment;
import by.istin.android.xcore.model.CursorModel;
import by.istin.android.xcore.provider.ModelContract;
import github.tmdb.R;
import github.tmdb.adapter.HomeSeriesAdapter;
import github.tmdb.adapter.SeriesAdapter;
import github.tmdb.api.ApiTMDB;
import github.tmdb.app.MainScreenActivity;
import github.tmdb.database.cursor.SeriesCursor;
import github.tmdb.database.model.Series;
import github.tmdb.database.processor.SeriesProcessor;

/**
 * @author Ivan Bakach
 * @version on 27.03.2016
 */
public class HomeSeriesFragment extends RecyclerViewFragment<HomeSeriesAdapter.ViewHolder, HomeSeriesAdapter, SeriesCursor> implements SeriesAdapter.ITouch {

    @Override
    public HomeSeriesAdapter createAdapter(FragmentActivity fragmentActivity, SeriesCursor cursor) {
        return new HomeSeriesAdapter(cursor, this);
    }

    @Override
    public void swap(HomeSeriesAdapter homeSeriesAdapter, SeriesCursor cursor) {
        homeSeriesAdapter.swapCursor(cursor);

    }

    @Override
    public CursorModel.CursorModelCreator<SeriesCursor> getCursorModelCreator() {
        return SeriesCursor.CREATOR;
    }

    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        return layoutManager;
    }

    @Override
    public int getViewLayout() {
        return R.layout.fragment_recycler_main;
    }

    @Override
    public Uri getUri() {
        return ModelContract.getUri(Series.class);
    }

    @Override
    public String getUrl() {
        return ApiTMDB.getTvOnTheAir() + "?"+"api_key="+ApiTMDB.API_KEY;
    }

    @Override
    public String getProcessorKey() {
        return SeriesProcessor.APP_SERVICE_KEY;
    }

    @Override
    public void touchAction(long idItem) {
        ((MainScreenActivity) getActivity()).setCurrentFragment(SeriesDetailFragment.newInstance(idItem), true);
    }
}