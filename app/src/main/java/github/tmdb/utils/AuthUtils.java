package github.tmdb.utils;

import github.tmdb.auth.VkOAuthHelper;

/**
 @author IvanBakach
 @version on 02.11.2014
 */
public class AuthUtils {

    public static boolean isLogged() {
        return VkOAuthHelper.isLogged();
    }
}