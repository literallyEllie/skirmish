package net.mcskirmish.redis;

import com.google.common.collect.Sets;
import net.mcskirmish.Module;
import net.mcskirmish.SkirmishPlugin;
import redis.clients.jedis.JedisPubSub;

import java.io.File;
import java.util.Set;

public class RedisManager extends Module {

    private static final String PATH_REDIS = "config" + File.separator + "redis.json";

    private Set<RedisSubscriber<?, ?>> subscribers;
    private RedisRepository repository;

    public RedisManager(SkirmishPlugin plugin) {
        super(plugin);
    }

    @Override
    protected void start() {
        subscribers = Sets.newHashSet();
        if (!plugin.isNetworkingServer())
            return;

        repository = new RedisRepository(new File(PATH_REDIS));
    }

    @Override
    protected void stop() {
        if (plugin.isNetworkingServer())
            subscribers.forEach(JedisPubSub::unsubscribe);
        subscribers.clear();
    }

    public RedisRepository getRepository() {
        return repository;
    }

    public void register(RedisSubscriber<?, ?> subscriber) {
        subscribers.add(subscriber);
    }

}
