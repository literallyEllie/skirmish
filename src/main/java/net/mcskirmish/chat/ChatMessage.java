package net.mcskirmish.chat;

import net.mcskirmish.account.Account;

public class ChatMessage {

    // Check if this commits

    private Account account;
    private String format, message;
    private boolean cancelled;

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

    public String getMessage() {
        return this.message;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void setFormat(String format) {
        this.format = format;
    }

}
