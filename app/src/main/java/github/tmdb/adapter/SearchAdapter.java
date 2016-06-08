package github.tmdb.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private SearchCursor mCursor;

    public SearchAdapter(SearchCursor cursor) {
        mCursor = cursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_series, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        final String label = String.valueOf(mCursor.getId()) + " " + mCursor.getMediaType();
        holder.mLabel.setText(label);
        final String mediaType = mCursor.getMediaType();
        ImageLoader imageLoader = ImageLoader.getInstance();
        switch (mediaType) {
            case ApiTMDB.MEDIA_TYPE_TV:
                imageLoader.displayImage(
                        mCursor.getPosterPath(ApiTMDB.POSTER_185X278_BACKDROP_185X104),
                        holder.mPoster,
                        BitmapDisplayOptions.PORTRAIT_BITMAP_DISPLAY_OPTIONS);
                break;
            case ApiTMDB.MEDIA_TYPE_MOVIE:
                imageLoader.displayImage(
                        mCursor.getPosterPath(ApiTMDB.POSTER_185X278_BACKDROP_185X104),
                        holder.mPoster,
                        BitmapDisplayOptions.PORTRAIT_BITMAP_DISPLAY_OPTIONS);
                break;
            case ApiTMDB.MEDIA_TYPE_PERSON:
                imageLoader.displayImage(
                        mCursor.getProfilePath(ApiTMDB.POSTER_185X278_BACKDROP_185X104),
                        holder.mPoster,
                        BitmapDisplayOptions.PORTRAIT_BITMAP_DISPLAY_OPTIONS);
                break;
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mLabel;
        public final ImageView mPoster;
        public ViewHolder(View itemView) {
            super(itemView);
            mLabel = (TextView) itemView.findViewById(R.id.label);
            mPoster = (ImageView) itemView.findViewById(R.id.poster);
        }
    }
}
