package net.mcskirmish.command.impl;

import com.google.common.collect.Lists;
import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.account.Account;
import net.mcskirmish.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class MicroCommand extends Command {

    private final String response;

    /**
     * Represents a command which returns a short message.
     * <p>
     * It will be sent using {@link net.mcskirmish.IInteractive#message(CommandSender, String)}
     *
     * @param plugin   plugin instance
     * @param label    label to execute
     * @param alias    ways to execute the command
     * @param response response message
     */
    public MicroCommand(SkirmishPlugin plugin, String label, List<String> alias, String response) {
        super(plugin, label, "Micro command", alias);
        this.response = response;
    }

    public MicroCommand(SkirmishPlugin plugin, String label, String response) {
        this(plugin, label, Lists.newArrayList(), response);
    }

    @Override
    public void run(CommandSender sender, Account account, String usedLabel, String[] args) {
        message(sender, response);
    }

}
