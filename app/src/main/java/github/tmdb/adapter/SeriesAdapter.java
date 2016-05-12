package github.tmdb.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import github.tmdb.R;
import github.tmdb.api.ApiTMDB;
import github.tmdb.database.cursor.SeriesCursor;

/**
 * @author Ivan_Bakach
 * @version on 5/12/16
 */
public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.ViewHolder> {

    private SeriesCursor mSeriesCursor;

    public SeriesAdapter(SeriesCursor seriesCursor) {
        mSeriesCursor = seriesCursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_series, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SeriesCursor cursor = (SeriesCursor) mSeriesCursor.get(position);
        holder.mSeriesLabel.setText(cursor.getName());
        ImageLoader.getInstance().displayImage(cursor.getPosterPath(ApiTMDB.POSTER_185X278_BACKDROP_185X104), holder.mSeriesPoster);
    }

    @Override
    public int getItemCount() {
        return mSeriesCursor.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mSeriesLabel;
        public final ImageView mSeriesPoster;

        public ViewHolder(View itemView) {
            super(itemView);
            mSeriesLabel = (TextView) itemView.findViewById(R.id.series_label);
            mSeriesPoster = (ImageView) itemView.findViewById(R.id.series_poster);
        }
    }

}
