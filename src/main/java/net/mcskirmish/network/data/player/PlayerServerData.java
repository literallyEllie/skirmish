package net.mcskirmish.network.data.player;

import net.mcskirmish.account.Account;
import net.mcskirmish.network.ServerGroup;
import org.bson.Document;

import java.util.UUID;

public class PlayerServerData {

    public static final String ID = "_id",
            NAME = "name",
            RANK = "rank",
            SERVER = "server",
            SERVER_GROUP = "server_group",
            SERVER_LOGIN = "server_login";

    private final Document document;

    public PlayerServerData(Document document) {
        this.document = document;
    }

    public PlayerServerData(Account account, ServerGroup group) {
        this(new Document(ID, account.getUuid())
                .append(NAME, account.getName())
                .append(RANK, account.getRank().id())
                .append(SERVER, account.getLastServer())
                .append(SERVER_GROUP, group.name())
                .append(SERVER_LOGIN, System.currentTimeMillis()));
    }

    public UUID getUuid() {
        return document.get(ID, UUID.class);
    }

    public String getName() {
        return document.getString(NAME);
    }

    public String getRank() {
        return document.getString(RANK);
    }

    public String getServer() {
        return document.getString(SERVER);
    }

    public String getServerGroup() {
        return document.getString(SERVER_GROUP);
    }

    public Document getDocument() {
        return document;
    }

}
