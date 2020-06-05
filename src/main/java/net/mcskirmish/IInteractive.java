package net.mcskirmish;

import net.mcskirmish.account.Account;
import net.mcskirmish.util.C;
import org.bukkit.command.CommandSender;

public interface IInteractive {

    String getPrefix();

    default void message(CommandSender sender, String message) {
        if (sender == null)
            return;
        sender.sendMessage(getPrefix() + " " + C.C + message);
    }

    default void message(Account account, String message) {
        message(account.getPlayer(), message);
    }

    default void message(CommandSender sender, String prefix, String message) {
        if (sender == null)
            return;
        sender.sendMessage(prefix + message);
    }

}
