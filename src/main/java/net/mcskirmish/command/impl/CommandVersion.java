package net.mcskirmish.command.impl;

import com.google.common.collect.Lists;
import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.account.Account;
import net.mcskirmish.account.Rank;
import net.mcskirmish.command.Command;
import net.mcskirmish.util.C;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class CommandVersion extends Command {

    public CommandVersion(SkirmishPlugin plugin) {
        super(plugin, "version", "See server version", Rank.DEVELOPER, Lists.newArrayList("ver"));
    }

    @Override
    public void run(CommandSender sender, Account account, String usedLabel, String[] args) {
        message(sender, "Running " + C.V + plugin.getServer().getName() + C.C + " version " + C.V + Bukkit.getVersion());
        message(sender, "Core plugin version: " + C.V + "v" + plugin.getDescription().getVersion());
    }

}
