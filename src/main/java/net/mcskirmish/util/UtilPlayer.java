package net.mcskirmish.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class UtilPlayer {

    public static Player getP(UUID uuid) {
        return Bukkit.getServer().getPlayer(uuid);
    }

    public static Player getP(String name) {
        return Bukkit.getServer().getPlayer(name);
    }

    public static Player getPExact(String name) {
        return Bukkit.getServer().getPlayerExact(name);
    }

}
