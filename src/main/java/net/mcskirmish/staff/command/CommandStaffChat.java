package net.mcskirmish.staff.command;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.account.Account;
import net.mcskirmish.command.Command;
import net.mcskirmish.staff.StaffChannel;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandStaffChat extends Command {

    private final StaffChannel channel;

    public CommandStaffChat(SkirmishPlugin plugin, StaffChannel channel) {
        super(plugin, channel.name() + "chat", "Sends messages to " + channel.name().toLowerCase() + " channel", channel.getRequired(),
                Lists.newArrayList(channel.name().substring(0, 1).toLowerCase() + "c"), "<message>");
        this.channel = channel;
    }

    @Override
    public void run(CommandSender sender, Account account, String usedLabel, String[] args) {
        String message = ChatColor.translateAlternateColorCodes('&', Joiner.on(" ").join(args));
        plugin.getStaffManager().sendStaffChat(channel, sender instanceof Player ? account : sender, message);
    }

}
