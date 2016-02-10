package github.tmdb.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import by.istin.android.xcore.model.CursorModel;
import by.istin.android.xcore.utils.CursorUtils;
import github.tmdb.R;
import github.tmdb.api.ApiTMDB;
import github.tmdb.core.cursor.MoviesListCursor;
import github.tmdb.core.model.MovieItemEntity;
import github.tmdb.utils.BitmapDisplayOptions;

/**
 * @author Ivan Bakach
 * @version on 09.08.2015
 */
public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.ViewHolder> {

    public interface ITouch {
        void touchAction(long idItem);
    }

    private final ITouch mITouch;
    private MoviesListCursor mCursor;
    private ArrayList<Long> mIdLists = new ArrayList<>();

    public FilmAdapter(MoviesListCursor moviesListCursor, ITouch iTouch) {
        mCursor = moviesListCursor;
        mITouch = iTouch;
    }

    @Override
    public FilmAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final FilmAdapter.ViewHolder holder, final int position) {
        CursorModel cursor = mCursor.get(position);

        holder.mainView.setTag(CursorUtils.getLong(MovieItemEntity.EXTERNAL_ID, cursor));
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mITouch.touchAction((long) v.getTag());
            }
        });
        holder.title.setText(CursorUtils.getString(MovieItemEntity.TITLE, cursor));
        holder.date.setText(CursorUtils.getString(MovieItemEntity.RELEASE_DATE, cursor));
//        holder.rating.setRating(CursorUtils.getFloat(MovieItemEntity.VOTE_AVERAGE, cursor));
        holder.ratingText.setText(String.format("%s/10 (%s)",
                CursorUtils.getFloat(MovieItemEntity.VOTE_AVERAGE, cursor), CursorUtils.getInt(MovieItemEntity.VOTE_COUNT, cursor)));

//        ImageLoader.getInstance().
//                displayImage(ApiTMDB.getImagePath(ApiTMDB.POSTER_500X750_BACKDROP_500X281,
//                        CursorUtils.getString(MovieItemEntity.BACKDROP_PATH, cursor)), holder.backdrop, BitmapDisplayOptions.PORTRAIT_BITMAP_DISPLAY_OPTIONS);
        ImageLoader.getInstance().
                displayImage(ApiTMDB.getImagePath(ApiTMDB.POSTER_92X138_BACKDROP_92X52,
                        CursorUtils.getString(MovieItemEntity.POSTER_PATH, cursor)), holder.poster, BitmapDisplayOptions.PORTRAIT_BITMAP_DISPLAY_OPTIONS);
    }

    @Override
    public int getItemCount() {
        return mCursor.size();
    }

//    public void addAll(ArrayList<Film> filmArrayList) {
//        mFilmList.addAll(filmArrayList);
//    }

    public void swapCursor(MoviesListCursor moviesListCursor) {
        CursorUtils.close(mCursor);
        mCursor = moviesListCursor;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView mainView;
        private final TextView title;
        private final TextView date;
        //        private final RatingBar rating;
        private final TextView ratingText;
        //        private final ImageView backdrop;
        private final ImageView poster;

        public ViewHolder(View convertView) {
            super(convertView);
            mainView = (CardView) convertView.findViewById(R.id.card_view);
            title = (TextView) convertView.findViewById(R.id.title);
            date = (TextView) convertView.findViewById(R.id.date);
//            rating = (RatingBar) convertView.findViewById(R.id.rating);
            ratingText = (TextView) convertView.findViewById(R.id.rating_text);
//            backdrop = (ImageView) convertView.findViewById(R.id.backdrop);
            poster = (ImageView) convertView.findViewById(R.id.poster);
        }

    }
}
