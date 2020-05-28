package net.mcskirmish.mongo;

import com.mongodb.client.MongoCollection;
import net.mcskirmish.SkirmishPlugin;
import org.bson.Document;

import java.util.Map;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

public class MongoRepository {

    private final String collection;
    private final SkirmishPlugin plugin;

    public MongoRepository(String collection, SkirmishPlugin plugin) {
        this.collection = collection;
        this.plugin = plugin;
    }

    public String getRepository() {
        return collection;
    }

    public MongoCollection<Document> getCollection() {
        return plugin.getMongoManager().getCollection(this);
    }

    public Document query(String key, Object search) {
        return getCollection().find(eq(key, search)).first();
    }

    public boolean update(String index, Object indexQuery, String updateKey, Object updateValue) {
        try {
            return getCollection().updateOne(eq(index, indexQuery),
                    set(updateKey, updateValue)).wasAcknowledged();
        } catch (Throwable e) {
            plugin.error("error updating " + index + " to repo " + collection, e);
        }
        return false;
    }

    public void batchUpdate(String index, Object indexQuery, Map<String, Object> updates) {
        getCollection().updateMany(eq(index, indexQuery), updates.entrySet().stream()
                        .map(stringObjectEntry -> set(stringObjectEntry.getKey(), stringObjectEntry.getValue()))
                        .collect(Collectors.toList()));
    }

    public void insert(Document document) {
        getCollection().insertOne(document);
    }

}
