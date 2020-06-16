package net.mcskirmish.mongo;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import net.mcskirmish.SkirmishPlugin;
import org.bson.Document;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class MongoRepository {

    private final String collection;
    private final SkirmishPlugin plugin;

    /**
     * Represents a wrapper for collections.
     * <p>
     * For each used collection, a child class should be made respectively
     * in order to access the database.
     *
     * @param collection the collection to use
     * @param plugin     the plugin instance
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
     * @param index      the key to find
     * @param indexQuery the key
     * @return a returned BSON {@link Document}, may be null
     */
    public Document query(String index, Object indexQuery) {
        return queryMany(index, indexQuery).first();
    }

    /**
     * Queries the collection for as many documents as possible that match the query.
     *
     * @param index      index to get data by
     * @param indexQuery assigned value of index
     * @return an iterable of the resulted queries
     */
    public FindIterable<Document> queryMany(String index, Object indexQuery) {
        return getCollection().find(eq(index, indexQuery));
    }

    /**
     * Gets all the documents in the collection.
     *
     * @return all the documents in the collection.
     */
    public FindIterable<Document> queryAll() {
        return getCollection().find();
    }

    /**
     * Attempts to update a pair of data into the collection
     * <p>
     * It will find the data to update using index and updateKey
     * and update the value of updateKey to be equal to updateValue
     *
     * @param index       the index to get the data by
     * @param indexQuery  the assigned index value
     * @param updateKey   the data key to update
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
     * Batch inserts data of an entry set into the database
     *
     * @param index      the index to get the data by
     * @param indexQuery the assigned value of the index
     * @param updates    the entries to update
     */
    public void batchUpdate(String index, Object indexQuery, Set<Map.Entry<String, Object>> updates) {
        getCollection().updateMany(eq(index, indexQuery),
                updates.stream()
                        .map(stringObjectEntry -> set(stringObjectEntry.getKey(), stringObjectEntry.getValue()))
                        .collect(Collectors.toList()));
    }

    /**
     * Batch inserts data of a map into the database
     *
     * @param index      the index to get the data by
     * @param indexQuery the assigned value of the index
     * @param updates    the values to update
     */
    public void batchUpdate(String index, Object indexQuery, Map<String, Object> updates) {
        batchUpdate(index, indexQuery, updates.entrySet());
    }

    /**
     * Batch inserts data of a document into the database
     *
     * @param index      the index to get the data by
     * @param indexQuery the assigned value of the index
     * @param document   the full document to update
     */
    public void updateFromDocument(String index, Object indexQuery, Document document) {
        batchUpdate(index, indexQuery, document.entrySet());
    }

    /**
     * Inserts a BSON {@link Document} into the collection.
     *
     * @param document the document to insert
     */
    public void insert(Document document) {
        getCollection().insertOne(document);
    }

    /**
     * Deletes as many documents as possible matching the query.
     *
     * @param index      the index of the query
     * @param indexQuery the required value to be deleted
     */
    public void delete(String index, Object indexQuery) {
        getCollection().deleteMany(eq(index, indexQuery));
    }

}
