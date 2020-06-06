package net.mcskirmish.chat;

import net.mcskirmish.account.Account;

public class ChatMessage {

    private final Account account;
    private String format, message;
    private boolean cancelled;

    /**
     * Represents a chat message that is sent from a player
     *
     * @param account account who sent
     * @param format the format of the message
     * @param message the raw message they wrote
     */
    public ChatMessage(Account account, String format, String message) {
        this.account = account;
        this.format = format;
        this.message = message;
    }

    public Account getAccount() {
        return this.account;
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

}
