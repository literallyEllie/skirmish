package net.mcskirmish.staff.command;

import com.google.common.collect.Lists;
import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.account.Account;
import net.mcskirmish.account.Rank;
import net.mcskirmish.command.Command;
import net.mcskirmish.util.C;
import net.mcskirmish.util.UtilServer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CommandBroadcast extends Command {

    public CommandBroadcast(SkirmishPlugin plugin) {
        super(plugin, "Broadcast", "Broadcast a message locally.", Rank.ADMIN, Lists.newArrayList("bc"), "<message>");
    }

    @Override
    public void run(CommandSender sender, Account account, String usedLabel, String[] args) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            builder.append(args[i] + " ");
        }

        String message = builder.substring(0, builder.length() - 1); // Remove the last " "
        UtilServer.broadcast(ChatColor.BOLD + C.IV + sender.getName() + ": " + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', message));
    }
}
