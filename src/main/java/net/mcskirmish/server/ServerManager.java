package net.mcskirmish.server;

import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import net.mcskirmish.Module;
import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.account.Rank;
import net.mcskirmish.util.UtilJson;

import java.io.*;
import java.util.Map;

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
            plugin.error("Failed to read server ID file!", e);
        }

        // min rank
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(PATH_MIN_RANK)))) {
            minRank = Rank.valueOf(reader.readLine());
        } catch (IOException e) {
            plugin.error("Failed to read from min rank file!", e);
        }

    }

    public String getServerId() {
        return serverId;
    }

    public Rank getMinRank() {
        return minRank;
    }

    public void setMinRank(Rank minRank) {
        this.minRank = minRank;

        try (FileWriter writer = new FileWriter(new File(PATH_MIN_RANK))) {
            writer.write(minRank.name());
        } catch (IOException e) {
            plugin.error("failed to write min rank", e);
        }
    }


}
