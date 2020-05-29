package net.mcskirmish.chat;

import net.mcskirmish.IInteractive;
import net.mcskirmish.Module;
import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.account.Account;
import net.mcskirmish.util.Prefix;
import net.md_5.bungee.event.EventHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatManager extends Module implements IInteractive {

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
        //TODO UTILIZE CHAT POLICY IM TOO TIRED RN LOL
    }

    public ChatPolicy getChatPolicy() {
        return chatPolicy;
    }

    public void setChatPolicy(ChatPolicy chatPolicy) {
        this.chatPolicy = chatPolicy;
    }

    @Override
    public String getPrefix() {
        return Prefix.CHAT;
    }

}
