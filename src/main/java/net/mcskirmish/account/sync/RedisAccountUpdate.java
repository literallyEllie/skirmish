package net.mcskirmish.account.sync;

import java.util.UUID;

public class RedisAccountUpdate {

    private final UUID uuid;
    private final String fieldName, data;
    private final RedisAccountSynchronizer.GenericType type;

    public RedisAccountUpdate(UUID uuid, String fieldName, String data, RedisAccountSynchronizer.GenericType type) {
        this.uuid = uuid;
        this.fieldName = fieldName;
        this.data = data;
        this.type = type;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getData() {
        return data;
    }

    public RedisAccountSynchronizer.GenericType getType() {
        return type;
    }

}
