package net.mcskirmish.command;

import com.google.common.collect.Lists;
import com.sun.deploy.panel.RuleSetViewerDialog;
import net.citizensnpcs.api.npc.MemoryNPCDataStore;
import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.account.Account;
import net.mcskirmish.account.Rank;
import net.mcskirmish.util.Domain;
import net.mcskirmish.util.UtilPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public abstract class Command extends org.bukkit.command.Command implements CommandExecutor, TabCompleter {

    protected final SkirmishPlugin plugin;
    private final Rank requiredRank;
    private final int requiredArgs;

    private boolean requiresPlayer;

    public Command(SkirmishPlugin plugin, String name, String description, Rank rank, List<String> aliases, String... usage) {
        super(name);
        super.setDescription(description.endsWith(".") ? description : description + ".");
        super.setAliases(aliases);

        this.plugin = plugin;
        this.requiredRank = rank;
        this.requiredArgs = (int) Arrays.stream(usage).filter((s) -> s.contains("<")).count();
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
                ((Player) sender).kickPlayer(ChatColor.RED + "It looks like your data went missing, " +
                        "you have been kicked to avoid further loss. Join our Discord for support " + Domain.DISCORD);
                return false;
            }

            if (!account.getRank().isHigherOrEqualTo(requiredRank)) {
                // TODO no perm message
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
            sender.sendMessage(ChatColor.RED + "There was an error executing that command, report to a member of staff");
            return true;
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

    protected Account getAccount(CommandSender sender) {
        return sender instanceof Player
                ? plugin.getAccountManager().getAccount((Player) sender)
                : null;
    }

    protected boolean checkArgs(CommandSender commandSender, String command, String[] args) {
        boolean check = args.length >= requiredArgs;
        if (!check) {
            this.usage(commandSender, command);
        }

        return check;
    }

    public void usage(CommandSender sender, String aliasUsed) {
        // TODO usage
        sender.sendMessage("Usage");
    }

    public void couldNotFind(CommandSender sender, String thing) {
        // TODO
        sender.sendMessage("Could not find " + thing);
    }

    public Player getPlayer(CommandSender sender, String name) {
        final Player player = UtilPlayer.getP(name);
        if (player == null) {
            couldNotFind(sender, "Player " + name);
            return null;
        }

        return player;
    }

    // TODO other util methods

}
