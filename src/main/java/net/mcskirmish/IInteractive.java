package net.mcskirmish;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface IInteractive {

    String getPrefix();

    default void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(getPrefix() + "> " + message);
    }

}
