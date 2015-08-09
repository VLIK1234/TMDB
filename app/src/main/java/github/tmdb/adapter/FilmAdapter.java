package github.tmdb.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import github.tmdb.CoreApplication;
import github.tmdb.R;
import github.tmdb.api.ApiTMDB;
import github.tmdb.app.DetailsActivity;
import github.tmdb.bo.DescriptionOfTheFilm;
import github.tmdb.bo.Film;

/**
 * @author Ivan Bakach
 * @version on 09.08.2015
 */
public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.ViewHolder>  {

    private ArrayList<Film> mFilmList = new ArrayList<>();

    public FilmAdapter(ArrayList<Film> filmList){
        mFilmList = filmList;
    }

    @Override
    public FilmAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);
        return new ViewHolder(v, mFilmList);
    }

    @Override
    public void onBindViewHolder(FilmAdapter.ViewHolder holder, int position) {
        Film film = mFilmList.get(position);
        holder.title.setText(film.getTitle());
        holder.date.setText(film.getReleaseDate());
        holder.rating.setRating(Float.valueOf(film.getVoteAverage()));
        holder.ratingText.setText(film.getVoteAverage() + "/10" + " (" + film.getVoteCount() + ")");

        final String url = film.getPosterPath(ApiTMDB.SizePoster.w185);
        ImageLoader.getInstance().displayImage(url, holder.poster);
    }

    @Override
    public int getItemCount() {
        return mFilmList.size();
    }

    public void addAll(ArrayList<Film> filmArrayList){
        mFilmList.addAll(filmArrayList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView title;
        private final TextView date;
        private final RatingBar rating;
        private final TextView ratingText;
        private final ImageView poster;
        private final ArrayList<Film> mListFilm;

        public ViewHolder(View convertView, ArrayList<Film> listFilm) {
            super(convertView);
            title = (TextView) convertView.findViewById(R.id.title);
            date = (TextView) convertView.findViewById(R.id.date);
            rating = (RatingBar) convertView.findViewById(R.id.rating);
            ratingText = (TextView) convertView.findViewById(R.id.rating_text);
            poster = (ImageView) convertView.findViewById(R.id.poster);
            mListFilm = listFilm;
        }

        @Override
        public void onClick(View v) {
            Log.d("TAG", "Click");
            Film item = (Film) mListFilm.get(getAdapterPosition());
            long selectItemID = item.getId();

            DescriptionOfTheFilm description = new DescriptionOfTheFilm(ApiTMDB.getMovie(selectItemID));
            Intent intent = new Intent(CoreApplication.getAppContext(), DetailsActivity.class);
            intent.putExtra(DescriptionOfTheFilm.class.getCanonicalName(), description);
            CoreApplication.getAppContext().startActivity(intent);
        }
    }
}
