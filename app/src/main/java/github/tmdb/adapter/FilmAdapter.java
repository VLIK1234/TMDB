package github.tmdb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import github.tmdb.R;
import github.tmdb.api.ApiTMDB;
import github.tmdb.bo.Film;

/**
 * @author Ivan Bakach
 * @version on 09.08.2015
 */
public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.ViewHolder> {

    public interface ITouch {
        void touchAction(long idItem);
    }

    private ArrayList<Film> mFilmList = new ArrayList<>();
    private ITouch mITouch;
    private Context mContext;

    public FilmAdapter(Context context, ArrayList<Film> filmList, ITouch iTouch) {
        mContext = context;
        mFilmList = filmList;
        mITouch = iTouch;
    }

    @Override
    public FilmAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);
        return new ViewHolder(v, mFilmList, mITouch);
    }

    @Override
    public void onBindViewHolder(final FilmAdapter.ViewHolder holder, int position) {
        Film film = mFilmList.get(position);
        holder.title.setText(film.getTitle());
        holder.date.setText(film.getReleaseDate());
        holder.rating.setRating(Float.valueOf(film.getVoteAverage()));
        holder.ratingText.setText(film.getVoteAverage() + "/10" + " (" + film.getVoteCount() + ")");

        final String url = film.getPosterPath(ApiTMDB.SizePoster.w185);
        holder.poster.post(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(url)) {
                    Picasso.with(mContext).load(url).into(holder.poster);
//                    ImageLoader.getInstance().displayImage(url, holder.poster, mOptions);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFilmList.size();
    }

    public void addAll(ArrayList<Film> filmArrayList) {
        mFilmList.addAll(filmArrayList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView title;
        private final TextView date;
        private final RatingBar rating;
        private final TextView ratingText;
        private final ImageView poster;
        private final ArrayList<Film> mListFilm;
        private final ITouch mITouch;

        public ViewHolder(View convertView, ArrayList<Film> listFilm, ITouch iTouch) {
            super(convertView);
            convertView.setOnClickListener(this);
            title = (TextView) convertView.findViewById(R.id.title);
            date = (TextView) convertView.findViewById(R.id.date);
            rating = (RatingBar) convertView.findViewById(R.id.rating);
            ratingText = (TextView) convertView.findViewById(R.id.rating_text);
            poster = (ImageView) convertView.findViewById(R.id.poster);
            mListFilm = listFilm;
            mITouch = iTouch;
        }

        @Override
        public void onClick(View v) {
            Log.d("TAG", "Click");
            Film item = mListFilm.get(getAdapterPosition());
            long selectItemID = item.getId();
            mITouch.touchAction(selectItemID);
        }
    }
}
