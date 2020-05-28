package net.mcskirmish.chat;

import net.mcskirmish.Module;
import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.account.Account;
import net.mcskirmish.account.Rank;
import net.md_5.bungee.event.EventHandler;
import org.bukkit.ChatColor;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatManager extends Module {

    private ChatPolicy chatPolicy;

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
        //TODO PROVIDE AN OPTION TO MAKE IT SO ANY SERVER CAN SET WHAT THE CHAT LOOKS LIKE
    }

    public ChatPolicy getChatPolicy() {
        return chatPolicy;
    }

    public void setChatPolicy(ChatPolicy chatPolicy) {
        this.chatPolicy = chatPolicy;
    }

}
