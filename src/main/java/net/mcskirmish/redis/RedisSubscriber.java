package net.mcskirmish.redis;

import net.mcskirmish.Module;
import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.util.UtilJson;
import redis.clients.jedis.JedisPubSub;

public abstract class RedisSubscriber<T, P extends Module> extends JedisPubSub {

    protected final SkirmishPlugin plugin;
    protected final P parent;
    protected final String channel;
    protected final RedisRepository repository;
    private final Class<T> childType;

    public RedisSubscriber(Class<T> childType, SkirmishPlugin plugin, P parent, String channel) {
        this.childType = childType;
        this.plugin = plugin;
        this.parent = parent;
        this.channel = channel;

        final RedisManager redisManager = plugin.getRedisManager();
        this.repository = redisManager.getRepository();
        redisManager.register(this);

        subscribe();
    }

    public abstract void receive(T data);

    public void subscribe() {
        if (!plugin.isNetworkingServer())
            return;
        parent.runAsync(() -> repository.get(jedis -> jedis.subscribe(this, channel)));
    }

    public void publish(T data) {
        if (!plugin.isNetworkingServer())
            return;
        parent.runAsync(() -> repository.get(jedis -> UtilJson.toJson(data)));
    }

    @Override
    public void onMessage(String channel, String message) {
        if (channel.equals(this.channel)) {
            receive(UtilJson.fromString(message, childType));
        }
    }

}
