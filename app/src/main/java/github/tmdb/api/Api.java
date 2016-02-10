package github.tmdb.api;

public class Api {

    private static final String BASE_PATH = "https://api.vk.com/method/";
    public static final String VERSION_VALUE = "5.28";
    public static final String VERSION_PARAM = "v";

    private static final String WALL_POST = BASE_PATH + "wall.post";
    public static String sendWallPost(String message) {
        return WALL_POST +"?message="+message;
    }
}
