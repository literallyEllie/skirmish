package net.mcskirmish.chat;

import net.mcskirmish.account.Account;
import net.mcskirmish.account.Rank;
import org.bukkit.ChatColor;

public class ChatPolicy {

    private String format;
    private int chatDelay;
    private Rank requiredRank;

    public ChatPolicy() {
        this.format = "{display}" + ChatColor.DARK_GRAY + "> {message}";
        this.chatDelay = 3;
        this.requiredRank = Rank.PLAYER;
    }

    public String getFormat() {
        return this.format;
    }

    public int getChatDelay() {
        return this.chatDelay;
    }

    public Rank getRequiredRank() {
        return this.requiredRank;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setChatDelay(String executorName, boolean silent, int delay) {
        if (!silent) {
            //TODO SEND A MESSAGE.
        }

        this.chatDelay = delay;
    }

    public void setRequiredRank(String executorName, boolean silent, Rank rank) {
        if (!silent) {
            //TODO SEND A MESSAGE.
        }

        this.requiredRank = rank;
    }

    public ChatMessage handleChat(ChatMessage message) {
        Account account = message.getAccount();
        Rank rank = account.getRank();
        message.setFormat(format.replace("{message}", (rank.isDefault() ? "" : rank.getPrefix() + " ") + account.getName() + " %2$s"));
        return message;
    }

}
