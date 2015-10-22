package github.tmdb.bo;

import org.json.JSONException;
import org.json.JSONObject;

import github.tmdb.api.ApiTMDB;

/**
 * @author IvanBakach
 * @version on 16.10.2015
 */
public class Cast {
    private final String profilePath;
    private final String name;
    private final String character;

    public Cast(String profilePath, String name, String character) {
        this.profilePath = profilePath;
        this.name = name;
        this.character = character;
    }

    public Cast(JSONObject jsonObject) throws JSONException {
        this.profilePath = ApiTMDB.getImagePath(ApiTMDB.SizePoster.w154, jsonObject.getString("profile_path"));
        this.name = jsonObject.getString("name");
        this.character = jsonObject.getString("character");
    }

    public String getProfilePath() {
        return profilePath;
    }

    public String getName() {
        return name;
    }

    public String getCharacter() {
        return character;
    }
}
