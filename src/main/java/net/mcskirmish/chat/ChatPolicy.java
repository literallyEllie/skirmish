package net.mcskirmish.chat;

import net.mcskirmish.account.Account;
import net.mcskirmish.rank.impl.StaffRank;
import net.mcskirmish.util.C;
import net.mcskirmish.util.UtilServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class ChatPolicy {

    private String format;
    private int chatDelay;
    private net.mcskirmish.rank.IRank requiredRank;

    /**
     * The base implementation of a {@link ChatPolicy}
     * <p>
     * Controls how the chat functions.
     */
    public ChatPolicy() {
        this.format = "{display}" + ChatColor.DARK_GRAY + "> {message}";
        this.chatDelay = 3;
        this.requiredRank = StaffRank.NONE;
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getChatDelay() {
        return this.chatDelay;
    }

    public net.mcskirmish.rank.IRank getRequiredRank() {
        return this.requiredRank;
    }

    public void setChatDelay(String executorName, boolean silent, int delay) {
        if (!silent) {
            UtilServer.broadcast(C.IV + executorName + C.IC + " has set the chat delay to " + C.IV + delay + " seconds.");
        }

        this.chatDelay = delay;
    }

    public boolean hasChatDelay() {
        return chatDelay > 0;
    }

    public void setRequiredRank(String executorName, boolean silent, net.mcskirmish.rank.IRank rank) {
        if (!silent) {
            UtilServer.broadcast(C.IV + executorName + C.IC + " has set the minimum required rank to chat to " + rank.getPrefix() + "+!");
        }

        this.requiredRank = rank;
    }

    public void clearChat(String executorName, boolean silent) {
        if (!silent) {
            UtilServer.broadcast(C.IV + executorName + C.IC + " has cleared the chat.");
        }

        for (int i = 0; i < 100; i++) {
            Bukkit.broadcastMessage("");
        }
    }

    public boolean hasRankRequire() {
        return requiredRank.weight() > StaffRank.NONE.ordinal();
    }

    public boolean canChat(Account account) {
        return account.getRank().isHigherOrEqualTo(requiredRank);
    }

    public ChatMessage handleChat(ChatMessage message) {
        Account account = message.getAccount();
        StaffRank rank = account.getStaffRank();
        message.setFormat(format.replace("{message}", rank.getPrefix() + " " + account.getName() + " %2$s"));
        return message;
    }

}
