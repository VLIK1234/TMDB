package github.tmdb.fragment;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import by.istin.android.xcore.fragment.XFragment;
import by.istin.android.xcore.model.CursorModel;
import by.istin.android.xcore.provider.ModelContract;
import by.istin.android.xcore.utils.CursorUtils;
import github.tmdb.R;
import github.tmdb.api.ApiTMDB;
import github.tmdb.database.cursor.SeriesDetailCursor;
import github.tmdb.database.model.Genre;
import github.tmdb.database.model.SeriesDetailEntity;
import github.tmdb.database.processor.SeriesDetailProcessor;
import github.tmdb.utils.BitmapDisplayOptions;
import github.tmdb.utils.TextUtilsImpl;

/**
 * @author Ivan Bakach
 * @version on 03.01.2016
 */
public class SeriesDetailFragment extends XFragment<CursorModel> {

    private static final String TAG = "SeriesDetailFragment";
    public static final String ID_KEY = "ID_SERIES";
    public static final float BACKGROUND_ROOT_ALPHA = 0.8f;

    private long idSeries;

    private View mLeak;
    private ImageView mPoster;
    private TextView mTitle;
    private TextView mDate;
    private TextView mGenres;
//    private TextView mRuntime;
    private TextView mRatingText;
//    private TextView mTagline;
    private TextView mOverview;
//    private FrameLayout mCastContainer;
//    private Fragment mCastFragment;
    private RelativeLayout mRoot;

    public static Fragment newInstance(long id) {
        SeriesDetailFragment fragmentPart = new SeriesDetailFragment();
        Bundle args = new Bundle();
        args.putLong(ID_KEY, id);
        fragmentPart.setArguments(args);
        return fragmentPart;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idSeries = getArguments().getLong(ID_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLeak = inflater.inflate(R.layout.fragment_detail, container, false);
        mRoot = (RelativeLayout) mLeak.findViewById(R.id.root);
        mPoster = (ImageView) mLeak.findViewById(R.id.poster);
        mTitle = (TextView) mLeak.findViewById(R.id.title);
        mDate = (TextView) mLeak.findViewById(R.id.date);
        mGenres = (TextView) mLeak.findViewById(R.id.genres);
//        mRuntime = (TextView) mLeak.findViewById(R.id.runtime);
        mRatingText = (TextView) mLeak.findViewById(R.id.rating_pic_text);
//        mTagline = (TextView) mLeak.findViewById(R.id.tagline);
        mOverview = (TextView) mLeak.findViewById(R.id.overview);
//        mCastContainer = (FrameLayout) mLeak.findViewById(R.id.cast_container);
        return mLeak;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        FragmentManager fragmentManager = getFragmentManager();
//        mCastFragment = CastFragment.newInstance(idSeries);
//        fragmentManager.beginTransaction()
//                .add(R.id.cast_container, mCastFragment)
//                .commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        if (mCastFragment != null) {
//            getFragmentManager().beginTransaction().remove(mCastFragment).commit();
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mLeak = null;
    }

    @Override
    public int getViewLayout() {
        return R.layout.fragment_detail;
    }

    @Override
    public Uri getUri() {
        return ModelContract.getSQLQueryUri(SeriesDetailCursor.getDetailSqlRequest(idSeries), ModelContract.getUri(SeriesDetailEntity.class));
    }

    @Override
    public String getUrl() {
        return ApiTMDB.getTVDetail(idSeries) + "?api_key=" + ApiTMDB.API_KEY;
    }

    @Override
    public String getProcessorKey() {
        return SeriesDetailProcessor.SYSTEM_SERVICE_KEY;
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
        return new String[]{SeriesDetailEntity.NAME, SeriesDetailEntity.OVERVIEW, SeriesDetailEntity.FIRST_AIR_DATE};
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
//        mRuntime.setTextColor(rgbColor);
//        holder.rating.setTextColor(rgbColor);
//        mTagline.setTextColor(rgbColor);
        mRatingText.setTextColor(rgbColor);
    }

    private void setSecondTextColor(int rgbColor) {
        mDate.setTextColor(rgbColor);
        mGenres.setTextColor(rgbColor);
    }

    private void setTextInfo(Cursor cursor) {
        mGenres.setText(CursorUtils.getString(Genre.GENRE_NAME, cursor));
//        mRuntime.setText(String.format(getString(R.string.min), CursorUtils.getInt(SeriesDetailEntity.RUNTIME, cursor)));

        String voteAverage = String.valueOf(CursorUtils.getDouble(SeriesDetailEntity.VOTE_AVERAGE, cursor));
        String voteCount = String.valueOf(CursorUtils.getInt(SeriesDetailEntity.VOTE_COUNT, cursor));
        SpannableStringBuilder ratingBuilder = new SpannableStringBuilder();
        ratingBuilder
                .append(TextUtilsImpl.setBold(getString(R.string.rating)))
                .append(String.format(getString(R.string.rating_template), voteAverage, voteCount));
        mRatingText.setText(ratingBuilder);

//        String tagline = CursorUtils.getString(SeriesDetailEntity.TAGLINE, cursor);
//        if (!StringUtil.isEmpty(tagline)) {
//            SpannableStringBuilder taglineBuilder = new SpannableStringBuilder();
//            taglineBuilder
//                    .append(TextUtilsImpl.setBold(getString(R.string.tagline)))
//                    .append(TextUtilsImpl.lineBreak())
//                    .append(tagline);
//            mTagline.setText(taglineBuilder);
//        }
    }

    private void setImages(Cursor cursor) {
//        final String urlBackdrop = ApiTMDB.getImagePath(ApiTMDB.POSTER_1000X1500_BACKDROP_1000X563, CursorUtils.getString(SeriesDetailEntity.BACKDROP_PATH, cursor));
//        ImageLoader.getInstance().displayImage(urlBackdrop, holder.backdrop, BitmapDisplayOptions.PORTRAIT_BITMAP_DISPLAY_OPTIONS);
        final String urlPoster = ApiTMDB.getImagePath(ApiTMDB.POSTER_1000X1500_BACKDROP_1000X563, CursorUtils.getString(SeriesDetailEntity.POSTER_PATH, cursor));
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
                                } else if (palette.getDarkVibrantColor() != null && palette.getLightVibrantColor() != null && palette.getVibrantColor() != null) {
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
        setPrimaryTextColor(primaryTextColor);
        setSecondTextColor(secondaryTextColor);
    }
}