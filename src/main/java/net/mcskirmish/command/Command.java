package net.mcskirmish.command;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import net.mcskirmish.IInteractive;
import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.account.Account;
import net.mcskirmish.account.Rank;
import net.mcskirmish.util.*;
import org.bukkit.ChatColor;
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

    public Command(SkirmishPlugin plugin, String name, String description, List<String> aliases, String... usage) {
        this(plugin, name, description, Rank.PLAYER, aliases, usage);
    }

    public Command(SkirmishPlugin plugin, String name, String description, Rank rank, String... usage) {
        this(plugin, name, description, rank, Lists.newArrayList(), usage);
    }

    protected void requiresPlayer() {
        requiresPlayer = true;
    }

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
            sender.sendMessage(C.IV + "There was an error executing that command, report to a member of staff");
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

    public final void usage(CommandSender sender, String aliasUsed) {
        message(sender, C.C + "Usage: " + C.V + "/" + aliasUsed + " " + usageSuffix);
    }

    public final void couldNotFind(CommandSender sender, String thing) {
        message(sender, M.NO_FOUND + thing);
    }

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
