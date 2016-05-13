package github.tmdb.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import by.istin.android.xcore.utils.CursorUtils;
import github.tmdb.R;
import github.tmdb.api.ApiTMDB;
import github.tmdb.database.cursor.MoviesListCursor;
import github.tmdb.database.cursor.SeriesCursor;
import github.tmdb.utils.BitmapDisplayOptions;

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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Context context = holder.itemView.getContext();
        final SeriesCursor cursor = (SeriesCursor) mSeriesCursor.get(position);
        holder.mSeriesLabel.setText(cursor.getName());

        ImageLoader.getInstance().loadImage(cursor.getPosterPath(ApiTMDB.POSTER_300X450_BACKDROP_300X169),
                BitmapDisplayOptions.PORTRAIT_BITMAP_DISPLAY_OPTIONS, new SimpleImageLoadingListener(){
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        Drawable topImage = new BitmapDrawable(context.getResources(), loadedImage);
                        holder.mSeriesLabel.setCompoundDrawablesWithIntrinsicBounds(null, topImage, null, null);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return mSeriesCursor.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mSeriesLabel;

        public ViewHolder(View itemView) {
            super(itemView);
            mSeriesLabel = (TextView) itemView.findViewById(R.id.series_label);
        }
    }

    public void swapCursor(SeriesCursor moviesListCursor) {
        CursorUtils.close(mSeriesCursor);
        mSeriesCursor = moviesListCursor;
        notifyDataSetChanged();
    }

}
