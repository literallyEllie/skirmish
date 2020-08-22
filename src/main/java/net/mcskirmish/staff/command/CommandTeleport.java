package net.mcskirmish.staff.command;

import com.google.common.collect.Lists;
import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.account.Account;
import net.mcskirmish.command.Command;
import net.mcskirmish.rank.impl.StaffRank;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTeleport extends Command {

    public CommandTeleport(SkirmishPlugin plugin) {
        super(plugin, "teleport", "Teleport to a player", StaffRank.ADMIN, Lists.newArrayList("tp"),
                "<player>", "[other player]");
    }

    @Override
    public void run(CommandSender sender, Account account, String usedLabel, String[] args) {
        Player target = getPlayer(sender, args[0]);
        if (target == null)
            return;

        Player other = null;

        if (args.length > 1) {
            other = getPlayer(sender, args[1]);
            if (other == null)
                return;
        } else if (account == null) {
            usage(sender, usedLabel);
            return;
        }

        if (other != null) {
            target.teleport(other);
        }


    }

}
