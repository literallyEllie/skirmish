package net.mcskirmish.chat;

import net.mcskirmish.account.Account;
import net.mcskirmish.account.Rank;
import net.mcskirmish.util.UtilServer;
import org.bukkit.ChatColor;

public class ChatPolicy {

    private String format;
    private int chatDelay;
    private Rank requiredRank;

    /**
     * The base implementation of a {@link ChatPolicy}
     *
     * Controls how the chat functions.
     */
    public ChatPolicy() {
        this.format = "{display}" + ChatColor.DARK_GRAY + "> {message}";
        this.chatDelay = 1;
        this.requiredRank = Rank.PLAYER;
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

    public Rank getRequiredRank() {
        return this.requiredRank;
    }

    public void setChatDelay(String executorName, boolean silent, int delay) {
        if (!silent) {
            // todo
            UtilServer.broadcast("..");
        }

        this.chatDelay = delay;
    }

    public boolean hasChatDelay() {
        return chatDelay > 0;
    }

    public void setRequiredRank(String executorName, boolean silent, Rank rank) {
        if (!silent) {
            // todo
            UtilServer.broadcast("..");
        }

        this.requiredRank = rank;
    }

    public boolean hasRankRequire() {
        return requiredRank.ordinal() > Rank.PLAYER.ordinal();
    }

    public ChatMessage handleChat(ChatMessage message) {
        Account account = message.getAccount();
        Rank rank = account.getRank();
        message.setFormat(format.replace("{message}", (rank.isDefault() ? "" : rank.getPrefix() + " ") + account.getName() + " %2$s"));
        return message;
    }

}
