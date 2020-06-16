package net.mcskirmish.network;

import com.google.common.collect.Maps;
import net.mcskirmish.Module;
import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.network.data.ServerDataManager;
import net.mcskirmish.util.UtilServer;

import java.util.Comparator;
import java.util.Map;

public class NetworkManager extends Module {

    public static final String DEST_NONE = "NONE";

    private ServerData thisServer;
    private Map<String, ServerData> serverData;

    private ServerDataManager serverDataManager;

    /**
     * Keeps track of server states on the network and player counts
     * <p>
     * Will do virtually nothing if {@link SkirmishPlugin#isNetworkingServer()} is <code>false</code>
     * ({@link ServerDataManager} will subsequently not be initiated if so)
     *
     * @param plugin plugin instance
     */
    public NetworkManager(SkirmishPlugin plugin) {
        super(plugin);
    }

    @Override
    protected void start() {
        serverData = Maps.newHashMap();

        if (plugin.isNetworkingServer()) {
            thisServer = new ServerData(plugin.getServerManager().getServerId(),
                    ServerGroup.fromName(plugin.getServerManager().getServerId()),
                    UtilServer.getPublicIp(),
                    UtilServer.getOnlinePlayers(),
                    UtilServer.getMaxPlayers(),
                    UtilServer.getTps(),
                    plugin.getServerManager().getMinRank());

            serverDataManager = new ServerDataManager(plugin);
        }
    }

    /**
     * @return the network server data of this server
     */
    public ServerData getThisServer() {
        return thisServer;
    }

    /**
     * Provides the last update of the active servers on the network
     * <p>
     * Indexed by {@link ServerData#getId()} in lowercase
     *
     * @return active server data
     */
    public Map<String, ServerData> getServerData() {
        return serverData;
    }

    /**
     * Overwrites the server data map using this new data
     *
     * @param serverData new server data
     */
    public void updateServerData(Map<String, ServerData> serverData) {
        this.serverData.clear();
        this.serverData.putAll(serverData);
    }

    /**
     * Gets the emptiest lobby on the network according to the active servers.
     * <p>
     * May be <code>null</code> if there is no lobbies activated
     *
     * @return the emptiest lobby on the network. May be <code>null</code>
     */
    public ServerData getEmptiestLobby() {
        return serverData.values().stream()
                .filter(data -> data.getServerGroup().isLobby())
                .sorted(Comparator.comparingInt(ServerData::getPlayers))
                .findFirst()
                .orElse(null);
    }

    /**
     * Updates the volatile fields of the server data of this server.
     *
     * @return the updated server data
     */
    public ServerData updateSelfServerData() {
        if (thisServer == null)
            return null;

        thisServer.getDocument().put(ServerData.PLAYERS, UtilServer.getOnlinePlayers());
        thisServer.getDocument().put(ServerData.TPS, UtilServer.getTps());
        thisServer.setRequiredRank(plugin.getServerManager().getMinRank());
        return thisServer;
    }

    /**
     * Gets the server data manager which does all the heavy lifting behind this class.
     * <p>
     * It will be null if {@link SkirmishPlugin#isNetworkingServer()} is <code>false</code>
     *
     * @return server data manager
     */
    public ServerDataManager getServerDataManager() {
        return serverDataManager;
    }

}
