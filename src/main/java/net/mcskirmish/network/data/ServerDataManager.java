package net.mcskirmish.network.data;

import com.google.common.collect.Maps;
import com.mongodb.client.FindIterable;
import net.mcskirmish.Module;
import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.account.Account;
import net.mcskirmish.event.update.UpdateEvent;
import net.mcskirmish.event.update.UpdateType;
import net.mcskirmish.mongo.table.ServerDataRepository;
import net.mcskirmish.mongo.table.ServerPlayersRepository;
import net.mcskirmish.network.NetworkManager;
import net.mcskirmish.network.ServerGroup;
import net.mcskirmish.network.data.player.PlayerServerData;
import net.mcskirmish.network.data.server.ServerData;
import org.bson.Document;
import org.bukkit.event.EventHandler;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public class ServerDataManager extends Module {

    private ServerDataRepository serverRepository;
    private ServerPlayersRepository playersRepository;

    /**
     * Manager for the backend side of server data.
     *
     * <b>Module will not be initiated if {@link SkirmishPlugin#isNetworkingServer()} is <code>false</code></b>
     * <p>
     * Refreshes {@link NetworkManager#getServerData()} periodically
     * and publishes the state of this server to the repository.
     * <p>
     * When the server ends, it will also delete the server from the repository
     *
     * @param plugin plugin instance
     */
    public ServerDataManager(SkirmishPlugin plugin) {
        super(plugin);
    }

    @Override
    protected void start() {
        serverRepository = new ServerDataRepository(plugin);
        playersRepository = new ServerPlayersRepository(plugin);

        // update at random period
        runAsync(0, ThreadLocalRandom.current().nextInt(15, 30) * 20, () -> getDataByName(null));
    }

    @Override
    protected void stop() {
        // remove from active server pool
        serverRepository.delete(ServerData.ID, plugin.getServerManager().getServerId());
    }

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if (event.getType() == UpdateType.TEN_SEC) {
            publishState();
        }
    }

    /**
     * Gets active server data asynchronously and groups by {@link ServerGroup}
     *
     * @param result active server data
     */
    public void getDataByGroup(Consumer<Map<ServerGroup, ServerData>> result) {
        runAsync(() -> {
            final FindIterable<Document> results = serverRepository.queryAll();

            Map<ServerGroup, ServerData> serverData = Maps.newHashMap();
            results.forEach((Consumer<? super Document>) document -> {
                ServerData data = new ServerData(document);
                serverData.put(data.getServerGroup(), data);
            });

            if (result != null)
                result.accept(serverData);
        });
    }

    /**
     * Gets active server data asynchronously and groups by their lowercase {@link ServerData#getId()}
     * <p>
     * Also updates to {@link NetworkManager#getServerData()}
     *
     * @param result active server data
     */
    public void getDataByName(Consumer<Map<String, ServerData>> result) {
        runAsync(() -> {
            final FindIterable<Document> results = serverRepository.queryAll();

            Map<String, ServerData> serverData = Maps.newHashMap();
            results.forEach((Consumer<? super Document>) document -> {
                ServerData data = new ServerData(document);
                serverData.put(data.getId().toLowerCase(), data);
            });

            plugin.getNetworkManager().updateServerData(serverData);

            if (result != null)
                result.accept(serverData);
        });
    }

    /**
     * Registers the account has being logged onto the server.
     * <p>
     * Should only be called one time when they join the local server
     *
     * @param account account joining
     * @param group   server category group
     */
    public void registerPlayerOnServer(Account account, ServerGroup group) {
        runAsync(() -> playersRepository.insert(new PlayerServerData(account, group).getDocument()));
    }

    /**
     * Registers an account having disconnected from the local server.
     * <p>
     * Should only be called when they are definitely going to leave.
     *
     * @param account account leaving
     */
    public void unregisterPlayerOnServer(Account account) {
        runAsync(() -> playersRepository.delete(PlayerServerData.ID, account.getUuid()));
    }

    private void publishState() {
        final ServerData serverData = plugin.getNetworkManager().updateSelfServerData();
        Document document = serverData.getDocument();

        runAsync(() -> {
            final Document existing = serverRepository.query(ServerData.ID, serverData.getId());
            if (existing == null) {
                serverRepository.insert(document);
            } else {
                Map<String, Object> updateValues = Maps.newHashMap();

                updateValues.put(ServerData.PUBLIC_IP, serverData.getPublicIp());
                updateValues.put(ServerData.PLAYERS, serverData.getPlayers());
                updateValues.put(ServerData.TPS, serverData.getTps());
                updateValues.put(ServerData.REQUIRED_RANK, serverData.getRequiredRank().name());

                serverRepository.batchUpdate(ServerData.ID, serverData.getId(), updateValues);
            }
        });

    }

}
