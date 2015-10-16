package github.tmdb.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import github.tmdb.R;
import github.tmdb.bo.Crew;

/**
 * @author IvanBakach
 * @version on 16.10.2015
 */
public class CrewsAdapter extends RecyclerView.Adapter<CrewsAdapter.ViewHolder> {

    private final ArrayList<Crew> mCrewList;

    public CrewsAdapter(ArrayList<Crew> crewList) {
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
        holder.charterCrew.setText(mCrewList.get(position).getCharter());
        ImageLoader.getInstance().displayImage(mCrewList.get(position).getProfilePath(), holder.profileCrew);
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
