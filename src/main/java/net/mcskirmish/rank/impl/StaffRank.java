package net.mcskirmish.rank.impl;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.Optional;

public enum StaffRank implements net.mcskirmish.rank.IRank {

    NONE("Player", ChatColor.WHITE),
    YOUTUBE("YouTube", ChatColor.WHITE),
    TWITCH("Twitch", ChatColor.DARK_PURPLE),
    MODERATOR("Mod", ChatColor.YELLOW),
    DEVELOPER("Developer", ChatColor.BLUE),
    ADMIN("Admin", ChatColor.RED),
    OWNER("Owner", ChatColor.DARK_RED);

    private final String name;
    private final ChatColor rankColor, chatColor;

    /**
     * Represents a preset rank which is present throughout every server
     * It is done in the order of hierarchy from lowest to highest.
     * <p>
     * To compare ranks use {@link net.mcskirmish.rank.IRank#isHigherOrEqualTo(net.mcskirmish.rank.IRank)}
     * <p>
     * This is not the lowest level of accounts, and this is dependant on the implementation.
     *
     * @param name      name of the rank
     * @param rankColor rank prefix color
     * @param userColor rank username color
     */
    StaffRank(String name, ChatColor rankColor, ChatColor userColor) {
        this.name = name;
        this.rankColor = rankColor;
        this.chatColor = userColor;
    }

    StaffRank(String name, ChatColor rankColor) {
        this(name, rankColor, ChatColor.WHITE);
    }

    public static Optional<StaffRank> fromString(String string) {
        String upper = string.toUpperCase();
        return Arrays.stream(values())
                .filter(rank -> rank.name().equals(upper) || rank.getName().toUpperCase().equals(upper))
                .findFirst();
    }

    @Override
    public String id() {
        return name();
    }

    @Override
    public int weight() {
        return ordinal();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ChatColor getRankColor() {
        return this.rankColor;
    }

    @Override
    public ChatColor getChatColor() {
        return chatColor;
    }

    @Override
    public boolean isStaff() {
        return isHigherOrEqualTo(ADMIN);
    }

    @Override
    public String getPrefix() {
        return getRankColor() + "[" + getName() + "]" + getChatColor();
    }

}
