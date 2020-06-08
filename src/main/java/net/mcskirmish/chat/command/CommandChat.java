package net.mcskirmish.chat.command;

import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.account.Account;
import net.mcskirmish.account.Rank;
import net.mcskirmish.chat.ChatPolicy;
import net.mcskirmish.command.Command;
import org.bukkit.command.CommandSender;

public class CommandChat extends Command {

    public CommandChat(SkirmishPlugin plugin) {
        super(plugin, "Chat", "Chat Control", Rank.ADMIN, "<mute/clear/cooldown>", "[value]");
    }

    @Override
    public void run(CommandSender sender, Account account, String usedLabel, String[] args) {
        ChatPolicy policy = plugin.getChatManager().getChatPolicy();

        if (args[0].equalsIgnoreCase("clear")) {
            policy.clearChat(sender.getName());
            return;
        }

        if (args[0].toLowerCase().matches("cooldown|cd|delay")) {
            if (args.length < 2) {
                message(sender, "Please specify a cooldown time in seconds.");
                return;
            }

            int value = 0;

            try {
                value = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                message(sender, "Invalid time '" + args[1] + "'.");
                return;
            }

            policy.setChatDelay(sender.getName(), false, value);
            return;
        }

        if (args[0].equalsIgnoreCase("mute")) {
            if (args.length < 2) {
                message(sender, "Please specify a valid rank.");
                return;
            }

            Rank rank = Rank.valueOf(args[1]);
            if (rank == null) {
                message(sender, "Please specify a valid rank.");
                return;
            }

            policy.setRequiredRank(sender.getName(), false, rank);
            return;
        }

        usage(sender, usedLabel);
    }
}
