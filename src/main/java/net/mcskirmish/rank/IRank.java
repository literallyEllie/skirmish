package net.mcskirmish.rank;

import org.bukkit.ChatColor;

public interface IRank {

    String id();

    int weight();

    String getName();

    ChatColor getRankColor();

    ChatColor getChatColor();

    boolean isStaff();

    String getPrefix();

    default boolean isHigherOrEqualTo(IRank rank) {
        return weight() >= rank.weight();
    }

}
