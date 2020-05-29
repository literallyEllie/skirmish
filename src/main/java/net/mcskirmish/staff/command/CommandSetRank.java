package net.mcskirmish.staff.command;

import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.account.Account;
import net.mcskirmish.account.Rank;
import net.mcskirmish.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class CommandSetRank extends Command {

    public CommandSetRank(SkirmishPlugin plugin) {
        super(plugin, "setrank", "Sets rank of a player", Rank.ADMIN, "<player>", "<rank>");
    }

    @Override
    public void run(CommandSender sender, Account account, String usedLabel, String[] args) {
        Optional<Rank> rankQuery = Rank.fromString(args[1]);
        if (!rankQuery.isPresent()) {
            couldNotFind(sender, "Rank " + args[1]);
            return;
        }
        Rank rank = rankQuery.get();

        // if the rank is equal or above them
        if (sender instanceof Player &&
                account.getRank().isHigherOrEqualTo(rank)) {

            // TODO no perm format
            msg(sender, "You cannot set a rank higher or equal to your own.");
            return;
        }

        Account target = getAccount(args[0]);
        if (target == null){
            couldNotFind(sender, args[0]);
            return;
        }

        target.setRank(rank);
        msg(target, "Your rank has been updated to " + rank.getPrefix());
        msg(sender, "Updated the rank of " + target.getName() + " to " + rank.getPrefix());

        plugin.log(sender.getName() + " set rank of " + target.getName() + " to " + rank);
    }

}
