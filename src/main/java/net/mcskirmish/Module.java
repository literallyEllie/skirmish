package net.mcskirmish;

import org.bukkit.event.Listener;

public abstract class Module implements Listener {

    protected final SkirmishPlugin plugin;
    private final String name;
    private final long startupTime;

    public Module(SkirmishPlugin plugin) {
        long start = System.currentTimeMillis();

        this.plugin = plugin;
        this.name = getClass().getSimpleName();

        try {
            start();

            plugin.registerModule(this);
        } catch (Throwable throwable) {
            plugin.error("error enabling module " + name, throwable);
        }

        startupTime = System.currentTimeMillis() - start;
    }

    protected abstract void start();

    protected void stop() {
    }

    public final String getName() {
        return name;
    }

    public final long getStartupTime() {
        return startupTime;
    }

    public void runAsync(Runnable runnable) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable);
    }

}
