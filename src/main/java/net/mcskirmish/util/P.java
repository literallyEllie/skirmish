package net.mcskirmish.util;

import org.bukkit.ChatColor;

public class P {

    /**
     * Represents prefixes used across the network.
     *
     * Their suffix uses the generic network color scheme ({@link C}) but can be ignored.
     */
    P() {
    }

    public static final String MODULE = ChatColor.GREEN.toString() + ChatColor.BOLD;
    public static final String DENIED = ChatColor.RED.toString() + ChatColor.BOLD + "ACCESS DENIED " + C.IC;
    public static final String ALERT = ChatColor.RED.toString() + ChatColor.BOLD + "ALERT " + C.IV;

}
