package net.mcskirmish.account;

import com.google.common.collect.Maps;
import net.mcskirmish.Module;
import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.mongo.table.AccountsRepository;
import net.mcskirmish.util.*;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class AccountManager extends Module {

    private static final String FAIL_LOG = "DB_FAILS.txt";

    private final Object loadLock = new Object();
    private AccountsRepository repo;
    private Map<UUID, Account> accounts;

    public AccountManager(SkirmishPlugin plugin) {
        super(plugin);
    }

    @Override
    protected void start() {
        accounts = Maps.newHashMap();
        repo = new AccountsRepository(plugin);
    }

    @EventHandler
    public void onPreJoin(AsyncPlayerPreLoginEvent event) {
        final UUID uuid = event.getUniqueId();
        final String name = event.getName();
        final String ip = event.getAddress().getHostAddress();

        if (name.contains(" ")) { // This actually exists! Search 'Daniel B' for an example.
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, ChatColor.RED + "Your name contains an invalid character!");
            return;
        }

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

            // update names
            if (!newAccount && !name.equals(account.getName())) {
                account.set(Account.NAME, name);
                account.set(Account.NAME_LOWER, name.toLowerCase());

                if (!pNames.contains(name)) {
                    pNames.add(name);
                    account.set(Account.PREV_NAMES, pNames);
                }
            }

            // update ips
            List<String> ips = account.getIPs();
            if (!ips.contains(ip)) {
                ips.add(ip);
                account.set(Account.IPS, ips);
            }

            // check can join
            final Rank minRank = plugin.getServerManager().getMinRank();
            if (account.getRank().ordinal() < minRank.ordinal()) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
                        ChatColor.RED + "Only " + minRank.getPrefix() + ChatColor.RED + "+ can join this server :(");
                return;
            }

            // Set first time
            account.set(Account.FIRST_TIME, true, null, false);
            account.set(Account.LAST_SERVER, plugin.getServerManager().getServerId());

            accounts.put(uuid, account);
            if (newAccount) {
                Account finalAccount = account;
                runAsync(() -> repo.insert(finalAccount.getDocument()));
            }
        }

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        final Player player = event.getPlayer();
        final Account account = getAccount(player);

        if (account == null) {
            player.kickPlayer(C.IC + "Sorry! There was an error loading your data..." +
                    "\n\nJoin our Discord for support " + C.IV + Domain.DISCORD);
            return;
        }

        account.setPlayer(player);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        accounts.remove(event.getPlayer().getUniqueId());
        event.setQuitMessage(null);
    }

    public Collection<Account> getAccounts() {
        return accounts.values();
    }

    public Account getAccount(Player player) {
        return getAccount(player.getUniqueId(), false);
    }

    public Account getAccount(UUID uuid, boolean database) {
        if (accounts.containsKey(uuid))
            return accounts.get(uuid);

        if (database) {
            final Document account = repo.query(Account.ID, uuid);
            if (account != null) {
                return new Account(this, account);
            }
        }

        return null;
    }

    public Account getAccount(String name, boolean database) {
        String nameLower = name.toLowerCase();

        final Optional<Account> localAccount = accounts.values().stream()
                .filter(account -> account.getNameLower().equals(nameLower))
                .findFirst();
        if (localAccount.isPresent())
            return localAccount.get();

        if (database) {
            final Document account = repo.query(Account.NAME_LOWER, nameLower);
            if (account != null) {
                return new Account(this, account);
            }
        }

        return null;
    }

    public void updateAccount(Account account, String key, Object attribute, Consumer<Boolean> callback) {
        if (key != null && attribute != null) {
            runAsync(() -> {
                final boolean success = repo.update(Account.ID, account.getUuid(), key, attribute);

                if (callback != null)
                    callback.accept(success);

                if (!success) {
                    logDbError(UtilTime.fullDate() + " FAILED UPDATE '" + key + "': '" + attribute + "' to " + repo.getRepository() + " for " + account.getUuid());
                }
            });

        }
    }

    private void logDbError(String description) {
        File file = new File(FAIL_LOG);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter writer = new FileWriter(file, true);
            writer.write(description + System.lineSeparator());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            plugin.error("error writing update log", e);
        }

    }

}
