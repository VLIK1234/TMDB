package github.tmdb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import github.tmdb.R;
import github.tmdb.bo.Cast;
import github.tmdb.utils.BitmapDisplayOptions;
import github.tmdb.utils.TextUtilsImpl;

/**
 * @author IvanBakach
 * @version on 16.10.2015
 */
public class CastAdapter extends RecyclerView.Adapter<CastAdapter.ViewHolder> {

    private final ArrayList<Cast> mCastList;
    private final Context mContext;
    private int mCharterLabelColor;

    public CastAdapter(Context context, ArrayList<Cast> castList) {
        mContext = context;
        mCastList = castList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        parent.setHorizontalScrollBarEnabled(true);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_crews, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nameCrew.setText(mCastList.get(position).getName());
        String charter = mCastList.get(position).getCharacter();
        if (!TextUtilsImpl.isEmpty(charter)) {
            holder.charterCrew.setText(String.format("as %s", charter));
        }
        //if color not set yet
        if (mCharterLabelColor != 0) {
            holder.charterCrew.setTextColor(mCharterLabelColor);
        }
        String profilePath = mCastList.get(position).getProfilePath();
        ImageLoader.getInstance().displayImage(profilePath, holder.profileCrew, BitmapDisplayOptions.PORTRAIT_BITMAP_DISPLAY_OPTIONS);
    }

    @Override
    public int getItemCount() {
        return mCastList.size();
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
