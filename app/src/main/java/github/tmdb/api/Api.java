package github.tmdb.api;

public class Api {

    public static final String BASE_PATH = "https://api.vk.com/method/";
    public static final String VERSION_VALUE = "5.28";
    public static final String VERSION_PARAM = "v";

    public static final String WALL_POST = BASE_PATH + "wall.post";
    public static String sendWallPost(String message) {
        return WALL_POST +"?message="+message;
    }
}
