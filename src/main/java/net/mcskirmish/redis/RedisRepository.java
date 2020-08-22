package net.mcskirmish.redis;

import org.bukkit.configuration.file.FileConfiguration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.function.Consumer;

public class RedisRepository {

    private final JedisPool pool;

    /**
     * Represents the redis repository of a database.
     * Uses to get a connection to Redis
     *
     * @param configuration the file to load credentials from
     */
    public RedisRepository(FileConfiguration configuration) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxWaitMillis(1000L);
        config.setMinIdle(200);
        config.setMaxWaitMillis(300);
        config.setTestOnBorrow(false);
        config.setTestOnReturn(true);

        if (configuration.isString("redis.auth")) {
            pool = new JedisPool(config, configuration.getString("redis.host"),
                    configuration.getInt("redis.port"),
                    2000,
                    configuration.getString("redis.auth"));
        } else {
            pool = new JedisPool(config,
                    configuration.getString("redis.auth"),
                    configuration.getInt("port"));
        }

    }

    public JedisPool getPool() {
        return pool;
    }

    public void get(Consumer<Jedis> consumer) {
        try (Jedis resource = pool.getResource()) {
            consumer.accept(resource);
        }
    }

}
