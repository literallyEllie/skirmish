package net.mcskirmish.rank;

import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.account.Account;
import net.mcskirmish.command.Command;
import net.mcskirmish.rank.impl.StaffRank;
import net.mcskirmish.util.P;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class CommandStaffRank extends Command {

    public CommandStaffRank(SkirmishPlugin plugin) {
        super(plugin, "staffrank", "Sets the staff rank of a player", StaffRank.ADMIN,
                "<player>", "<rank>");
    }

    @Override
    public void run(CommandSender sender, Account account, String usedLabel, String[] args) {
        final Optional<StaffRank> rankQuery = StaffRank.fromString(args[1]);
        if (!rankQuery.isPresent()) {
            couldNotFind(sender, "Rank " + args[1]);
            return;
        }

        final StaffRank rank = rankQuery.get();

        // if the rank is equal or above them
        if (sender instanceof Player &&
                rank.isHigherOrEqualTo(account.getStaffRank())) {
            message(sender, P.DENIED, "You cannot set a rank higher or equal to your own.");
            return;
        }

        Account target = getAccount(args[0]);
        if (target == null) {
            couldNotFind(sender, args[0]);
            return;
        }

        target.setStaffRank(rank);
        message(target, "Your rank has been updated to " + rank.getChatColor() + rank.getName());

        if (!sender.getName().equalsIgnoreCase(args[0]))
            message(sender, "Updated the rank of " + target.getName() + " to " + rank.getChatColor() + rank.getName());

        plugin.log(sender.getName() + " set rank of " + target.getName() + " to " + rank);
    }

}
