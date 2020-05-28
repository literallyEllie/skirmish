package net.mcskirmish.mongo;

import com.mongodb.client.MongoCollection;
import net.mcskirmish.SkirmishPlugin;
import org.bson.Document;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

public class MongoRepository {

    private final String collection;

    public MongoRepository(String collection) {
        this.collection = collection;
    }

    public String getRepository() {
        return collection;
    }

    public MongoCollection<Document> getCollection(SkirmishPlugin plugin) {
        return plugin.getMongoManager().getCollection(this);
    }

    public Document query(MongoCollection<Document> collection, String key, Object search) {
        return collection.find(eq(key, search)).first();
    }

    public void update(MongoCollection<Document> collection, String index, Object indexQuery,
                       String updateKey, Object updateValue) {
        collection.updateOne(eq(index, indexQuery),
                set(updateKey, updateValue));
    }

    public void insert(MongoCollection<Document> collection, Document document) {
        collection.insertOne(document);
    }


}
