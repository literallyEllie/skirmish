package net.mcskirmish.util;

import org.bukkit.Bukkit;

public class UtilServer {

    public static int getOnlinePlayers() {
        return Bukkit.getOnlinePlayers().size();
    }

    public static int getMaxPlayers() {
        return Bukkit.getMaxPlayers();
    }

}
