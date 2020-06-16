package net.mcskirmish.network.event;

import net.mcskirmish.account.Account;
import net.mcskirmish.event.SkirmishEvent;

public class PlayerQuitNetworkEvent extends SkirmishEvent {

    private final Account account;

    /**
     * Called when a players leaves the server with a {@link Account#getDestinationServer()} being
     * equal to {@link net.mcskirmish.network.NetworkManager#DEST_NONE}
     *
     * @param account account who left
     */
    public PlayerQuitNetworkEvent(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

}
