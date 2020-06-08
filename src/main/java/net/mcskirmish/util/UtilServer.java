package net.mcskirmish.util;

import org.bukkit.Bukkit;

public class UtilServer {

    public static int getOnlinePlayers() {
        return Bukkit.getOnlinePlayers().size();
    }

    public static int getMaxPlayers() {
        return Bukkit.getMaxPlayers();
    }

    public static void broadcast(String string) {
        // TODO
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(string);
        Bukkit.broadcastMessage("");
    }

}
