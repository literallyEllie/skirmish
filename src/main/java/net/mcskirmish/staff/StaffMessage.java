package net.mcskirmish.staff;

import com.sun.corba.se.impl.naming.cosnaming.InternalBindingKey;
import net.mcskirmish.account.Account;
import net.mcskirmish.account.Rank;

public class StaffMessage {

    private final String channel, sender, message;

    public StaffMessage(StaffChannel channel, String sender, String message) {
        this.channel = channel.name();
        this.sender = sender;
        this.message = message;
    }

    public String getChannel() {
        return channel;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

}
