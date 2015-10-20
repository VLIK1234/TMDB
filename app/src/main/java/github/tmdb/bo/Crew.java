package github.tmdb.bo;

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
