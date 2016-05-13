package github.tmdb.database.model;

import android.content.ContentValues;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

import by.istin.android.xcore.annotations.dbBoolean;
import by.istin.android.xcore.annotations.dbDouble;
import by.istin.android.xcore.annotations.dbInteger;
import by.istin.android.xcore.annotations.dbLong;
import by.istin.android.xcore.annotations.dbString;
import by.istin.android.xcore.db.IDBConnection;
import by.istin.android.xcore.db.impl.DBHelper;
import by.istin.android.xcore.processor.IOnProceedEntity;
import by.istin.android.xcore.source.DataSourceRequest;

/**
 * @author Ivan Bakach
 * @version on 03.01.2016
 */
public class SeriesDetailEntity implements BaseColumns {

//                created_by: [
//        {
//            id: 9813,
//                    name: "David Benioff",
//                profile_path: "/8CuuNIKMzMUL1NKOPv9AqEwM7og.jpg"
//        },
//        {
//            id: 228068,
//                    name: "D. B. Weiss",
//                profile_path: "/caUAtilEe06OwOjoQY3B7BgpARi.jpg"
//        }
//        ],
//        episode_run_time: [
//        60
//        ],
//                genres: [
//        {
//            id: 10759,
//                    name: "Action & Adventure"
//        },
//        {
//            id: 18,
//                    name: "Drama"
//        },
//        {
//            id: 10765,
//                    name: "Sci-Fi & Fantasy"
//        }
//        ],z
//            languages: [
//        "es",
//                "en",
//                "de"
//        ],
//            networks: [
//        {
//            id: 49,
//                    name: "HBO"
//        }
//        ],
//            origin_country: [
//        "US"
//        ],
//            production_companies: [
//        ],
//        seasons: [
//        ],

    public static final String ID_KEY = "id";

    @dbLong
    @SerializedName(value = "id")
    public static final String ID = _ID;

    @dbString
    public static final String BACKDROP_PATH = "backdrop_path";

    @dbString
    public static final String FIRST_AIR_DATE = "first_air_date";

    @dbString
    public static final String HOMEPAGE = "homepage";

    @dbBoolean
    public static final String IN_PRODUCTION = "in_production";

    @dbString
    public static final String LAST_AIR_DATE = "last_air_date";

    @dbString
    public static final String NAME = "name";

    @dbInteger
    public static final String NUMBER_OF_EPISODES = "number_of_episodes";

    @dbInteger
    public static final String NUMBER_OF_SEASONS = "number_of_seasons";

    @dbString
    public static final String ORIGINAL_LANGUAGE = "original_language";

    @dbString
    public static final String ORIGINAL_NAME = "original_name";

    @dbString
    public static final String OVERVIEW = "overview";

    @dbString
    public static final String POSTER_PATH = "poster_path";

    @dbDouble
    public static final String POPULARITY = "popularity";

    @dbString
    public static final String STATUS = "status";

    @dbString
    public static final String TYPE = "type";

    @dbDouble
    public static final String VOTE_AVERAGE = "vote_average";

    @dbDouble
    public static final String VOTE_COUNT = "vote_count";

