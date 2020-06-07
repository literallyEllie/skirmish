package net.mcskirmish.staff.command;

import com.google.common.collect.Lists;
import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.account.Account;
import net.mcskirmish.account.Rank;
import net.mcskirmish.command.Command;
import net.mcskirmish.util.C;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandGameMode extends Command {

    public CommandGameMode(SkirmishPlugin plugin) {
        super(plugin, "Gamemode", "Sets a players gamemode", Rank.ADMIN, Lists.newArrayList("gm"), "<creative/survival/adventure/spectator>", "[player]");
    }

    @Override
    public void run(CommandSender sender, Account account, String usedLabel, String[] args) {
        Player target;
        GameMode mode = null;

        if (args[0].matches("survival|s|0")) mode = GameMode.SURVIVAL;
        if (args[0].matches("creative|c|1")) mode = GameMode.CREATIVE;
        if (args[0].matches("adventure|a|2")) mode = GameMode.ADVENTURE;
        if (args[0].matches("spectator|spec|3")) mode = GameMode.SPECTATOR;

        if (mode == null) {
            couldNotFind(sender, "Gamemode " + args[0]);
            return;
        }

        if (args.length < 2) {
            if (sender instanceof Player) {
                target = (Player) sender;
            } else {
                specifyTarget(sender);
                return;
            }
        } else {
            target = getPlayer(sender, args[1]);
            if (target == null) return;
        }

        target.setGameMode(mode);

        if (target.equals(sender)) {
            message(sender, "Your gamemode has been set to " + C.V + WordUtils.capitalize(mode.toString().toLowerCase()) + "!");
        } else {
            message(target, "Your gamemode has been set to " + C.V + WordUtils.capitalize(mode.toString().toLowerCase()) + "!");
            message(sender, "You set " + target.getName() + "'s gamemode to " + C.V + WordUtils.capitalize(mode.toString().toLowerCase()) + "!");
        }
    }

}
