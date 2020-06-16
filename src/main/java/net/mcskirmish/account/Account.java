package net.mcskirmish.account;

import com.google.common.collect.Lists;
import net.mcskirmish.IInteractive;
import net.mcskirmish.util.C;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class Account {

    public static final String ID = "_id",
            NAME = "name",
            NAME_LOWER = "name_lower",
            PREV_NAMES = "prev_names",
            FIRST_LOGIN = "first_login",
            LAST_LOGIN = "last_login",
            FIRST_TIME = "first_time",
            IPS = "ips",
            RANK = "rank",
            LAST_USE = "last_use",
            DESTINATION_SERVER = "destination_server",
            LAST_SERVER = "last_server",
            VANISHED = "vanished",
            DISCORD_ID = "discord_id";

    private final AccountManager accountManager;
    private final Document document;

    private Player player;
    private HashMap<String, Long> cooldowns;

    /**
     * Represents an account and personal data of a user.
     * <p>
     * All primitive data is stored in a BSON {@link Document} which is received from the database
     * upon getting via {@link AccountManager#getAccount(UUID, boolean)}
     *
     * @param accountManager instance of the account manager
     * @param document       raw document of the player
     */
    public Account(AccountManager accountManager, Document document) {
        this.accountManager = accountManager;
        this.document = document;
        this.cooldowns = new HashMap<>();
    }

    /**
     * Creates a new account instance presuming the account is being created <b>now</b>
     * Typically called on {@link org.bukkit.event.player.AsyncPlayerPreLoginEvent}
     * <p>
     * Default values set:
     * - Name
     * - Name in lower-case
     * - Empty list of previous names
     * - First login ({@link System#currentTimeMillis()})
     * - Singleton list of their connected IP
     * - Their rank as {@link Rank#PLAYER}
     *
     * @param accountManager account manager instance
     * @param uuid           the uuid of the player
     * @param name           the connecting name of the player
     * @param ip             the the player is connecting with
     * @return default account instance
     */
    public static Account newAccount(AccountManager accountManager, UUID uuid, String name, String ip) {
        Document document = new Document(ID, uuid)
                .append(NAME, name)
                .append(NAME_LOWER, name.toLowerCase())
                .append(PREV_NAMES, Lists.newArrayList())
                .append(FIRST_LOGIN, System.currentTimeMillis())
                .append(IPS, Lists.newArrayList(ip))
                .append(RANK, Rank.PLAYER.name());
        return new Account(accountManager, document);
    }

    public Document getDocument() {
        return document;
    }

    public Object get(String key) {
        return document.get(key);
    }

    public boolean hasAttribute(String key) {
        return document.containsKey(key);
    }

    public void set(String key, Object value) {
        set(key, value, null);
    }

    public void set(String key, Object value, Consumer<Boolean> callback) {
        set(key, value, callback, true);
    }

    public void set(String key, Object value, Consumer<Boolean> callback, boolean database) {
        document.put(key, value);
        if (database) {
            accountManager.updateAccount(this, key, value, callback);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
        setDisplay();
    }

    public UUID getUuid() {
        return (UUID) document.get(ID);
    }

    public String getName() {
        return document.getString(NAME);
    }

    public String getNameLower() {
        return document.getString(NAME_LOWER);
    }

    public List<String> getPreviousNames() {
        return document.getList(PREV_NAMES, String.class);
    }

    public long getFirstLogin() {
        return document.getLong(FIRST_LOGIN);
    }

    public boolean isFirstLogin() {
        return !hasAttribute(LAST_LOGIN);
    }

    public long getLastLogin() {
        return document.getLong(LAST_LOGIN);
    }

    public boolean isFirstTime() {
        return document.getBoolean(FIRST_TIME);
    }

    public List<String> getIPs() {
        return document.getList(IPS, String.class);
    }

    public Rank getRank() {
        try {
            return Rank.valueOf(document.getString(RANK));
        } catch (IllegalArgumentException e) {
            System.out.println(getUuid() + " has invalid rank (" + document.getString(RANK) + ")");
            setRank(Rank.PLAYER);
        }

        return Rank.PLAYER;
    }

    public void setRank(Rank rank) {
        set(RANK, rank.name());
        setDisplay();
    }

    public void setDisplay() {
        if (player == null)
            return;
        final Rank rank = getRank();

        player.setDisplayName(rank.getPrefix() + (rank.isDefault() ? "" : " ") + getName());
    }

    public String getDestinationServer() {
        return document.getString(DESTINATION_SERVER);
    }

    public String getLastServer() {
        return document.getString(LAST_SERVER);
    }

    public boolean isVanished() {
        return document.getBoolean(VANISHED);
    }

    public long getDiscordId() {
        return document.getLong(DISCORD_ID);
    }

    public boolean hasDiscordLinked() {
        return document.containsKey(DISCORD_ID);
    }

    /*  */

    public void sendMessage(String message) {
        if (player == null)
            return;

        player.sendMessage(message);
    }

    public void sendMessage(String... messages) {
        if (player == null)
            return;

        for (String message : messages) {
            sendMessage(message);
        }
    }

    public void sendMessage(IInteractive interactive, String message) {
        if (player == null)
            return;

        interactive.message(player, message);
    }

    public void addCooldown(String name, double seconds) {
        cooldowns.put(name, (long) (System.currentTimeMillis() + (seconds * 1000)));
    }

    public long getCooldown(String name) {
        return cooldowns.get(name);
    }

    public boolean isCooldownActive(String name, boolean silent) {
        if (!cooldowns.containsKey(name))
            return false;

        final long remaining = cooldowns.get(name) - System.currentTimeMillis();
        boolean active = remaining > 0;

        if (!silent && active) {
            sendMessage(C.V + name + C.C + " is on cooldown for " + C.V + (remaining / 1000) + " seconds.");
        } else if (!active)
            cooldowns.remove(name);

        return active;
    }

}
