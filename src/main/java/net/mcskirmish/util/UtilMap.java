package net.mcskirmish.util;

import com.google.common.collect.Maps;

import java.util.Map;

public class UtilMap {

    public static <K, V> Map<K, V> newOf(K key, V value) {
        Map<K, V> map = Maps.newHashMap();
        map.put(key, value);
        return map;
    }

}
