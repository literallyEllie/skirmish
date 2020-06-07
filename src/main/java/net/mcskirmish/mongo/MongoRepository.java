package net.mcskirmish.mongo;

import com.mongodb.client.MongoCollection;
import net.mcskirmish.SkirmishPlugin;
import org.bson.Document;

import java.util.Map;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class MongoRepository {

    private final String collection;
    private final SkirmishPlugin plugin;

    /**
     * Represents a wrapper for collections.
     *
     * For each used collection, a child class should be made respectively
     * in order to access the database.
     *
     * @param collection the collection to use
     * @param plugin the plugin instance
     */
    public MongoRepository(String collection, SkirmishPlugin plugin) {
        this.collection = collection;
        this.plugin = plugin;
    }

    public String getRepository() {
        return collection;
    }

    /**
     * Gets the actual collection from the database.
     *
     * @return the {@link MongoCollection} of this collection
     */
    public MongoCollection<Document> getCollection() {
        return plugin.getMongoManager().getCollection(this);
    }

    /**
     * Queries the collection for key and the variable to search with.
     *
     * @param key the key to find
     * @param search the key
     * @return a returned BSON {@link Document}
     */
    public Document query(String key, Object search) {
        return getCollection().find(eq(key, search)).first();
    }

    /**
     * Attempts to update a pair of data into the collection
     *
     * It will find the data to update using index and updateKey
     * and update the value of updateKey to be equal to updateValue
     *
     * @param index the index to get the data by
     * @param indexQuery the assigned index value
     * @param updateKey the data key to update
     * @param updateValue the assigned value of the data to update
     * @return if it was successful or not.
     */
    public boolean update(String index, Object indexQuery, String updateKey, Object updateValue) {
        try {
            return getCollection().updateOne(eq(index, indexQuery),
                    set(updateKey, updateValue)).wasAcknowledged();
        } catch (Throwable e) {
            plugin.error("error updating " + index + " to repo " + collection, e);
        }
        return false;
    }

    /**
     * Batch inserts a map of data into the database
     *
     * @param index the index to get the data by
     * @param indexQuery the assigned value of the index
     * @param updates the values to update
     */
    public void batchUpdate(String index, Object indexQuery, Map<String, Object> updates) {
        getCollection().updateMany(eq(index, indexQuery), updates.entrySet().stream()
                .map(stringObjectEntry -> set(stringObjectEntry.getKey(), stringObjectEntry.getValue()))
                .collect(Collectors.toList()));
    }

    /**
     * Inserts a BSON {@link Document} into the collection.
     *
     * @param document the document to insert
     */
    public void insert(Document document) {
        getCollection().insertOne(document);
    }

}