    public static final String GENRES = "genres";

//    @Override
//    public boolean onProceedEntity(DBHelper dbHelper, IDBConnection db, DataSourceRequest dataSourceRequest, @Nullable ContentValues parent, ContentValues contentValues, int position, JsonElement jsonElement) {
//        JsonObject jsonObject = jsonElement.getAsJsonObject();
//        long id = jsonObject.get(ID_KEY).getAsLong();
//        for (Map.Entry<String, JsonElement> item : jsonObject.entrySet()) {
//            String keyJsonElement = item.getKey();
//            JsonElement value = item.getValue();
//            if (GENRES.equals(keyJsonElement)) {
//                parseGenres(dbHelper, db, dataSourceRequest, id, value);
//            }
////            else if (PRODUCTION_COMPANIES.equals(keyJsonElement)) {
////                parseProductionCompanies(dbHelper, db, dataSourceRequest, id, value);
////            } else if (PRODUCTION_COUNTRIES.equals(keyJsonElement)) {
////                parseProductionCountries(dbHelper, db, dataSourceRequest, id, value);
////            } else if (SPOKEN_LANGUAGES.equals(keyJsonElement)) {
////                parseSpokenLanguages(dbHelper, db, dataSourceRequest, id, value);
////            }
//        }
//        dbHelper.updateOrInsert(dataSourceRequest, db, SeriesDetailEntity.class, contentValues);
//        return false;
//    }
//
    private void parseGenres(DBHelper dbHelper, IDBConnection db, DataSourceRequest dataSourceRequest, long id, JsonElement value) {
        ContentValues genresValue = new ContentValues();
        JsonArray genresArray = value.getAsJsonArray();
        for (int i = 0; i < genresArray.size(); i++) {
            JsonObject genre = genresArray.get(i).getAsJsonObject();
            String genreName = genre.get(Genre.NAME).getAsString();
            genresValue.put(Genre.NAME, genreName);
            genresValue.put(Genre.SERIES_ID, id);
            genresValue.put(Genre.ID, genreName.hashCode() + id);
            dbHelper.updateOrInsert(dataSourceRequest, db, Genre.class, genresValue);
            genresValue.clear();
        }
    }
//
//    private void parseProductionCompanies(DBHelper dbHelper, IDBConnection db, DataSourceRequest dataSourceRequest, long idMovie, JsonElement value) {
//        ContentValues productionCompanyValue = new ContentValues();
//        JsonArray productionCompaniesArray = value.getAsJsonArray();
//        for (int i = 0; i < productionCompaniesArray.size(); i++) {
//            JsonObject genre = productionCompaniesArray.get(i).getAsJsonObject();
//            for (Map.Entry<String, JsonElement> itemGenre : genre.entrySet()) {
//                if (ProductionCompany.ID_KEY.equals(itemGenre.getKey())) {
//                    productionCompanyValue.put(ProductionCompany.ID, itemGenre.getValue().getAsLong());
//                } else if (ProductionCompany.NAME.equals(itemGenre.getKey())) {
//                    productionCompanyValue.put(itemGenre.getKey(), itemGenre.getValue().getAsString());
//                }
//            }
//            productionCompanyValue.put(ProductionCompany.MOVIE_ID, idMovie);
//            dbHelper.updateOrInsert(dataSourceRequest, db, ProductionCompany.class, productionCompanyValue);
//            productionCompanyValue.clear();
//        }
//    }
//
//    private void parseProductionCountries(DBHelper dbHelper, IDBConnection db, DataSourceRequest dataSourceRequest, long idMovie, JsonElement value) {
//        ContentValues productionCountryValue = new ContentValues();
//        JsonArray productionCountriesArray = value.getAsJsonArray();
//        for (int i = 0; i < productionCountriesArray.size(); i++) {
//            JsonObject genre = productionCountriesArray.get(i).getAsJsonObject();
//            for (Map.Entry<String, JsonElement> itemGenre : genre.entrySet()) {
//                if (ProductionCountry.NAME.equals(itemGenre.getKey()) || ProductionCountry.ISO_3166_1.equals(itemGenre.getKey())) {
//                    productionCountryValue.put(itemGenre.getKey(), itemGenre.getValue().getAsString());
//                }
//            }
//            productionCountryValue.put(ProductionCountry.ID, (idMovie + genre.get(Genre.NAME).getAsString()).hashCode());
//            productionCountryValue.put(ProductionCountry.MOVIE_ID, idMovie);
//            dbHelper.updateOrInsert(dataSourceRequest, db, ProductionCountry.class, productionCountryValue);
//            productionCountryValue.clear();
//        }
//    }
//
//    private void parseSpokenLanguages(DBHelper dbHelper, IDBConnection db, DataSourceRequest dataSourceRequest, long idMovie, JsonElement value) {
//        ContentValues spokenLanguageValue = new ContentValues();
//        JsonArray spokenLanguagesArray = value.getAsJsonArray();
//        for (int i = 0; i < spokenLanguagesArray.size(); i++) {
//            JsonObject genre = spokenLanguagesArray.get(i).getAsJsonObject();
//            for (Map.Entry<String, JsonElement> itemGenre : genre.entrySet()) {
//                if (SpokenLanguage.NAME.equals(itemGenre.getKey()) || SpokenLanguage.ISO_3166_1.equals(itemGenre.getKey())) {
//                    spokenLanguageValue.put(itemGenre.getKey(), itemGenre.getValue().getAsString());
//                }
//            }
//            spokenLanguageValue.put(SpokenLanguage.ID, (idMovie + genre.get(SpokenLanguage.NAME).getAsString()).hashCode());
//            spokenLanguageValue.put(SpokenLanguage.MOVIE_ID, idMovie);
//            dbHelper.updateOrInsert(dataSourceRequest, db, ProductionCountry.class, spokenLanguageValue);
//            spokenLanguageValue.clear();
//        }
//    }
}
