package br.com.pedroncios.worstmovie.util;

import java.time.LocalDateTime;

public class Utils {

    public static void log(String type, String message) {
        System.out.println(LocalDateTime.now() + " [" + type + "] " + message);
    }
}
