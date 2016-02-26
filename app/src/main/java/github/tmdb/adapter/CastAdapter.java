package github.tmdb.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import by.istin.android.xcore.utils.CursorUtils;
import by.istin.android.xcore.utils.StringUtil;
import github.tmdb.R;
import github.tmdb.database.cursor.CastCursor;
import github.tmdb.listener.IClickCallback;
import github.tmdb.utils.BitmapDisplayOptions;

/**
 * @author IvanBakach
 * @version on 16.10.2015
 */
public class CastAdapter extends RecyclerView.Adapter<CastAdapter.ViewHolder> {

    private CastCursor mCursor;
    private int mCharterLabelColor;
    private IClickCallback mClickCallback;

    public CastAdapter(CastCursor cursors, IClickCallback clickCallback) {
        swapCursor(cursors);
        mCursor = cursors;
        mClickCallback = clickCallback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        parent.setHorizontalScrollBarEnabled(true);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_crews, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        holder.nameCrew.setText(mCursor.getName());
        String charter = mCursor.getCharacter();
        if (!StringUtil.isEmpty(charter)) {
            holder.charterCrew.setText(String.format("as %s", charter));
        }
        //if color not set yet
        if (mCharterLabelColor != 0) {
            holder.charterCrew.setTextColor(mCharterLabelColor);
        }
        String profilePath = mCursor.getProfilePath();
        ImageLoader.getInstance().displayImage(profilePath, holder.profileCrew, BitmapDisplayOptions.PORTRAIT_BITMAP_DISPLAY_OPTIONS);
        holder.itemView.setTag(mCursor.getId());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickCallback.onClickCallback(v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCursor.size();
    }

    public void swapCursor(CastCursor moviesListCursor) {
        CursorUtils.close(mCursor);
        mCursor = moviesListCursor;
        notifyDataSetChanged();
    }

    public void setCharterLabelColor(int rgbColor){
        mCharterLabelColor = rgbColor;
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{
        public final ImageView profileCrew;
        public final TextView nameCrew;
        public final TextView charterCrew;

        public ViewHolder(View itemView) {
            super(itemView);
            profileCrew = (ImageView) itemView.findViewById(R.id.iv_profile_crew);
            nameCrew = (TextView) itemView.findViewById(R.id.tv_name_crew);
            charterCrew = (TextView) itemView.findViewById(R.id.tv_charter_crew);
        }
    }

}
