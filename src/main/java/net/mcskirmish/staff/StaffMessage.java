package net.mcskirmish.staff;

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
