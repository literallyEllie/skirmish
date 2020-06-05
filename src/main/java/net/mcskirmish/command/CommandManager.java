package net.mcskirmish.command;

import com.google.common.collect.Lists;
import net.mcskirmish.Module;
import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.util.UtilReflect;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;
import java.util.List;

public class CommandManager extends Module {

    private static final String COMMAND_ID = "skirmish";

    private List<Command> localCommands;
    private CommandMap commandMap;

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

    public <T> Command getCommand(Class<T> command) {
        for (Command commands : localCommands) {
            if (commands.getClass().equals(command))
                return commands;
        }
        return null;
    }

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

    public void unregisterCommand(Command command, boolean safe) {
        if (!safe)
            this.localCommands.remove(command);

        if (commandMap == null)
            return;
        this.commandMap.getCommand(command.getLabel()).unregister(commandMap);
    }

}
