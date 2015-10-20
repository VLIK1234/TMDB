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
    private final String character;

    public Crew(String profilePath, String name, String character) {
        this.profilePath = profilePath;
        this.name = name;
        this.character = character;
    }

    public Crew(JSONObject jsonObject) throws JSONException {
        this.profilePath = "https://image.tmdb.org/t/p/"+ ApiTMDB.SizePoster.w154 + jsonObject.getString("profile_path");
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
