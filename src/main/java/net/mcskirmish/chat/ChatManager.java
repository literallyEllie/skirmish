package net.mcskirmish.chat;

import net.mcskirmish.IInteractive;
import net.mcskirmish.Module;
import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.account.Account;
import net.mcskirmish.account.Rank;
import net.mcskirmish.util.P;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.regex.Pattern;

public class ChatManager extends Module implements IInteractive {

    private static final Pattern REGEX_COLOR = Pattern.compile("&([abcdefABCDEF0123456789])");

    private ChatPolicy chatPolicy;

    /**
     * Chat manager processing all messages through a {@link ChatPolicy}
     * This includes:
     * - minimum rank to speak
     * - chat filter
     * - chat delay
     * - replacing chat colors
     * <p>
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
    public void onChat(AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        Account account = plugin.getAccountManager().getAccount(player.getPlayer());

        // fallback format
        event.setFormat(player.getDisplayName() + ChatColor.DARK_GRAY + ": " + ChatColor.RESET + " %2$s");

        // replace colors
        if (account.getRank().isHigherOrEqualTo(Rank.ADMIN)) {
            event.setMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
        } else if (account.getRank().isHigherOrEqualTo(Rank.MODERATOR)) {
            event.setMessage(event.getMessage().replaceAll(REGEX_COLOR.pattern(), ChatColor.COLOR_CHAR + "$1"));
        }

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
