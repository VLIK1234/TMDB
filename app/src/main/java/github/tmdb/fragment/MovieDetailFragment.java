package github.tmdb.fragment;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.graphics.Palette;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import by.istin.android.xcore.fragment.XFragment;
import by.istin.android.xcore.model.CursorModel;
import by.istin.android.xcore.provider.ModelContract;
import by.istin.android.xcore.utils.CursorUtils;
import by.istin.android.xcore.utils.StringUtil;
import github.tmdb.R;
import github.tmdb.api.ApiTMDB;
import github.tmdb.database.cursor.MoviesDetailCursor;
import github.tmdb.database.model.Genre;
import github.tmdb.database.model.MovieDetailEntity;
import github.tmdb.database.processor.MovieDetailProcessor;
import github.tmdb.utils.BitmapDisplayOptions;
import github.tmdb.utils.TextUtilsImpl;
import github.tmdb.utils.UIUtil;

/**
 * @author Ivan Bakach
 * @version on 03.01.2016
 */
public class MovieDetailFragment extends XFragment<CursorModel> {

    private static final String TAG = "MovieDetailFragment";
    public static final String ID_MOVIE_KEY = "ID_MOVIE";
    public static final float BACKGROUND_ROOT_ALPHA = 0.8f;

    private long idMovie;

    private View mLeak;
    private ImageView mPoster;
    private TextView mTitle;
    private TextView mDate;
    private TextView mGenres;
    private TextView mRuntime;
    private TextView mRatingText;
    private TextView mTagline;
    private TextView mOverview;
    private FrameLayout mCastContainer;
    private Fragment mCastFragment;
    private ScrollView mRoot;

    public static Fragment newInstance(long idMovie) {
        MovieDetailFragment fragmentPart = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putLong(ID_MOVIE_KEY, idMovie);
        fragmentPart.setArguments(args);
        return fragmentPart;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idMovie = getArguments().getLong(ID_MOVIE_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        mRoot = (ScrollView) view.findViewById(R.id.root);
        mPoster = (ImageView) view.findViewById(R.id.poster);
        mTitle = (TextView) view.findViewById(R.id.title);
        mDate = (TextView) view.findViewById(R.id.date);
        mGenres = (TextView) view.findViewById(R.id.genres);
        mRuntime = (TextView) view.findViewById(R.id.runtime);
        mRatingText = (TextView) view.findViewById(R.id.rating_pic_text);
        mTagline = (TextView) view.findViewById(R.id.tagline);
        mOverview = (TextView) view.findViewById(R.id.overview);
        mCastContainer = (FrameLayout) view.findViewById(R.id.cast_container);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fragmentManager = getFragmentManager();
        mCastFragment = CastFragment.newInstance(idMovie);
        fragmentManager.beginTransaction()
                .add(R.id.cast_container, mCastFragment)
                .commit();
    }

    @Override
    public int getViewLayout() {
        return R.layout.fragment_detail;
    }

    @Override
    public Uri getUri() {
//        ContentUtils.getEntitiesFromSQL()
        return ModelContract.getSQLQueryUri(MoviesDetailCursor.getDetailSqlRequest(idMovie), ModelContract.getUri(Genre.class));
//        return ModelContract.getUri(MovieDetailEntity.class);
    }

    @Override
    public String getUrl() {
        return ApiTMDB.getMovieDetail(idMovie) + "?"+"api_key=f413bc4bacac8dff174a909f8ef535ae";
    }

    @Override
    public String getProcessorKey() {
        return MovieDetailProcessor.SYSTEM_SERVICE_KEY;
    }

    @Override
    protected void onLoadFinished(Cursor cursor) {
        if (cursor.getCount() > 0) {
            setTextInfo(cursor);
            setImages(cursor);
        }
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
    }

    private void setPrimaryTextColor(int rgbColor) {
        mTitle.setTextColor(rgbColor);
        mOverview.setTextColor(rgbColor);
        mRuntime.setTextColor(rgbColor);
//        holder.rating.setTextColor(rgbColor);
        mTagline.setTextColor(rgbColor);
        mRatingText.setTextColor(rgbColor);
    }

    private void setSecondTextColor(int rgbColor) {
        mDate.setTextColor(rgbColor);
        mGenres.setTextColor(rgbColor);
    }

    private void setTextInfo(Cursor cursor) {
        mGenres.setText(CursorUtils.getString(Genre.GENRE_NAME, cursor));
        mRuntime.setText(String.format(getString(R.string.min), CursorUtils.getInt(MovieDetailEntity.RUNTIME, cursor)));

        String voteAverage = String.valueOf(CursorUtils.getDouble(MovieDetailEntity.VOTE_AVERAGE, cursor));

        String voteCount = String.valueOf(CursorUtils.getInt(MovieDetailEntity.VOTE_COUNT, cursor));
        SpannableStringBuilder ratingBuilder = new SpannableStringBuilder();
        ratingBuilder
                .append(TextUtilsImpl.setBold(getString(R.string.rating)))
                .append(String.format(getString(R.string.rating_template), voteAverage, voteCount));
        mRatingText.setText(ratingBuilder);

        String tagline = CursorUtils.getString(MovieDetailEntity.TAGLINE, cursor);
        if (!StringUtil.isEmpty(tagline)) {
            SpannableStringBuilder taglineBuilder = new SpannableStringBuilder();
            taglineBuilder
                    .append(TextUtilsImpl.setBold(getString(R.string.tagline)))
                    .append(TextUtilsImpl.lineBreak())
                    .append(tagline);
            mTagline.setText(taglineBuilder);
        }
    }

    private void setImages(Cursor cursor) {
//        final String urlBackdrop = ApiTMDB.getImagePath(ApiTMDB.POSTER_1000X1500_BACKDROP_1000X563, CursorUtils.getString(MovieDetailEntity.BACKDROP_PATH, cursor));
//        ImageLoader.getInstance().displayImage(urlBackdrop, holder.backdrop, BitmapDisplayOptions.PORTRAIT_BITMAP_DISPLAY_OPTIONS);
        final String urlPoster = ApiTMDB.getImagePath(ApiTMDB.POSTER_1000X1500_BACKDROP_1000X563, CursorUtils.getString(MovieDetailEntity.POSTER_PATH, cursor));
        ImageLoader.getInstance().displayImage(urlPoster, mPoster, BitmapDisplayOptions.PORTRAIT_BITMAP_DISPLAY_OPTIONS, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (loadedImage != null) {
                    Palette.generateAsync(loadedImage, new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(final Palette palette) {
                            if (palette != null) {
                                if (palette.getDarkMutedColor() != null && palette.getLightMutedColor() != null && palette.getMutedColor() != null) {
                                    setPaletteColor(palette.getDarkMutedColor().getRgb(), palette.getLightMutedColor().getRgb(), palette.getMutedColor().getRgb());
                                } else {
                                    setPaletteColor(palette.getDarkVibrantColor().getRgb(), palette.getLightVibrantColor().getRgb(), palette.getVibrantColor().getRgb());
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private void setPaletteColor(int rootColor, int primaryTextColor, int secondaryTextColor) {
        mRoot.setBackgroundColor(rootColor);
        ((CastFragment)mCastFragment).setRootColor(rootColor);
        setPrimaryTextColor(primaryTextColor);
        setSecondTextColor(secondaryTextColor);
    }
}