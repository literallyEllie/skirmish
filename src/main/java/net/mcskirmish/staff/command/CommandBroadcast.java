package net.mcskirmish.staff.command;

import com.google.common.base.Joiner;
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
        UtilServer.broadcast( C.IV + ChatColor.BOLD + sender.getName() + ": " + ChatColor.RESET +
                ChatColor.translateAlternateColorCodes('&', Joiner.on(" ").join(args)));
    }

}
