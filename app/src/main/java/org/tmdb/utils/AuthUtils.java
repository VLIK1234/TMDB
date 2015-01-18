package org.tmdb.utils;

import org.tmdb.auth.VkOAuthHelper;

/**
 * Created by VLIK on 02.11.2014.
 */
public class AuthUtils {

    public static boolean isLogged() {
        return VkOAuthHelper.isLogged();
    }
}