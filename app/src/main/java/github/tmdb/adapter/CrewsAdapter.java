package github.tmdb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import github.tmdb.R;
import github.tmdb.bo.Crew;

/**
 * @author IvanBakach
 * @version on 16.10.2015
 */
public class CrewsAdapter extends RecyclerView.Adapter<CrewsAdapter.ViewHolder> {

    private final ArrayList<Crew> mCrewList;
    private final Context mContext;

    public CrewsAdapter(Context context, ArrayList<Crew> crewList) {
        mContext = context;
        mCrewList = crewList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        parent.setHorizontalScrollBarEnabled(true);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_crews, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nameCrew.setText(mCrewList.get(position).getName());
        holder.charterCrew.setText(mCrewList.get(position).getCharacter());
        Picasso.with(mContext).load(mCrewList.get(position).getProfilePath()).into(holder.profileCrew);
//        ImageLoader.getInstance().displayImage(mCrewList.get(position).getProfilePath(), holder.profileCrew);
    }

    @Override
    public int getItemCount() {
        return mCrewList.size();
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
