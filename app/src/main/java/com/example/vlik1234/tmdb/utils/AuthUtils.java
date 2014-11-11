package com.example.vlik1234.tmdb.utils;

/**
 * Created by ASUS on 02.11.2014.
 */
public class AuthUtils {

    private static boolean IS_AUTHORIZED = false;

    public static void setLogged(boolean isLogged) {
        //TODO do not write it in real life
        IS_AUTHORIZED = isLogged;
    }

    public static boolean isLogged() {
        return IS_AUTHORIZED;
    }
}