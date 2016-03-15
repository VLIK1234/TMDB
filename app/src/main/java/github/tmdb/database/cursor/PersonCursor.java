package github.tmdb.database.cursor;

import android.database.Cursor;

import by.istin.android.xcore.model.CursorModel;

/**
 * @author Ivan Bakach
 * @version on 13.03.2016
 */
public class PersonCursor extends CursorModel {
    public PersonCursor(Cursor cursor) {
        super(cursor);
    }

    public String getAdult(){
        return getString("adult");
    }

    public String getBiography(){
        return getString("biography");
    }

    public String getBirthday() {
        return getString("birthday");
    }

    public String getDeathday() {
        return getString("deathday");
    }

    public String getHomepage() {
        return getString("homepage");
    }

    public String getImdbId() {
        return getString("imdb_id");
    }

    public String getName() {
        return getString("name");
    }

    public String getPlaceOfBirth() {
        return getString("place_of_birth");
    }

    public String getPopularity() {
        return getString("popularity");
    }

    public String getProfilePath() {
        return getString("profile_path");
    }
}
