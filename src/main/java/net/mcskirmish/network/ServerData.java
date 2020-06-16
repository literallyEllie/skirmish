package net.mcskirmish.network;

import net.mcskirmish.account.Rank;
import org.bson.Document;

public class ServerData {

    public static final String ID = "_id",
            GROUP = "group",
            PUBLIC_IP = "public_ip",
            PLAYERS = "players",
            MAX_PLAYERS = "max_players",
            TPS = "tps",
            REQUIRED_RANK = "required_rank";

    private final Document document;
    private final ServerGroup serverGroup;
    private Rank requiredRank;

    /**
     * Represents an online server on the network.
     *
     * @param id         server id
     * @param group      server group
     * @param publicIp   public ip of the server
     * @param players    online players
     * @param maxPlayers max players
     * @param tps        most recent tps
     * @param rank       required rank to join the server
     */
    public ServerData(String id, ServerGroup group, String publicIp, int players, int maxPlayers, double tps, Rank rank) {
        this.document = new Document(ID, id)
                .append(GROUP, (serverGroup = group).name())
                .append(PUBLIC_IP, publicIp)
                .append(PLAYERS, players)
                .append(MAX_PLAYERS, maxPlayers)
                .append(TPS, tps)
                .append(REQUIRED_RANK, (requiredRank = rank).name());
    }

    public ServerData(Document document) {
        this.document = document;
        this.serverGroup = ServerGroup.valueOf(document.getString(GROUP));
        this.requiredRank = Rank.valueOf(document.getString(REQUIRED_RANK));
    }

    public String getId() {
        return document.getString(ID);
    }

    public ServerGroup getServerGroup() {
        return serverGroup;
    }

    public String getPublicIp() {
        return document.getString(PUBLIC_IP);
    }

    public int getPlayers() {
        return document.getInteger(PLAYERS);
    }

    public int getMaxPlayers() {
        return document.getInteger(MAX_PLAYERS);
    }

    public boolean isFull() {
        return getPlayers() <= getMaxPlayers();
    }

    public double getTps() {
        return document.getDouble(TPS);
    }

    public Rank getRequiredRank() {
        return requiredRank;
    }

    public void setRequiredRank(Rank rank) {
        document.append(REQUIRED_RANK, (requiredRank = rank).name());
    }

    public Document getDocument() {
        return document;
    }

}
