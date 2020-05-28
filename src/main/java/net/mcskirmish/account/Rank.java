package net.mcskirmish.account;

import org.bukkit.ChatColor;

public enum Rank {

    PLAYER("Player", ChatColor.WHITE, ChatColor.WHITE, false, 0),
    ADMIN("Admin", ChatColor.RED, ChatColor.WHITE, true, 1),
    OWNER("Owner", ChatColor.DARK_RED, ChatColor.WHITE, true, Integer.MAX_VALUE);

    private String name;
    private ChatColor rankColor, userColor; // TODO Maybe make rank colour or user colour optional?
    private boolean hasPrefix;
    private int permissionLevel;

    Rank(String name, ChatColor rankColor, ChatColor userColor, boolean hasPrefix, int permissionLevel) {
        this.name = name;
        this.rankColor = rankColor;
        this.userColor = userColor;
        this.hasPrefix = hasPrefix;
        this.permissionLevel = permissionLevel;
    }

    public String getName() {
        return this.name;
    }

    public ChatColor getRankColor() {
        return this.rankColor;
    }

    public ChatColor getUserColor() {
        return this.userColor;
    }

    public boolean hasPrefix() {
        return this.hasPrefix;
    }

    public int getPermissionLevel() {
        return permissionLevel;
    }

    public String getPrefix() {
        if (!hasPrefix) return null;
        return getRankColor() + "[" + getName() + "]" + getUserColor();
    }
}
