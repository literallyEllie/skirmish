package net.mcskirmish.network.event;

import net.mcskirmish.account.Account;
import net.mcskirmish.event.SkirmishEvent;

public class PlayerSwitchServerEvent extends SkirmishEvent {

    private final Account account;
    private final String destination;

    /**
     * Called when a players leaves the server with a {@link Account#getDestinationServer()} being
     * <b>not</b> equal to {@link net.mcskirmish.network.NetworkManager#DEST_NONE}
     *
     * @param account     account who left
     * @param destination the server id to which they are going to
     */
    public PlayerSwitchServerEvent(Account account, String destination) {
        this.account = account;
        this.destination = destination;
    }

    public Account getAccount() {
        return account;
    }

    public String getDestination() {
        return destination;
    }

}
