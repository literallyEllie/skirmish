package net.mcskirmish.command;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import net.mcskirmish.IInteractive;
import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.account.Account;
import net.mcskirmish.account.Rank;
import net.mcskirmish.util.*;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public abstract class Command extends org.bukkit.command.Command implements CommandExecutor, TabCompleter, IInteractive {

    protected final SkirmishPlugin plugin;
    private final Rank requiredRank;
    private final int requiredArgs;
    private final String prefix, usageSuffix;

    private boolean requiresPlayer;

    /**
     * Represents a command which can be run by a player.
     * <p>
     * You can set whether this command can only be run by a player using {@link Command#requiresPlayer()}
     *
     * @param plugin      plugin instance
     * @param name        main label to execute
     * @param description brief description of what the command does
     * @param rank        the minimum rank required to execute the command
     * @param aliases     aliases of the command
     * @param usage       the varargs usage of the command in the format of:
     *                    "<...>", "[...]" etc.
     *                    Where <..> represents mandatory fields
     *                    And [..] represents optional fields
     *                    All the mandatory fields are counted and this is stored as {@link Command#requiredArgs}
     */
    public Command(SkirmishPlugin plugin, String name, String description, Rank rank, List<String> aliases, String... usage) {
        super(name);
        super.setDescription(description.endsWith(".") ? description : description + ".");
        super.setAliases(aliases);

        this.plugin = plugin;
        this.requiredRank = rank;
        this.requiredArgs = (int) Arrays.stream(usage).filter((s) -> s.contains("<")).count();
        this.prefix = P.MODULE + name.toUpperCase();
        this.usageSuffix = Joiner.on(" ").join(usage) + C.C + " - " + C.V + description;
    }

    /**
     * Alternative constructor for commands which can be run by anyone. ({@link Rank#PLAYER})
     *
     * @param plugin      plugin instance
     * @param name        main label to execute
     * @param description brief description of what the command does
     * @param aliases     aliases of the command
     * @param usage       the varargs usage of the command in the format of:
     *                    "<...>", "[...]" etc.
     *                    Where <..> represents mandatory fields
     *                    And [..] represents optional fields
     *                    All the mandatory fields are counted and this is stored as {@link Command#requiredArgs}
     */
    public Command(SkirmishPlugin plugin, String name, String description, List<String> aliases, String... usage) {
        this(plugin, name, description, Rank.PLAYER, aliases, usage);
    }

    /**
     * Alternative constructor for commands which have no aliases
     *
     * @param plugin      plugin instance
     * @param name        main label to execute
     * @param description brief description of what the command does
     * @param rank        the minimum rank required to execute the command
     * @param usage       the varargs usage of the command in the format of:
     *                    "<...>", "[...]" etc.
     *                    Where <..> represents mandatory fields
     *                    And [..] represents optional fields
     *                    All the mandatory fields are counted and this is stored as {@link Command#requiredArgs}
     */
    public Command(SkirmishPlugin plugin, String name, String description, Rank rank, String... usage) {
        this(plugin, name, description, rank, Lists.newArrayList(), usage);
    }

    /**
     * Sets that this command requires the {@link CommandSender} being an instance of a {@link Player}
     */
    protected void requiresPlayer() {
        requiresPlayer = true;
    }

    /**
     * Method called when all the preconditions have been met and is ready to be passed through
     * to the implementation.
     * <p>
     * Account will be null if sender is an instance of {@link org.bukkit.command.ConsoleCommandSender}
     * <p>
     * When this is called, you can assume:
     * - Account is not null <b>unless</b> the reason stated above
     * - If a {@link Player} they are at least {@link Command#requiredRank}
     * - They are a player <b>unless</b> {@link Command#requiresPlayer}
     * - They have at least {@link Command#requiredArgs} in their args
     *
     * @param sender    the sender of the command
     * @param account   the possible account of the player (may be null)
     * @param usedLabel the label they used to execute the command
     * @param args      the trailing arguments
     */
    public abstract void run(CommandSender sender, Account account, String usedLabel, String[] args);

    @Override
    public final boolean execute(CommandSender sender, String commandLabel, String[] args) {
        Account account = null;
        if (sender instanceof Player) {
            account = getAccount(sender);
            if (account == null) {
                ((Player) sender).kickPlayer(C.IC + "It looks like your data went missing..." +
                        "\nYou have been kicked to avoid further loss. \n\nJoin our Discord for support " + C.IV + Domain.DISCORD);
                return false;
            }

            if (!account.getRank().isHigherOrEqualTo(requiredRank)) {
                sender.sendMessage(M.noPerm(requiredRank));
                return true;
            }

        } else if (requiresPlayer) {
            sender.sendMessage("Requires player state");
            return true;
        }

        if (!checkArgs(sender, commandLabel, args))
            return true;

        try {
            run(sender, account, commandLabel, args);
        } catch (Throwable throwable) {
            plugin.error("error executing command " + getLabel(), throwable);
            message(sender, C.IV + "There was an error executing that command, report to a member of staff");
            return false;
        }

        return true;
    }

    @Override
    public final boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        return execute(sender, label, args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        return Lists.newArrayList();
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    protected Account getAccount(CommandSender sender) {
        return sender instanceof Player
                ? plugin.getAccountManager().getAccount((Player) sender)
                : null;
    }

    protected Account getAccount(String name) {
        return plugin.getAccountManager().getAccount(name, true);
    }

    protected boolean checkArgs(CommandSender commandSender, String command, String[] args) {
        boolean check = args.length >= requiredArgs;
        if (!check) {
            this.usage(commandSender, command);
        }

        return check;
    }

    /**
     * Sends the {@link CommandSender} a usage message
     *
     * @param sender    the sender to message
     * @param aliasUsed the alias they used
     */
    public final void usage(CommandSender sender, String aliasUsed) {
        message(sender, C.C + "Usage: " + C.V + "/" + aliasUsed + " " + usageSuffix);
    }

    /**
     * Sends the {@link CommandSender} a usage message
     *
     * @param sender the sender to message
     * @param thing  thing that could not be found
     */
    public final void couldNotFind(CommandSender sender, String thing) {
        message(sender, M.NO_FOUND + thing);
    }

    public final void specifyTarget(CommandSender sender) {
        message(sender, P.DENIED + "Please specify a target.");
    }

    /**
     * Gets the player by the name.
     * If the player cannot be found, it will send them an appropriate response.
     *
     * @param sender the {@link CommandSender} who requested the finding
     * @param name   the name of the target player
     * @return the player which matches that name (may be null)
     */
    public final Player getPlayer(CommandSender sender, String name) {
        final Player player = UtilPlayer.getP(name);
        if (player == null) {
            couldNotFind(sender, "Player " + name);
            return null;
        }

        return player;
    }

    // TODO other util methods

}
