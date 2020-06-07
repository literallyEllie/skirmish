package net.mcskirmish.staff.command;

import com.google.common.collect.Lists;
import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.account.Account;
import net.mcskirmish.account.Rank;
import net.mcskirmish.command.Command;
import net.mcskirmish.util.C;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandClearInventory extends Command {

    public CommandClearInventory(SkirmishPlugin plugin) {
        super(plugin, "clearinventory", "Clears a players inventory.", Rank.ADMIN, Lists.newArrayList("ci"), "[player]");
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

        target.getPlayer().getInventory().clear();
        target.getPlayer().getInventory().setArmorContents(null);

        message(target, "Your inventory has been cleared.");
        if (!target.equals(sender)) {
            message(sender, "Cleared " + C.V + target.getName() + C.C + "'s inventory.");
        }
    }

}
