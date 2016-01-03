package github.tmdb.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import by.istin.android.xcore.db.impl.DBHelper;
import by.istin.android.xcore.fragment.XFragment;
import by.istin.android.xcore.model.CursorModel;
import by.istin.android.xcore.provider.ModelContract;
import github.tmdb.R;
import github.tmdb.api.ApiTMDB;
import github.tmdb.core.cursor.MoviesDetailCursor;
import github.tmdb.core.model.MovieDetailEntity;
import github.tmdb.core.model.MovieItemEntity;
import github.tmdb.core.processor.MovieDetailProcessor;
import github.tmdb.core.processor.MovieEntityProcessor;

/**
 * @author Ivan Bakach
 * @version on 03.01.2016
 */
public class MovieDetailFragment extends XFragment<CursorModel> {

    private static final String TAG = "MovieDetailFragment";

    private long idMovie = 1955L;
    @Override
    public int getViewLayout() {
        return R.layout.fragment_detail;
    }

    @Override
    public Uri getUri() {
//        ContentUtils.getEntitiesFromSQL()
        return ModelContract.getSQLQueryUri(MoviesDetailCursor.getDetailSqlRequest(idMovie), null);
//        return ModelContract.getUri(MovieDetailEntity.class);
    }

    @Override
    public String getUrl() {
        return ApiTMDB.getMovie(idMovie) + "?api_key=f413bc4bacac8dff174a909f8ef535ae";
    }

    @Override
    public String getProcessorKey() {
        return MovieDetailProcessor.SYSTEM_SERVICE_KEY;
    }

    @Override
    protected void onLoadFinished(Cursor cursor) {
        Log.d(TAG, "onLoadFinished() returned: " + cursor.getCount());
    }

    @Override
    protected String[] getAdapterColumns() {
        return new String[]{MovieDetailEntity.TITLE, MovieDetailEntity.OVERVIEW, MovieDetailEntity.RELEASE_DATE};
    }

    @Override
    protected int[] getAdapterControlIds() {
        return new int[]{R.id.title, R.id.overview, R.id.date};
    }

    @Override
    protected void onLoaderReset() {
        Log.d(TAG, "onLoaderReset: ");
    }

}
