package github.tmdb.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import github.tmdb.R;
import github.tmdb.api.ApiTMDB;
import github.tmdb.bo.Film;
import github.tmdb.image.ImageLoader;

/**
 * Created by ASUS on 01.02.2015.
 */
public class CustomAdapter extends ArrayAdapter<Film> {

    private Context context;
    private ImageLoader imageLoader;

    static class ViewHolder {
        TextView title;
        TextView date;
        RatingBar rating;
        TextView ratingText;

    }
    private ViewHolder holder = new ViewHolder();

    public CustomAdapter(Context context, int resource, int textViewResourceId, List<Film> objects, ImageLoader imageLoader) {
        super(context, resource, textViewResourceId, objects);
        this.context = context;
        this.imageLoader = imageLoader;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.adapter_item, null);
        }
        Film item = getItem(position);
        holder.title = (TextView) convertView.findViewById(R.id.title);
        holder.date = (TextView) convertView.findViewById(R.id.date);
        holder.rating = (RatingBar) convertView.findViewById(R.id.rating);
        holder.ratingText = (TextView) convertView.findViewById(R.id.rating_text);

        holder.title.setText(item.getTitle());
        holder.date.setText(item.getReleaseDate());
        holder.rating.setRating(Float.valueOf(item.getVoteAverage()));
        holder.ratingText.setText(item.getVoteAverage() + "/10" + " (" + item.getVoteCount() + ")");

        convertView.setTag(item.getId());
        final ImageView poster = (ImageView) convertView.findViewById(R.id.poster);
        final String url = item.getPosterPath(ApiTMDB.SizePoster.w185);
        imageLoader.loadAndDisplay(url, poster);
        return convertView;
    }
}
