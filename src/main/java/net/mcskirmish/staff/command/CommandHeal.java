package net.mcskirmish.staff.command;

import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.account.Account;
import net.mcskirmish.command.Command;
import net.mcskirmish.rank.impl.StaffRank;
import net.mcskirmish.util.C;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHeal extends Command {

    public CommandHeal(SkirmishPlugin plugin) {
        super(plugin, "heal", "Heals a player.", StaffRank.ADMIN, "[player]");
    }

    @Override
    public void run(CommandSender sender, Account account, String usedLabel, String[] args) {
        Player target;
        if (args.length == 0) {
            if (sender instanceof Player) {
                target = (Player) sender;
            } else {
                specifyTarget(sender);
                return;
            }
        } else {
            target = getPlayer(sender, args[0]);
            if (target == null) {
                return;
            }
        }

        target.getPlayer().setHealth(target.getPlayer().getMaxHealth());

        message(target, "You have been healed!");
        if (!target.equals(sender)) {
            message(sender, "Healed player " + C.V + target.getName());
        }

    }
}
