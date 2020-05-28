package net.mcskirmish.redis;

import net.mcskirmish.Module;
import net.mcskirmish.SkirmishPlugin;

import java.io.File;

public class RedisManager extends Module {

    private static final String PATH_REDIS_LOCAL = "config" + File.separator + "redis_local";

    public RedisManager(SkirmishPlugin plugin) {
        super(plugin);
    }

    @Override
    protected void start() {

    }

}
