package net.mcskirmish.staff;

import net.mcskirmish.rank.impl.StaffRank;
import org.bukkit.ChatColor;

public enum StaffChannel {

    STAFF(ChatColor.AQUA + ChatColor.BOLD.toString(), StaffRank.ADMIN),
    ADMIN(ChatColor.RED + ChatColor.BOLD.toString(), StaffRank.ADMIN);

    private final StaffRank required;
    private final String prefix;

    StaffChannel(String prefix, StaffRank requiredRank) {
        this.required = requiredRank;
        this.prefix = prefix + name() + " ";
    }

    public String getPrefix() {
        return prefix;
    }

    public StaffRank getRequired() {
        return required;
    }

    public String formatMessage(String sender, String message) {
        return prefix + sender + ChatColor.RESET + ": " + message;
    }

}
