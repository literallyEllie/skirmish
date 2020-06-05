package net.mcskirmish.staff;

import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.redis.RedisSubscriber;

public class RedisStaffChat extends RedisSubscriber<StaffMessage, StaffManager> {

    public RedisStaffChat(SkirmishPlugin plugin) {
        super(StaffMessage.class, plugin, plugin.getStaffManager(), "STAFFCHAT");
    }

    @Override
    public void receive(StaffMessage data) {
        final StaffChannel channel = StaffChannel.valueOf(data.getChannel());
        plugin.getStaffManager().displayStaffChat(
                channel,
                channel.formatMessage(data.getSender(), data.getMessage())
        );
    }

}
