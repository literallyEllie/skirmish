package net.mcskirmish.staff;

import net.mcskirmish.Module;
import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.account.Account;
import net.mcskirmish.staff.command.*;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class StaffManager extends Module {

    private RedisStaffChat redisStaffChat;

    public StaffManager(SkirmishPlugin plugin) {
        super(plugin);
    }

    @Override
    protected void start() {
        plugin.getCommandManager().registerCommands(
                new CommandRank(plugin), new CommandHeal(plugin), new CommandFly(plugin), new CommandFeed(plugin), new CommandClearInventory(plugin)
        );

        // staff chat init
        redisStaffChat = new RedisStaffChat(plugin, this);
        for (StaffChannel channel : StaffChannel.values()) {
            plugin.getCommandManager().registerCommands(new CommandStaffChat(plugin, channel));
        }

    }

    public void sendStaffChat(StaffChannel channel, Object sender, String message) {
        String senderName;
        if (sender instanceof Account) {
            senderName = ((Account) sender).getRank().getPrefix() + " " + ((Account) sender).getName();
        } else if (sender instanceof ConsoleCommandSender) {
            senderName = ChatColor.RED + "[Console]";
        } else if (sender instanceof Player) {
            senderName = ((Player) sender).getDisplayName();
        } else
            senderName = ChatColor.GRAY + sender.toString();

        if (plugin.isNetworkingServer()) {
            redisStaffChat.publish(new StaffMessage(channel, senderName, message));
        } else
            displayStaffChat(channel, channel.formatMessage(senderName, message));
    }

    public void displayStaffChat(StaffChannel channel, String message) {
        plugin.getAccountManager().getAccounts().stream().filter(account -> account.getRank().isHigherOrEqualTo(channel.getRequired()))
                .forEach(account -> account.sendMessage(message));
    }

}
