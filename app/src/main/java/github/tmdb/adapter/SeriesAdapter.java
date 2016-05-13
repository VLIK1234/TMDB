package github.tmdb.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
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
import github.tmdb.database.cursor.SeriesCursor;
import github.tmdb.database.cursor.SeriesCursor;
import github.tmdb.utils.BitmapDisplayOptions;

/**
 * @author Ivan Bakach
 * @version on 09.08.2015
 */
public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.ViewHolder> {

    public interface ITouch {
        void touchAction(long idItem);
    }

    private final ITouch mITouch;
    private SeriesCursor mCursor;

    public SeriesAdapter(SeriesCursor SeriesCursor, ITouch iTouch) {
        mCursor = SeriesCursor;
        mITouch = iTouch;
    }

    @Override
    public SeriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final SeriesAdapter.ViewHolder holder, final int position) {
        final SeriesCursor cursor = (SeriesCursor)mCursor.get(position);
        final Context context = holder.itemView.getContext();

        holder.mainView.setTag(cursor.getId());
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mITouch.touchAction((long) v.getTag());
            }
        });
        holder.title.setText(cursor.getName());
        holder.date.setText(cursor.getFirstAirDate());
        holder.ratingText.setText(String.format(context.getString(R.string.rating_text_template), cursor.getVoteAverage(), cursor.getVoteCount()));

        ImageLoader.getInstance().
                displayImage(cursor.getBackdropPath(ApiTMDB.POSTER_780X1170_BACKDROP_780X439), holder.backdrop, BitmapDisplayOptions.PORTRAIT_BITMAP_DISPLAY_OPTIONS);
        ImageLoader.getInstance().
                displayImage(cursor.getPosterPath(ApiTMDB.POSTER_342X513_BACKDROP_342X192), holder.poster, BitmapDisplayOptions.PORTRAIT_BITMAP_DISPLAY_OPTIONS);
    }

    @Override
    public int getItemCount() {
        return mCursor.size();
    }

    public void swapCursor(SeriesCursor SeriesCursor) {
        CursorUtils.close(mCursor);
        mCursor = SeriesCursor;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView mainView;
        private final TextView title;
        private final TextView date;
        //        private final RatingBar rating;
        private final TextView ratingText;
        private final ImageView backdrop;
        private final ImageView poster;

        public ViewHolder(View convertView) {
            super(convertView);
            mainView = (CardView) convertView.findViewById(R.id.card_view);
            title = (TextView) convertView.findViewById(R.id.tv_title);
            date = (TextView) convertView.findViewById(R.id.date);
//            rating = (RatingBar) convertView.findViewById(R.id.rating);
            ratingText = (TextView) convertView.findViewById(R.id.rating_text);
            backdrop = (ImageView) convertView.findViewById(R.id.backdrop);
            poster = (ImageView) convertView.findViewById(R.id.poster);
        }

    }
}
