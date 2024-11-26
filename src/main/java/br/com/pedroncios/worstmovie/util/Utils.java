package br.com.pedroncios.worstmovie.util;

import br.com.pedroncios.worstmovie.dto.ProducerDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Utils {

    public static void log(String type, String message) {
        System.out.println(LocalDateTime.now() + " [" + type + "] " + message);
    }

    private static String[] parseNames(String names) {
        if (names == null || names.isBlank()) {
            return null;
        }
        return(names.split(",| and "));
    }

    public static <T> List<T> parseObjects(String names, Function<String, T> mapper) {
        List<T> objects = new ArrayList<>();
        String[] parsedNames = parseNames(names);

        if (parsedNames != null) {
            for (String name : parsedNames) {
                String trimmedName = name.trim();
                if (!trimmedName.isEmpty()) {
                    objects.add(mapper.apply(trimmedName));
                }
            }
        }

        return objects;
    }
}
