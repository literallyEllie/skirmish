package net.mcskirmish.bungee;

import net.md_5.bungee.api.plugin.Plugin;

public class SkirmishBungee extends Plugin {

    @Override
    public void onEnable() {
        getLogger().info("SkirmishBungee v" + getDescription().getVersion() + " started");
    }

}
