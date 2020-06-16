package net.mcskirmish.command.impl;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.account.Account;
import net.mcskirmish.command.Command;
import net.mcskirmish.util.C;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

public class CommandList extends Command {

    public CommandList(SkirmishPlugin plugin) {
        super(plugin, "list", "List online players", Lists.newArrayList("online"));
    }

    @Override
    public void run(CommandSender sender, Account account, String usedLabel, String[] args) {
        // todo check vanish states
        message(sender, C.V + plugin.getServer().getOnlinePlayers().size() + C.C + " online");
        sender.sendMessage(Joiner.on(C.C + ", ").join(plugin.getServer().getOnlinePlayers().stream()
                .map(Player::getDisplayName)
                .collect(Collectors.toList())));
    }

}
