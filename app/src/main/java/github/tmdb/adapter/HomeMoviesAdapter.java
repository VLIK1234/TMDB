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
import github.tmdb.utils.BitmapDisplayOptions;

/**
 * @author Ivan Bakach
 * @version on 09.08.2015
 */
public class HomeMoviesAdapter extends RecyclerView.Adapter<HomeMoviesAdapter.ViewHolder> {

    public interface ITouch {
        void touchAction(long idItem);
    }

    private final ITouch mITouch;
    private MoviesListCursor mCursor;

    public HomeMoviesAdapter(MoviesListCursor moviesListCursor, ITouch iTouch) {
        mCursor = moviesListCursor;
        mITouch = iTouch;
    }

    @Override
    public HomeMoviesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_series, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final HomeMoviesAdapter.ViewHolder holder, final int position) {
        final MoviesListCursor cursor = (MoviesListCursor)mCursor.get(position);
        final Context context = holder.itemView.getContext();

        holder.mTitle.setText(cursor.getTitle());

        ImageLoader.getInstance().loadImage(cursor.getPosterPath(ApiTMDB.POSTER_300X450_BACKDROP_300X169),
                BitmapDisplayOptions.PORTRAIT_BITMAP_DISPLAY_OPTIONS, new SimpleImageLoadingListener(){
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                Drawable topImage = new BitmapDrawable(context.getResources(), loadedImage);
                holder.mTitle.setCompoundDrawablesWithIntrinsicBounds(null, topImage, null, null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCursor.size();
    }

    public void swapCursor(MoviesListCursor moviesListCursor) {
        CursorUtils.close(mCursor);
        mCursor = moviesListCursor;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTitle;

        public ViewHolder(View convertView) {
            super(convertView);
            mTitle = (TextView) convertView.findViewById(R.id.series_label);
        }

    }
}
