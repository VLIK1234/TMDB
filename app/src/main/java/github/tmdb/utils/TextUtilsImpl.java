package github.tmdb.utils;

/**
 * @author IvanBakach
 * @version on 22.10.2015
 */
public class TextUtilsImpl {
    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0||str.equals("null"))
            return true;
        else
            return false;
    }
}
