package net.mcskirmish;

import com.google.common.collect.Lists;
import net.mcskirmish.account.AccountManager;
import net.mcskirmish.chat.ChatManager;
import net.mcskirmish.command.CommandManager;
import net.mcskirmish.event.update.Updater;
import net.mcskirmish.mongo.MongoManager;
import net.mcskirmish.network.NetworkManager;
import net.mcskirmish.redis.RedisManager;
import net.mcskirmish.region.RegionManager;
import net.mcskirmish.server.IDomainProvider;
import net.mcskirmish.server.ITabProvider;
import net.mcskirmish.server.ServerManager;
import net.mcskirmish.staff.StaffManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;

public abstract class SkirmishPlugin extends JavaPlugin {

    private final Collection<Module> modules = Lists.newLinkedList();
    private MongoManager mongoManager;
    private RedisManager redisManager;
    private ServerManager serverManager;
    private NetworkManager networkManager;
    private AccountManager accountManager;
    private CommandManager commandManager;
    private ChatManager chatManager;
    private StaffManager staffManager;
    private RegionManager regionManager;

    private boolean isLocalServer, isDevServer, isNetworkingServer, isLobbyServer;
    private long serverStart, startupTime;

    // generics
    private ITabProvider tabProvider;
    private IDomainProvider domainProvider;

    @Override
    public final void onEnable() {
        serverStart = System.currentTimeMillis();
        saveDefaultConfig();

        // load server type
        isLocalServer = new File("LOCAL").exists();
        isDevServer = new File("DEV").exists();
        isNetworkingServer = new File("NETWORKING").exists();
        isLobbyServer = new File("LOBBY").exists();

        // start modules
        mongoManager = new MongoManager(this);
        redisManager = new RedisManager(this);
        serverManager = new ServerManager(this);
        networkManager = new NetworkManager(this);
        accountManager = new AccountManager(this);
        commandManager = new CommandManager(this);
        chatManager = new ChatManager(this);
        staffManager = new StaffManager(this);
        regionManager = new RegionManager(this);

        // startup underlying
        try {
            start();
        } catch (Throwable throwable) {
            error("error starting up underlying plugin", throwable);
        }

        // start updater
        new Updater(this);

        // finish startup
        startupTime = System.currentTimeMillis() - serverStart;
        log(getDescription().getName() + " v" + getDescription().getVersion() + " loaded in " + startupTime + "ms");
    }

    @Override
    public final void onDisable() {
        // stop underlying
        try {
            stop();
        } catch (Throwable throwable) {
            error("error disabling underlying plugin", throwable);
        }

        // disable modules in reverse order to keep most important alive
        final Iterator<Module> moduleIterator = ((LinkedList<Module>) modules).descendingIterator();
        while (moduleIterator.hasNext()) {
            final Module module = moduleIterator.next();

            try {
                module.stop();
            } catch (Throwable e) {
                error("failed to stop module " + module.getName(), e);
            } finally {
                moduleIterator.remove();
            }
        }

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

    public NetworkManager getNetworkManager() {
        return networkManager;
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

    public RegionManager getRegionManager() {
        return regionManager;
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

    public ITabProvider getTabProvider() {
        return tabProvider;
    }

    public void setTabProvider(ITabProvider tabProvider) {
        this.tabProvider = tabProvider;
    }

    public IDomainProvider getDomainProvider() {
        return domainProvider;
    }

    public void setDomainProvider(IDomainProvider domainProvider) {
        this.domainProvider = domainProvider;
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
