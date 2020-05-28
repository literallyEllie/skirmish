package net.mcskirmish.account;

import com.google.common.collect.Lists;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import javax.swing.plaf.SplitPaneUI;
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
            LAST_SERVER = "last_server";

    private final AccountManager accountManager;
    private final Document document;

    private Player player;

    public Account(AccountManager accountManager, Document document) {
        this.accountManager = accountManager;
        this.document = document;
    }

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
        return document.getString(NAME);
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
        set(RANK, rank.toString());
        setDisplay();
    }

    public void setDisplay() {
        if (player == null)
            return;
        final Rank rank = getRank();

        player.setDisplayName(rank.getPrefix() + (rank.isDefault() ? "" : " ") + getName());
    }

    /*
    public long getLastUse(String key) {
        if (!hasAttribute(LAST_USE))
            return -1;
        final List<Long> lastUse = document.get(LAST_USE, Long.class);
        return Long.parseLong(dataAttribute.asM().getOrDefault(key, "-1"));
    }

    public void setLastUseNow(String key) {
        setLastUse(key, System.currentTimeMillis());
    }

    public void setLastUse(String key, long when) {
        if (!hasAttribute(LAST_USE)) {
            attributes.put(LAST_USE, new DataAttribute(Maps.newHashMap()));
        }

        final Map<String, String> uses = get(LAST_USE).asM();
        uses.put(key, String.valueOf(when));
        set(key, new DataAttribute(uses));
    }

    public boolean hasUsedBefore(String key) {
        return hasAttribute(LAST_USE) && get(LAST_USE).asM().containsKey(key);
    }

     */

}
