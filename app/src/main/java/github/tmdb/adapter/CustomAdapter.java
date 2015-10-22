package github.tmdb.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import github.tmdb.R;
import github.tmdb.api.ApiTMDB;
import github.tmdb.bo.Film;

/**
 @author IvanBakach
 @version on 01.02.2015
 */
public class CustomAdapter extends ArrayAdapter<Film> {

    private Context mContext;

    static class ViewHolder {
        TextView title;
        TextView date;
        RatingBar rating;
        TextView ratingText;

    }
    private ViewHolder holder = new ViewHolder();

    public CustomAdapter(Context context, int resource, int textViewResourceId, List<Film> objects) {
        super(context, resource, textViewResourceId, objects);
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.adapter_item, null);
        }
        Film item = getItem(position);
        holder.title = (TextView) convertView.findViewById(R.id.title);
        holder.date = (TextView) convertView.findViewById(R.id.date);
        holder.rating = (RatingBar) convertView.findViewById(R.id.rating);
        holder.ratingText = (TextView) convertView.findViewById(R.id.rating_text);

        holder.title.setText(item.getTitle());
        holder.date.setText(item.getReleaseDate());
        holder.rating.setRating(Float.valueOf(item.getVoteAverage()));
        StringBuilder builder = new StringBuilder();
        builder.append(item.getVoteAverage()).append("/10 (").append(item.getVoteCount()).append(")");
        holder.ratingText.setText(builder);

        final ImageView poster = (ImageView) convertView.findViewById(R.id.poster);
        final String url = item.getPosterPath(ApiTMDB.SizePoster.w185);
        poster.post(new Runnable() {
            @Override
            public void run() {
                Picasso.with(mContext).load(url).into(poster);
            }
        });
        return convertView;
    }
}
