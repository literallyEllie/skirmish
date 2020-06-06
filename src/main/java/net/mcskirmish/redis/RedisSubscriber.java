package net.mcskirmish.redis;

import net.mcskirmish.Module;
import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.util.UtilJson;
import redis.clients.jedis.JedisPubSub;

import java.util.function.Consumer;

public abstract class RedisSubscriber<T, P extends Module> extends JedisPubSub {

    protected final SkirmishPlugin plugin;
    protected final P parent;
    protected final String channel;
    protected final RedisRepository repository;
    private final Class<T> childType;

    /**
     * Represents a subscriber listener to a Redis PubSub channel.
     *
     * If {@link SkirmishPlugin#isNetworkingServer()} is false, this will just act idle.
     * Implementation should be made to accommodate that.
     *
     * When a message is received on the channel. It parsed using {@link com.google.gson.Gson}
     * using the childType and sent to {@link RedisSubscriber#receive(Object)} for processing.
     *
     * @param childType the type expected to come through the channel
     * @param plugin plugin instance
     * @param parent the parent module for this subscriber
     * @param channel the channel to listen on
     */
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

    /**
     * Method called when data is successfully parsed into the childType using {@link com.google.gson.Gson}
     * incoming on this channel
     *
     * @param data the parsed data received on the channel
     */
    public abstract void receive(T data);

    /**
     * Subscribes to the channel using a connection from the {@link RedisRepository#get(Consumer)}
     *
     * <b>This method is called already from the constructor</b>
     *
     * If the server is not {@link SkirmishPlugin#isNetworkingServer()} this will be ignored.
     */
    public void subscribe() {
        if (!plugin.isNetworkingServer())
            return;
        parent.runAsync(() -> repository.get(jedis -> jedis.subscribe(this, channel)));
    }

    /**
     * Publishes data to the channel for other listeners on this channel to process.
     *
     * The data is serialized into a JSON string with {@link com.google.gson.Gson}
     *
     * If the server is not {@link SkirmishPlugin#isNetworkingServer()} this will be ignored.
     *
     * @param data data to serialize and send on the channel
     */
    public void publish(T data) {
        if (!plugin.isNetworkingServer())
            return;
        parent.runAsync(() -> repository.get(jedis -> jedis.publish(channel, UtilJson.toJson(data))));
    }

    @Override
    public void onMessage(String channel, String message) {
        if (channel.equals(this.channel)) {
            receive(UtilJson.fromString(message, childType));
        }
    }

}
