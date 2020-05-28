package net.mcskirmish.command.commands;

import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.account.Account;
import net.mcskirmish.account.Rank;
import net.mcskirmish.command.Command;
import net.mcskirmish.util.F;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CommandTest extends Command {

    public CommandTest(SkirmishPlugin plugin) {
        super(plugin, "Test", "A simple test command.", Rank.ADMIN, Arrays.asList("test", "tst"), "/tst", "/test");
    }

    @Override
    public void run(CommandSender sender, Account account, String usedLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(F.f("&cThis command is being ran by CONSOLE!"));
            return;
        }

        account.sendMessage("TEST", ChatColor.YELLOW, "This is simply a test command...", "nothing much...");
        return;
    }
}
