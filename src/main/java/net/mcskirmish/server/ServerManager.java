package net.mcskirmish.server;

import com.google.common.base.Preconditions;
import net.mcskirmish.Module;
import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.rank.IRank;
import net.mcskirmish.rank.impl.StaffRank;

public class ServerManager extends Module {

    private static final String PATH_SERVER_ID = "server-id";
    private static final String PATH_MIN_RANK = "min-rank";

    private String serverId;
    private IRank minRank;

    public ServerManager(SkirmishPlugin plugin) {
        super(plugin);
    }

    @Override
    protected void start() {
        serverId = plugin.getConfig().getString(PATH_SERVER_ID, null);
        minRank = StaffRank.valueOf(plugin.getConfig().getString(PATH_MIN_RANK, StaffRank.NONE.name()));

        Preconditions.checkNotNull(serverId, "server id must be defined!");
    }

    /**
     * Gets the server id of this server as read from file at startup.
     * All servers are required to have this.
     *
     * @return the id of this server
     */
    public String getServerId() {
        return serverId;
    }

    /**
     * Gets the min rank required to join the server
     * as enforced by {@link net.mcskirmish.account.AccountManager}
     *
     * @return the minimum rank to join
     */
    public IRank getMinRank() {
        if (minRank == null)
            minRank = StaffRank.NONE;
        return minRank;
    }

    /**
     * Sets the minimum rank to join the server and writes to file
     * <p>
     * Enforced by {@link net.mcskirmish.account.AccountManager}
     *
     * @param minRank min rank required to join the server
     */
    public void setMinRank(IRank minRank) {
        this.minRank = minRank;

        plugin.getConfig().set(PATH_MIN_RANK, minRank.id());
        plugin.saveConfig();
    }

}
