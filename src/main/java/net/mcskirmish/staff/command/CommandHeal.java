package net.mcskirmish.staff.command;

import com.google.common.collect.Lists;
import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.account.Account;
import net.mcskirmish.account.Rank;
import net.mcskirmish.command.Command;
import net.mcskirmish.util.P;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandHeal extends Command {


    public CommandHeal(SkirmishPlugin plugin) {
        super(plugin, "Heal", "Heals a player.", Rank.ADMIN, Lists.newArrayList("heal"), "[player]");
    }

    @Override
    public void run(CommandSender sender, Account account, String usedLabel, String[] args) {
        Account target = null;
        if (args.length == 0) {
            if (sender instanceof Player) {
                target = account;
            } else {
                specifyTarget(sender);
                return;
            }
        } else {
            target = plugin.getAccountManager().getAccount(getPlayer(sender, args[0]));
            if (target == null) {
               couldNotFind(sender, args[0]);
               return;
            }
        }


        if (target == account) {
            message(target, "You have been healed!");
        } else {
            message(sender, "Healed player " + target.getName());
        }

        target.getPlayer().setHealth(target.getPlayer().getMaxHealth());
    }

}
