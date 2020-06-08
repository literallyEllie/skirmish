package net.mcskirmish.command.commands;

import com.google.common.collect.Lists;
import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.account.Account;
import net.mcskirmish.account.Rank;
import net.mcskirmish.command.Command;
import org.bukkit.command.CommandSender;

import javax.crypto.SecretKey;

public class CommandHelp extends Command {

    public CommandHelp(SkirmishPlugin plugin) {
        super(plugin, "help", "Help menu", Lists.newArrayList("?"));
    }

    @Override
    public void run(CommandSender sender, Account account, String usedLabel, String[] args) {
        sender.sendMessage("todo");
        // TODO some nice looking chat interface
    }

}