package net.mcskirmish.staff;

import net.mcskirmish.account.Rank;
import org.bukkit.ChatColor;

public enum StaffChannel {

    STAFF(ChatColor.AQUA + ChatColor.BOLD.toString(), Rank.ADMIN),
    ADMIN(ChatColor.RED + ChatColor.BOLD.toString(), Rank.ADMIN);

    private final Rank required;
    private final String prefix;

    StaffChannel(String prefix, Rank requiredRank) {
        this.required = requiredRank;
        this.prefix = prefix + name() + " ";
    }

    public String getPrefix() {
        return prefix;
    }

    public Rank getRequired() {
        return required;
    }

    public String formatMessage(String sender, String message) {
        return prefix + sender + ChatColor.RESET + ": " + message;
    }

}
