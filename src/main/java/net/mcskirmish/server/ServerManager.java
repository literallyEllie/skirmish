package net.mcskirmish.server;

import net.mcskirmish.Module;
import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.account.Rank;

import java.io.*;

public class ServerManager extends Module {

    private static final String PATH_MIN_RANK = "config" + File.separator + "min-rank.txt";
    private static final String PATH_SERVER_ID = "config" + File.separator + "server-id.txt";

    private String serverId;
    private Rank minRank;

    public ServerManager(SkirmishPlugin plugin) {
        super(plugin);
    }

    @Override
    protected void start() {

        try (BufferedReader reader = new BufferedReader(new FileReader(new File(PATH_SERVER_ID)))) {
            serverId = reader.readLine();
        } catch (IOException e) {
            plugin.error("failed to read server id file", e);
        }

        // min rank
        final File minRankFile = new File(PATH_MIN_RANK);
        if (minRankFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(minRankFile))) {
                minRank = Rank.valueOf(reader.readLine());
            } catch (IOException e) {
                plugin.error("failed to read from min rank file", e);
            }
        }

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
    public Rank getMinRank() {
        if (minRank == null)
            minRank = Rank.PLAYER;
        return minRank;
    }

    /**
     * Sets the minimum rank to join the server and writes to file
     *
     * Enforced by {@link net.mcskirmish.account.AccountManager}
     *
     * @param minRank min rank required to join the server
     */
    public void setMinRank(Rank minRank) {
        this.minRank = minRank;

        try (FileWriter writer = new FileWriter(new File(PATH_MIN_RANK))) {
            writer.write(minRank.name());
            writer.flush();
        } catch (IOException e) {
            plugin.error("failed to write min rank", e);
        }
    }


}
