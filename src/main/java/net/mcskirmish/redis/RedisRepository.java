package net.mcskirmish.redis;

import com.google.gson.stream.JsonReader;
import net.mcskirmish.util.UtilJson;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;

public class RedisRepository {

    private final JedisPool pool;

    public RedisRepository(File credentialsFile) {
        final Map<String, String> values = readConfig(credentialsFile);
        if (values != null) {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxWaitMillis(1000L);
            config.setMinIdle(200);
            config.setMaxWaitMillis(300);
            config.setTestOnBorrow(false);
            config.setTestOnReturn(true);

            if (values.containsKey("auth")) {
                pool = new JedisPool(config, values.get("host"), Integer.parseInt(values.get("port")));
            } else {
                pool = new JedisPool(config, values.get("host"), Integer.parseInt(values.get("port")),
                        2000, values.get("auth"));
            }

        } else
            pool = null;
    }

    public JedisPool getPool() {
        return pool;
    }

    public void get(Consumer<Jedis> consumer) {
        try (Jedis resource = pool.getResource()) {
            consumer.accept(resource);
        }
    }

    private Map<String, String> readConfig(File file) {
        try (JsonReader jsonReader = new JsonReader(new FileReader(file))) {
            return UtilJson.fromConfig(jsonReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
