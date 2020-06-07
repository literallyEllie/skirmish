package net.mcskirmish.staff.command;

import com.google.common.collect.Lists;
import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.account.Account;
import net.mcskirmish.account.Rank;
import net.mcskirmish.command.Command;
import net.mcskirmish.util.P;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class CommandRank extends Command {

    public CommandRank(SkirmishPlugin plugin) {
        super(plugin, "rank", "Sets rank of a player", Rank.ADMIN, Lists.newArrayList("setrank"),
                "<player>", "<rank>");
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
                rank.isHigherOrEqualTo(account.getRank())) {

            message(sender, P.DENIED, "You cannot set a rank higher or equal to your own.");
            return;
        }

        Account target = getAccount(args[0]);
        if (target == null) {
            couldNotFind(sender, args[0]);
            return;
        }

        target.setRank(rank);
        message(target, "Your rank has been updated to " + rank.getPrefix());

        if (!sender.getName().equalsIgnoreCase(args[0]))
            message(sender, "Updated the rank of " + target.getName() + " to " + rank.getPrefix());

        plugin.log(sender.getName() + " set rank of " + target.getName() + " to " + rank);
    }

}
