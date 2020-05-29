package net.mcskirmish;

import com.google.common.collect.Lists;
import net.mcskirmish.account.AccountManager;
import net.mcskirmish.chat.ChatManager;
import net.mcskirmish.command.CommandManager;
import net.mcskirmish.mongo.MongoManager;
import net.mcskirmish.redis.RedisManager;
import net.mcskirmish.server.ServerManager;
import net.mcskirmish.staff.StaffManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Collection;
import java.util.logging.Level;

public abstract class SkirmishPlugin extends JavaPlugin {

    private final Collection<Module> modules = Lists.newArrayList();
    private MongoManager mongoManager;
    private RedisManager redisManager;
    private ServerManager serverManager;
    private AccountManager accountManager;
    private CommandManager commandManager;
    private ChatManager chatManager;
    private StaffManager staffManager;

    private boolean isLocalServer, isDevServer, isNetworkingServer, isLobbyServer;
    private long serverStart, startupTime;

    @Override
    public final void onEnable() {
        serverStart = System.currentTimeMillis();

        // load server type
        isLocalServer = new File("LOCAL").exists();
        isDevServer = new File("DEV").exists();
        isNetworkingServer = new File("NETWORKING").exists();
        isLobbyServer = new File("LOBBY").exists();

        // start modules
        mongoManager = new MongoManager(this);
        redisManager = new RedisManager(this);
        serverManager = new ServerManager(this);
        accountManager = new AccountManager(this);
        commandManager = new CommandManager(this);
        chatManager = new ChatManager(this);
        staffManager = new StaffManager(this);

        // startup underlying
        try {
            start();
        } catch (Throwable throwable) {
            error("error starting up underlying plugin", throwable);
        }

        // finish startup
        startupTime = System.currentTimeMillis() - serverStart;
        log(getDescription().getName() + " v" + getDescription().getVersion() + " loaded in " + startupTime + " ms");
    }

    @Override
    public final void onDisable() {
        // stop underlying
        try {
            stop();
        } catch (Throwable throwable) {
            error("error disabling underlying plugin", throwable);
        }

        // disable modules
        modules.forEach(module -> {
            try {
                module.stop();
            } catch (Throwable e) {
                error("failed to stop module " + module.getName(), e);
            }
        });

        // finish disable
        log(getDescription().getName() + "v " + getDescription().getVersion() + " disabled.");
    }

    protected abstract void start();

    protected void stop() {
    }

    public void registerModule(Module module) {
        modules.add(module);
        registerListener(module);
    }

    public void registerListener(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    public MongoManager getMongoManager() {
        return mongoManager;
    }

    public RedisManager getRedisManager() {
        return redisManager;
    }

    public ServerManager getServerManager() {
        return serverManager;
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public ChatManager getChatManager() {
        return this.chatManager;
    }

    public StaffManager getStaffManager() {
        return staffManager;
    }

    public boolean isLocalServer() {
        return isLocalServer;
    }

    public boolean isDevServer() {
        return isDevServer;
    }

    public boolean isNetworkingServer() {
        return isNetworkingServer;
    }

    public boolean isLobbyServer() {
        return isLobbyServer;
    }

    public long getServerStart() {
        return serverStart;
    }

    public long getStartupTime() {
        return startupTime;
    }

    public void log(Level level, String data, Throwable throwable) {
        getLogger().log(level, data, throwable);
    }

    public void log(Level level, String data) {
        log(level, data, null);
    }

    public void log(String info) {
        log(Level.INFO, info);
    }

    public void warn(String warn) {
        log(Level.WARNING, warn);
    }

    public void error(String error, Throwable throwable) {
        log(Level.SEVERE, error, throwable);
    }

}
