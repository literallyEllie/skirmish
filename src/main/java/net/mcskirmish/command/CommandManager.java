package net.mcskirmish.command;

import com.google.common.collect.Lists;
import net.mcskirmish.Module;
import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.command.impl.CommandHelp;
import net.mcskirmish.command.impl.CommandList;
import net.mcskirmish.command.impl.CommandVersion;
import net.mcskirmish.command.impl.MicroCommand;
import net.mcskirmish.util.C;
import net.mcskirmish.util.Domain;
import org.bukkit.command.SimpleCommandMap;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class CommandManager extends Module {

    private static final String COMMAND_ID = "skirmish";

    private List<Command> localCommands;
    private Map<String, Command> knownCommands;
    private SimpleCommandMap commandMap;

    /**
     * Handles all API commands.
     * Commands should not be created outside of the provided API ({@link Command})
     * to keep consistency.
     * <p>
     * Commands can be registered and got using
     * Command#registerCommands and Command#getCommand respectively.
     *
     * @param plugin plugin instance
     */
    public CommandManager(SkirmishPlugin plugin) {
        super(plugin);
    }

    @Override
    protected void start() {
        localCommands = Lists.newArrayList();

        try {
            Field commandField = plugin.getServer().getClass().getDeclaredField("commandMap");
            commandField.setAccessible(true);
            commandMap = (SimpleCommandMap) commandField.get(plugin.getServer());
            // for vanilla
            Field knownField = commandMap.getClass().getDeclaredField("knownCommands");
            knownField.setAccessible(true);
            knownCommands = (Map<String, Command>) knownField.get(commandMap);
        } catch (NoSuchFieldException | NullPointerException | IllegalAccessException e) {
            plugin.error("failed to get knownCommands from commandMap", e);
        }

        // unregister vanilla
        unregisterVanilla("reload");
        unregisterVanilla("rl");
        unregisterVanilla("version");
        unregisterVanilla("ver");
        unregisterVanilla("about");
        unregisterVanilla("me");
        unregisterVanilla("say");
        unregisterVanilla("help");
        unregisterVanilla("?");

        // register core commands
        registerCommands(new CommandVersion(plugin), new CommandHelp(plugin), new CommandList(plugin));

        // Micro commands
        registerCommands(new MicroCommand(plugin, "discord", Lists.newArrayList("dc"),
                "You can join our Discord at " + C.V + plugin.getDomainProvider().get(Domain.DISCORD)));
    }

    @Override
    protected void stop() {
        localCommands.forEach(command -> unregisterCommand(command, true));
        localCommands.clear();
    }

    /**
     * Gets a command by its class as registered as a local command
     *
     * @param command the command class
     * @param <T>     the expected type
     * @return the command matching this class, may be null if no such command is registered.
     */
    public <T> Command getCommand(Class<T> command) {
        for (Command commands : localCommands) {
            if (commands.getClass().equals(command))
                return commands;
        }
        return null;
    }

    /**
     * Registers a varargs array of commands
     *
     * @param commands commands to register
     */
    public void registerCommands(Command... commands) {
        for (Command command : commands) {
            if (localCommands.contains(command)) {
                continue;
            }

            commandMap.register(command.getLabel(), COMMAND_ID, command);
            this.localCommands.add(command);
        }
    }

    /**
     * Unregisters a command from the command map.
     *
     * @param command the command to unregister
     * @param safe    if should remove from the local list of commands
     */
    public void unregisterCommand(Command command, boolean safe) {
        if (!safe)
            this.localCommands.remove(command);

        command.unregister(commandMap);
    }

    public void unregisterVanilla(String label) {
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            /*
            final org.bukkit.command.Command plainCmd = commandMap.getCommand(label);
            if (plainCmd != null)
                plainCmd.unregister(commandMap);
            final org.bukkit.command.Command minecraftCmd = commandMap.getCommand("minecraft:" + label);
            if (minecraftCmd != null)
                minecraftCmd.unregister(commandMap);
            final org.bukkit.command.Command bukkitCmd = commandMap.getCommand("bukkit:" + label);
            if (bukkitCmd != null)
                bukkitCmd.unregister(commandMap);

             */

            knownCommands.remove(label);
            knownCommands.remove("minecraft:" + label);
            knownCommands.remove("bukkit:" + label);
        }, 1L);
    }

}
