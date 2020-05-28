package net.mcskirmish.account;

import com.google.common.collect.Maps;
import net.mcskirmish.Module;
import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.mongo.table.AccountsRepository;
import net.mcskirmish.util.D;
import net.mcskirmish.util.UtilPlayer;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class AccountManager extends Module {

    private final Object loadLock = new Object();
    private AccountsRepository repo;
    private Map<UUID, Account> accounts;

    public AccountManager(SkirmishPlugin plugin) {
        super(plugin);
    }

    @Override
    protected void start() {
        accounts = Maps.newHashMap();
        repo = new AccountsRepository();
    }

    @EventHandler
    public void onPreJoin(AsyncPlayerPreLoginEvent event) {
        final UUID uuid = event.getUniqueId();
        final String name = event.getName();
        final String ip = event.getAddress().getHostAddress();

        if (UtilPlayer.getP(uuid) != null || UtilPlayer.getPExact(name) != null) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.RED + "You are already connected!");
            return;
        }

        synchronized (loadLock) {
            boolean newAccount;

            Account account = getAccount(uuid, true);
            if ((newAccount = account == null)) {
                account = Account.newAccount(this, uuid, name, ip);
            }

            account.set(Account.LAST_LOGIN, System.currentTimeMillis());
            List<String> pNames = account.getPreviousNames();
            List<String> ips = account.getIPs();

            if (!newAccount && !name.equals(account.get(Account.NAME))) {
                account.set(Account.NAME, name);
                account.set(Account.NAME_LOWER, name.toLowerCase());

                if (!pNames.contains(name)) {
                    pNames.add(name);
                    account.set(Account.PREV_NAMES, pNames);
                }
            }

            if (!ips.contains(ip)) {
                ips.add(ip);
                account.set(Account.IPS, ips);
            }
            // TODO first time local var

            // if all gucci
            accounts.put(uuid, account);
            if (newAccount) {
                Account finalAccount = account;
                runAsync(() -> repo.insert(repo.getCollection(plugin), finalAccount.getDocument()));
            } else {

            }

        }

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final Account account = getAccount(player);

        if (account == null) {
            player.kickPlayer(ChatColor.RED + "Sorry! There was an error loading your data. Join our Discord for support www.discord.gg/potato");
            return;
        }

        account.setPlayer(player);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        accounts.remove(event.getPlayer().getUniqueId());
    }

    public Account getAccount(Player player) {
        return getAccount(player.getUniqueId(), false);
    }

    public Account getAccount(UUID uuid, boolean db) {
        if (!db || accounts.containsKey(uuid))
            return accounts.get(uuid);

        // TODO make accessing repo cleaner
        final Document query = repo.query(repo.getCollection(plugin), Account.ID, uuid);
        if (query != null) {
            return new Account(this, query);
        }

        return null;
    }

    public void updateAccount(Account account, String key, Object attribute, Consumer<Boolean> callback) {
        if (key != null && attribute != null) {
            runAsync(() -> {
                D.d("updating " + account.getUuid());
                repo.update(repo.getCollection(plugin), Account.ID, account.getUuid(), key, attribute);

                if (callback != null)
                    callback.accept(true);
            });

        }
    }

}
