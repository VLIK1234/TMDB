package github.tmdb.utils;

import android.text.Html;
import android.text.Spanned;

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

    public static Spanned setBold(String text) {
        return Html.fromHtml(String.format("<b>%1$s</b>", text));
    }

    public static Spanned setSmall(String text) {
        return Html.fromHtml(String.format("<small>%1$s</small>", text));
    }

    public static Spanned lineBreak() {
        return Html.fromHtml("<br>");
    }
}
