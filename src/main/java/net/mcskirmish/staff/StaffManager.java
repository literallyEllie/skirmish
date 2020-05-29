package net.mcskirmish.staff;

import net.mcskirmish.Module;
import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.command.Command;
import net.mcskirmish.staff.command.CommandSetRank;

public class StaffManager extends Module {

    public StaffManager(SkirmishPlugin plugin) {
        super(plugin);
    }

    @Override
    protected void start() {

        plugin.getCommandManager().registerCommands(
                new CommandSetRank(plugin)
        );

    }

}
