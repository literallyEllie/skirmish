package net.mcskirmish.staff.command;

import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.account.Account;
import net.mcskirmish.account.Rank;
import net.mcskirmish.command.Command;
import net.mcskirmish.util.C;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandFeed extends Command {

    public CommandFeed(SkirmishPlugin plugin) {
        super(plugin, "feed", "Feeds a player.", Rank.ADMIN, "[player]");
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

        target.getPlayer().setFoodLevel(20);

        message(target, "You have been fed.");
        if (!target.equals(sender)) {
            message(sender, "Fed player " + C.V + target.getName() + C.C + ".");
        }
    }

}
