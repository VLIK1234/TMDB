package github.tmdb.fragment;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import github.tmdb.core.cursor.MoviesDetailCursor;
import github.tmdb.core.model.Genre;
import github.tmdb.core.model.MovieDetailEntity;
import github.tmdb.core.processor.MovieDetailProcessor;
import github.tmdb.utils.BitmapDisplayOptions;
import github.tmdb.utils.TextUtilsImpl;
import github.tmdb.utils.UIUtil;

/**
 * @author Ivan Bakach
 * @version on 03.01.2016
 */
public class MovieDetailFragment extends XFragment<CursorModel> {

    private static class ViewHolder {
        LinearLayout root;
        TextView title;
        TextView date;
        TextView genres;
        TextView runtime;
        TextView rating;
        TextView ratingText;
        TextView tagline;
        TextView overview;
        TextView castLabel;
        TextView crewLabel;
        ImageView poster;
        ImageView backdrop;
        Button trailerButton;
        Button postButton;
        RecyclerView castList;
        RecyclerView crewList;
        ProgressBar mProgressBar;
        ScrollView mScrollView;
    }

    private static final String TAG = "MovieDetailFragment";
    public static final String ID_MOVIE_KEY = "ID_MOVIE";
    public static final int BACKGROUND_ROOT_ALPHA = 200;

    private long idMovie;
    private ViewHolder holder = new ViewHolder();

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
        View view = inflater.inflate(R.layout.fragment_detail, null);
        holder.root = (LinearLayout) view.findViewById(R.id.ll_root);
        holder.poster = (ImageView) view.findViewById(R.id.poster);
        holder.backdrop = (ImageView) view.findViewById(R.id.backdrop);
        holder.title = (TextView) view.findViewById(R.id.title);
        holder.date = (TextView) view.findViewById(R.id.date);
        holder.genres = (TextView) view.findViewById(R.id.genres);
        holder.runtime = (TextView) view.findViewById(R.id.runtime);
        holder.rating = (TextView) view.findViewById(R.id.rating_pic);
        holder.ratingText = (TextView) view.findViewById(R.id.rating_pic_text);
        holder.tagline = (TextView) view.findViewById(R.id.tagline);
        holder.overview = (TextView) view.findViewById(R.id.overview);
        return view;
    }

    @Override
    public int getViewLayout() {
        return R.layout.fragment_detail;
    }

    @Override
    public Uri getUri() {
//        ContentUtils.getEntitiesFromSQL()
        return ModelContract.getSQLQueryUri(MoviesDetailCursor.getDetailSqlRequest(idMovie), ModelContract.getUri(MovieDetailEntity.class));
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
        if (cursor.getCount() > 0) {
            holder.title.setText(CursorUtils.getString(MovieDetailEntity.TITLE, cursor));
            holder.date.setText(CursorUtils.getString(MovieDetailEntity.RELEASE_DATE, cursor));
            holder.runtime.setText(String.format("%1$d %2$s", CursorUtils.getInt(MovieDetailEntity.RUNTIME, cursor), getString(R.string.min)));

            String voteAverage = String.valueOf(CursorUtils.getDouble(MovieDetailEntity.VOTE_AVERAGE, cursor));
            holder.rating.setText(voteAverage);
            String voteCount = String.valueOf(CursorUtils.getInt(MovieDetailEntity.VOTE_COUNT, cursor));
            SpannableStringBuilder ratingBuilder = new SpannableStringBuilder();
            ratingBuilder
                    .append(TextUtilsImpl.setBold(getString(R.string.rating)))
                    .append(String.format(getString(R.string.rating_template), voteAverage, voteCount));
            holder.ratingText.setText(ratingBuilder);

            String tagline = CursorUtils.getString(MovieDetailEntity.TAGLINE, cursor);
            if (!StringUtil.isEmpty(tagline)) {
                SpannableStringBuilder taglineBuilder = new SpannableStringBuilder();
                taglineBuilder
                        .append(TextUtilsImpl.setBold(getString(R.string.tagline)))
                        .append(TextUtilsImpl.lineBreak())
                        .append(tagline);
                holder.tagline.setText(taglineBuilder);
            }
            holder.overview.setText(CursorUtils.getString(MovieDetailEntity.OVERVIEW, cursor));

            final String urlBackdrop = ApiTMDB.getImagePath(ApiTMDB.POSTER_1000X1500_BACKDROP_1000X563, CursorUtils.getString(MovieDetailEntity.BACKDROP_PATH, cursor));
            ImageLoader.getInstance().displayImage(urlBackdrop, holder.backdrop, BitmapDisplayOptions.PORTRAIT_BITMAP_DISPLAY_OPTIONS);
            final String urlPoster = ApiTMDB.getImagePath(ApiTMDB.POSTER_1000X1500_BACKDROP_1000X563, CursorUtils.getString(MovieDetailEntity.POSTER_PATH, cursor));
            ImageLoader.getInstance().displayImage(urlPoster, holder.poster, BitmapDisplayOptions.IMAGE_OPTIONS_EMPTY_PH, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    if (loadedImage != null) {
                        Palette.generateAsync(loadedImage, new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(final Palette palette) {
                                if (palette != null) {
                                    if (palette.getDarkMutedColor() != null && palette.getLightMutedColor() != null &&
                                            palette.getMutedColor() != null) {
                                        holder.root.setBackgroundColor(palette.getDarkMutedColor().getRgb());
                                        Drawable background = holder.root.getBackground();
                                        background.setAlpha(BACKGROUND_ROOT_ALPHA);
                                        UIUtil.setBackgroundCompact(holder.root, background);
                                        setPrimaryTextColor(palette.getLightMutedColor().getRgb());
                                        setSecondTextColor(palette.getMutedColor().getRgb());
                                    } else {
                                        holder.root.setBackgroundColor(palette.getDarkVibrantColor().getRgb());
                                        Drawable background = holder.root.getBackground();
                                        background.setAlpha(BACKGROUND_ROOT_ALPHA);
                                        UIUtil.setBackgroundCompact(holder.root, background);
                                        setPrimaryTextColor(palette.getLightVibrantColor().getRgb());
                                        setSecondTextColor(palette.getVibrantColor().getRgb());
                                    }
                                }
                            }
                        });
                    }
                }
            });

            StringBuilder genresLine = new StringBuilder();
            do {
                genresLine.append(String.format("%s%s", CursorUtils.getString(Genre.NAME, cursor), (!cursor.isLast() ? " | " : StringUtil.EMPTY)));
            } while (cursor.moveToNext());
            holder.genres.setText(genresLine.toString());
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
        Log.d(TAG, "onLoaderReset: ");
    }

    private void setPrimaryTextColor(int rgbColor) {
        holder.title.setTextColor(rgbColor);
        holder.overview.setTextColor(rgbColor);
        holder.runtime.setTextColor(rgbColor);
//        holder.rating.setTextColor(rgbColor);
        holder.tagline.setTextColor(rgbColor);
        holder.ratingText.setTextColor(rgbColor);
    }

    private void setSecondTextColor(int rgbColor) {
        holder.date.setTextColor(rgbColor);
        holder.genres.setTextColor(rgbColor);
    }
}
