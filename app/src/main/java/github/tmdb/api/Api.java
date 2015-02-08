package github.tmdb.api;

public class Api {

    public static final String BASE_PATH = "https://api.vk.com/method/";
    public static final String VERSION_VALUE = "5.8";
    public static final String VERSION_PARAM = "v";

    public static final String FRIENDS_GET = BASE_PATH + "friends.get?fields=photo_200_orig,online,nickname";
    public static final String WALL_POST = BASE_PATH + "wall.post?fields=photo_200_orig,online,nickname";
    public static String setWallPost(String message) {
        return WALL_POST +"&message="+message;
    }
}
