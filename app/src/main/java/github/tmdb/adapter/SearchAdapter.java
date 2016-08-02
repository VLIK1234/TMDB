package github.tmdb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import by.istin.android.xcore.utils.CursorUtils;
import github.tmdb.R;
import github.tmdb.api.ApiTMDB;
import github.tmdb.database.cursor.SearchCursor;
import github.tmdb.utils.BitmapDisplayOptions;

/**
 * @author Ivan Bakach
 * @version on 04.06.2016
 */
public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int MOVIE_VIEW_TYPE = 0;
    public static final int PERSON_VIEW_TYPE = 1;
    public static final int SERIES_VIEW_TYPE = 2;

    private SearchCursor mCursor;

    public SearchAdapter(SearchCursor cursor) {
        mCursor = cursor;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        switch (viewType) {
            case MOVIE_VIEW_TYPE:
                return new SeriesOrMovieViewHolder(LayoutInflater.from(context)
                        .inflate(R.layout.adapter_search_series, parent, false));
            case SERIES_VIEW_TYPE:
                return new SeriesOrMovieViewHolder(LayoutInflater.from(context)
                        .inflate(R.layout.adapter_search_series, parent, false));
            case PERSON_VIEW_TYPE:
                return new PersonViewHolder(LayoutInflater.from(context)
                        .inflate(R.layout.adapter_search_person, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        final String label = String.valueOf(mCursor.getId()) + " " + mCursor.getMediaType();
        final String mediaType = mCursor.getMediaType();
        ImageLoader imageLoader = ImageLoader.getInstance();

        SeriesOrMovieViewHolder seriesOrMovieViewHolder = null;
        if (holder instanceof SeriesOrMovieViewHolder) {
            seriesOrMovieViewHolder = (SeriesOrMovieViewHolder) holder;
        }
        switch (mediaType) {
            case ApiTMDB.MEDIA_TYPE_TV:
                if (seriesOrMovieViewHolder != null) {
                    seriesOrMovieViewHolder.mTitle.setText(mCursor.getName());
                    seriesOrMovieViewHolder.mSeriesOverview.setText(mCursor.getOverview());
                    imageLoader.displayImage(
                            mCursor.getPosterPath(ApiTMDB.POSTER_185X278_BACKDROP_185X104),
                            seriesOrMovieViewHolder.mPoster,
                            BitmapDisplayOptions.PORTRAIT_BITMAP_DISPLAY_OPTIONS);
                }
                break;
            case ApiTMDB.MEDIA_TYPE_MOVIE:
                if (seriesOrMovieViewHolder != null) {
                    seriesOrMovieViewHolder.mTitle.setText(mCursor.getMovieTitle());
                    seriesOrMovieViewHolder.mSeriesOverview.setText(mCursor.getOverview());
                    imageLoader.displayImage(
                            mCursor.getPosterPath(ApiTMDB.POSTER_185X278_BACKDROP_185X104),
                            seriesOrMovieViewHolder.mPoster,
                            BitmapDisplayOptions.PORTRAIT_BITMAP_DISPLAY_OPTIONS);
                }
                break;
            case ApiTMDB.MEDIA_TYPE_PERSON:
                PersonViewHolder personViewHolder = null;
                if (holder instanceof PersonViewHolder) {
                    personViewHolder = (PersonViewHolder) holder;
                }
                if (personViewHolder != null) {
                    personViewHolder.mName.setText(mCursor.getName());
                    imageLoader.displayImage(
                            mCursor.getProfilePath(ApiTMDB.POSTER_185X278_BACKDROP_185X104),
                            personViewHolder.mPoster,
                            BitmapDisplayOptions.PORTRAIT_BITMAP_DISPLAY_OPTIONS);
                }
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        mCursor.moveToPosition(position);
        final String mediaType = mCursor.getMediaType();
        switch (mediaType) {
            case ApiTMDB.MEDIA_TYPE_TV:
                return SERIES_VIEW_TYPE;
            case ApiTMDB.MEDIA_TYPE_MOVIE:
                return MOVIE_VIEW_TYPE;
            case ApiTMDB.MEDIA_TYPE_PERSON:
                return PERSON_VIEW_TYPE;
            default:
                return super.getItemViewType(position);
        }
    }

    @Override
    public int getItemCount() {
        return mCursor.size();
    }

    public void swapCursor(SearchCursor cursor) {
        CursorUtils.close(mCursor);
        mCursor = cursor;
        notifyDataSetChanged();
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {
        public final TextView mName;
        public final ImageView mPoster;
        public final FrameLayout mPersonContainer;
        public PersonViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.label);
            mPoster = (ImageView) itemView.findViewById(R.id.poster);
            mPersonContainer = (FrameLayout) itemView.findViewById(R.id.person_content);
        }
    }

    public class SeriesOrMovieViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTitle;
        public final TextView mSeriesOverview;
        public final ImageView mPoster;

        public SeriesOrMovieViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.series_name);
            mSeriesOverview = (TextView) itemView.findViewById(R.id.series_overview);
            mPoster = (ImageView) itemView.findViewById(R.id.poster);
        }
    }
}
