package net.mcskirmish;

import net.mcskirmish.account.Account;
import net.mcskirmish.util.C;
import org.bukkit.command.CommandSender;

public interface IInteractive {

    String getPrefix();

    /**
     * Sends a message to the player using the implemented {@link IInteractive#getPrefix()}
     * It is null safe
     *
     * @param sender  the sender to send to (can be null)
     * @param message the message to send
     */
    default void message(CommandSender sender, String message) {
        if (sender == null)
            return;
        sender.sendMessage(getPrefix() + " " + C.C + message);
    }

    /**
     * Sends a message to the player using the implemented {@link IInteractive#getPrefix()}
     * It is null safe
     *
     * @param account the account to send to (can be null)
     * @param message the message to send
     */
    default void message(Account account, String message) {
        message(account.getPlayer(), message);
    }

    /**
     * Sends a message to the player using a defined prefix
     * It is null safe
     *
     * @param sender  the sender to send to (can be null)
     * @param prefix  a custom prefix to send instead of {@link IInteractive#getPrefix()}
     * @param message the message to send
     */
    default void message(CommandSender sender, String prefix, String message) {
        if (sender == null)
            return;
        sender.sendMessage(prefix + message);
    }

}
