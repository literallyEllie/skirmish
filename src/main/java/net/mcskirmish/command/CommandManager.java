package net.mcskirmish.command;

import com.google.common.collect.Lists;
import net.mcskirmish.Module;
import net.mcskirmish.SkirmishPlugin;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;
import java.util.List;

public class CommandManager extends Module {

    private static final String COMMAND_ID = "skirmish";

    private List<Command> localCommands;
    private CommandMap commandMap;

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
            final Field commandMap = plugin.getServer().getClass().getDeclaredField("commandMap");
            commandMap.setAccessible(true);
            this.commandMap = (CommandMap) commandMap.get(plugin.getServer());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            plugin.error("failed to get commandMap", e);
        }
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
        if (commandMap == null)
            return;

        for (Command command : commands) {
            if (localCommands.contains(command)) {
                continue;
            }

            this.commandMap.register(COMMAND_ID, command);
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

        if (commandMap == null)
            return;
        this.commandMap.getCommand(command.getLabel()).unregister(commandMap);
    }

}
