package net.mcskirmish.chat.command;

import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.account.Account;
import net.mcskirmish.chat.ChatPolicy;
import net.mcskirmish.command.Command;
import net.mcskirmish.rank.impl.StaffRank;
import org.bukkit.command.CommandSender;

import java.util.Optional;

public class CommandChat extends Command {

    public CommandChat(SkirmishPlugin plugin) {
        super(plugin, "Chat", "Chat Control", StaffRank.MODERATOR, "<mute | clear | cooldown>", "[value]");
    }

    @Override
    public void run(CommandSender sender, Account account, String usedLabel, String[] args) {
        ChatPolicy policy = plugin.getChatManager().getChatPolicy();

        if (args[0].equalsIgnoreCase("clear")) {
            policy.clearChat(sender.getName(), false);
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

            final Optional<StaffRank> rank = StaffRank.fromString(args[1]);
            if (!rank.isPresent()) {
                message(sender, "Please specify a valid rank.");
                return;
            }

            policy.setRequiredRank(sender.getName(), false, rank.get());
            return;
        }

        usage(sender, usedLabel);
    }
}
