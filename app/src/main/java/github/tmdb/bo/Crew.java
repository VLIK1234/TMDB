package github.tmdb.bo;

import org.json.JSONException;
import org.json.JSONObject;

import github.tmdb.api.ApiTMDB;

/**
 * @author IvanBakach
 * @version on 16.10.2015
 */
public class Crew {
    private final String profilePath;
    private final String name;
    private final String job;

    public Crew(String profilePath, String name, String job) {
        this.profilePath = profilePath;
        this.name = name;
        this.job = job;
    }

    public Crew(JSONObject jsonObject) throws JSONException {
        this.profilePath = ApiTMDB.getImagePath(ApiTMDB.SizePoster.w154, jsonObject.getString("profile_path"));
        this.name = jsonObject.getString("name");
        this.job = jsonObject.getString("job");
    }

    public String getProfilePath() {
        return profilePath;
    }

    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }
}
