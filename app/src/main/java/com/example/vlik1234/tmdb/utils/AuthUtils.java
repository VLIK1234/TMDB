package com.example.vlik1234.tmdb.utils;

import com.example.vlik1234.tmdb.auth.VkOAuthHelper;

/**
 * Created by ASUS on 02.11.2014.
 */
public class AuthUtils {

    public static boolean isLogged() {
        return VkOAuthHelper.isLogged();
    }
}