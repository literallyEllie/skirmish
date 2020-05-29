package net.mcskirmish.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.util.Map;

public class UtilJson {

    private static final Gson GSON;
    private static TypeToken<Map<String, String>> GENERIC_CONF_TOKEN;

    static {
        GSON = new Gson();
        GENERIC_CONF_TOKEN = new TypeToken<Map<String, String>>() {
        };
    }

    public static <T> T fromFile(JsonReader reader, TypeToken<T> token) {
        return GSON.fromJson(reader, token.getType());
    }

    public static <T> T fromString(String json, TypeToken<T> token) {
        return GSON.fromJson(json, token.getType());
    }

    public static Map<String, String> fromConfig(JsonReader reader) {
        return fromFile(reader, GENERIC_CONF_TOKEN);
    }

}
