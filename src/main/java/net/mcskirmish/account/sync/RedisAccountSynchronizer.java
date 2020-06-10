package net.mcskirmish.account.sync;

import com.google.common.collect.BiMap;
import com.google.common.collect.EnumHashBiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.account.Account;
import net.mcskirmish.account.AccountManager;
import net.mcskirmish.redis.RedisSubscriber;
import net.mcskirmish.util.UtilJson;
import org.bukkit.material.LongGrass;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class RedisAccountSynchronizer extends RedisSubscriber<RedisAccountUpdate, AccountManager> {

    private static final TypeToken<List<String>> TOKEN_LIST_STRING = new TypeToken<List<String>>(){};

    private final Map<GenericType, Function<String, Object>> genericParsers;
    private final BiMap<Class<?>, GenericType> typeLookup;
    private final Map<String, Function<String, Object>> specialParsers;

    /**
     * Synchronizer for account data being updated across the network.
     *
     * This was a good idea but it is inherently flawed and inefficient.
     * For this to work it would require each field to adapt the data themselves,
     * which does not scale.
     *
     * Alternatively, only have this sync for some native {@link Account} fields which can be parsed easily,
     * like primitive types and enum constants.
     *
     * {@link Account#getRank()} is the most important anyway.
     *
     * @param plugin the plugin instance
     * @param parent account manager instance.
     */
    public RedisAccountSynchronizer(SkirmishPlugin plugin, AccountManager parent) {
        super(RedisAccountUpdate.class, plugin, parent, "ACCOUNT");

        genericParsers = Maps.newHashMap();
        typeLookup = HashBiMap.create();
        specialParsers = Maps.newHashMap();

        //
        genericParsers.put(GenericType.INTEGER, Integer::valueOf);
        genericParsers.put(GenericType.LONG, Long::valueOf);
        genericParsers.put(GenericType.LIST_STRING, s -> UtilJson.fromString(s, TOKEN_LIST_STRING));

        typeLookup.put(Integer.class, GenericType.INTEGER);
        typeLookup.put(Long.class, GenericType.LONG);
    }

    public void sendUpdate(String field) {

    }

    @Override
    public void receive(RedisAccountUpdate data) {
        final UUID uuid = data.getUuid();
        final Account account = parent.getAccount(uuid, false);
        if (account == null)
            return;

        Object parsedData;
        if (data.getType().isCustom()) {
            final Function<String, Object> parseFunction = specialParsers.get(data.getFieldName());
            if (parseFunction == null) {
                plugin.warn("do not know how to parse " + data.getFieldName() + ": " + data.getData() + " - " + data.getUuid());
                return;
            }

            parsedData = parseFunction.apply(data.getData());
        } else {
            final Function<String, Object> parseFunction = genericParsers.get(data.getType());
            if (parseFunction == null) {
                plugin.warn("do not know how to parse " + data.getFieldName() + "(" + data.getType() + "): " + data.getData() + " - " + data.getUuid());
                return;
            }

            parsedData = parseFunction.apply(data.getData());
        }

        account.set(data.getFieldName(), parsedData, null, false);
    }

    public void addParser(String fieldName, Function<String, Object> data) {
        specialParsers.put(fieldName, data);
    }

    enum GenericType {
        INTEGER,
        LONG,
        LIST_STRING,
        CUSTOM;

        public boolean isCustom() {
            return this == CUSTOM;
        }
    }

}
