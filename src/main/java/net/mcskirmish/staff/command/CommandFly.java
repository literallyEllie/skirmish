package net.mcskirmish.staff.command;

import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.account.Account;
import net.mcskirmish.command.Command;
import net.mcskirmish.rank.impl.StaffRank;
import net.mcskirmish.util.C;
import net.mcskirmish.util.M;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandFly extends Command {

    public CommandFly(SkirmishPlugin plugin) {
        super(plugin, "fly", "Toggle flying mode for a player", StaffRank.MODERATOR, "[player]");
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
            if (account != null && !account.getStaffRank().isHigherOrEqualTo(StaffRank.ADMIN)) {
                sender.sendMessage(M.noPerm(StaffRank.ADMIN));
                return;
            }

            target = getPlayer(sender, args[0]);
            if (target == null) {
                return;
            }
        }

        boolean newState = !target.getAllowFlight();
        target.setAllowFlight(newState);
        target.setFlying(newState);

        message(target, "You can " + C.V + (newState ? "now" : "no longer") + C.C + " fly.");
        if (!target.equals(sender)) {
            message(sender, C.V + target.getName() + C.C + " can " + C.V + (newState ? "now" : "no longer") + C.C + " fly.");
        }

    }

}
