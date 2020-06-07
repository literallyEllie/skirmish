package net.mcskirmish.chat;

import net.mcskirmish.IInteractive;
import net.mcskirmish.Module;
import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.account.Account;
import net.mcskirmish.util.P;
import net.md_5.bungee.event.EventHandler;
import org.bukkit.ChatColor;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatManager extends Module implements IInteractive {

    private ChatPolicy chatPolicy;

    /**
     * Chat manager processing all messages through a {@link ChatPolicy}
     * This includes:
     * - minimum rank to speak
     * - chat filter
     * - chat delay
     * - replacing chat colors
     *
     * By default it will use the base implementations of {@link ChatPolicy}
     * however it can be modified by an underlying plugin and set
     *
     * @param plugin instance of the plugin
     */
    public ChatManager(SkirmishPlugin plugin) {
        super(plugin);
    }

    @Override
    protected void start() {
        chatPolicy = new ChatPolicy();
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Account account = plugin.getAccountManager().getAccount(e.getPlayer());

        e.setFormat(e.getPlayer().getDisplayName() + ": " + ChatColor.RESET + " %s");
    }

    public ChatPolicy getChatPolicy() {
        return chatPolicy;
    }

    public void setChatPolicy(ChatPolicy chatPolicy) {
        this.chatPolicy = chatPolicy;
    }

    @Override
    public String getPrefix() {
        return P.MODULE + "CHAT";
    }

}
