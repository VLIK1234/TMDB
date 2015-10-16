package github.tmdb.bo;

/**
 * @author IvanBakach
 * @version on 16.10.2015
 */
public class Crew {
    private final String profilePath;
    private final String name;
    private final String charter;

    public Crew(String profilePath, String name, String charter) {
        this.profilePath = profilePath;
        this.name = name;
        this.charter = charter;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public String getName() {
        return name;
    }

    public String getCharter() {
        return charter;
    }
}
