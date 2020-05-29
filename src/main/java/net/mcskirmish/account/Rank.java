package net.mcskirmish.account;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.Optional;

public enum Rank {

    PLAYER("Player", ChatColor.WHITE),
    ADMIN("Admin", ChatColor.RED),
    OWNER("Owner", ChatColor.DARK_RED);

    private final String name;
    private final ChatColor rankColor, chatColor; // TODO Maybe make rank colour or user colour optional?

    Rank(String name, ChatColor rankColor, ChatColor userColor) {
        this.name = name;
        this.rankColor = rankColor;
        this.chatColor = userColor;
    }

    Rank(String name, ChatColor rankColor) {
        this(name, rankColor, ChatColor.WHITE);
    }

    public static Optional<Rank> fromString(String string) {
        String upper = string.toUpperCase();
        return Arrays.stream(values())
                .filter(rank -> rank.name().equals(upper) || rank.getName().toUpperCase().equals(upper))
                .findFirst();
    }

    public String getName() {
        return this.name;
    }

    public ChatColor getRankColor() {
        return this.rankColor;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public boolean isDefault() {
        return this == PLAYER;
    }

    public boolean isStaff() {
        return isHigherOrEqualTo(ADMIN);
    }

    public String getPrefix() {
        return getRankColor() + (isDefault()
                ? ""
                : "[" + getName() + "]") + getChatColor();
    }

    public boolean isHigherOrEqualTo(Rank rank) {
        return this.ordinal() >= rank.ordinal();
    }

}
