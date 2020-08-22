package net.mcskirmish.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.mcskirmish.Module;
import net.mcskirmish.SkirmishPlugin;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bukkit.configuration.file.FileConfiguration;

public class MongoManager extends Module {

    private MongoClient client;
    private MongoDatabase database;

    /**
     * Base wrapper used to access the database.
     * <p>
     * Access using a {@link MongoRepository} through {@link MongoManager#getCollection(MongoRepository)}
     *
     * @param plugin plugin instance
     */
    public MongoManager(SkirmishPlugin plugin) {
        super(plugin);
    }

    @Override
    protected void start() {
        final FileConfiguration config = plugin.getConfig();

        MongoCredential credential = MongoCredential.createCredential(
                config.getString("mongo.username"),
                config.getString("mongo.udatabase"),
                config.getString("mongo.password").toCharArray()
        );

        client = new MongoClient(
                new ServerAddress(config.getString("mongo.host"), config.getInt("mongo.port")),
                credential,
                MongoClientOptions.builder()
                        .uuidRepresentation(UuidRepresentation.STANDARD)
                        .build()
        );

        database = client.getDatabase(config.getString("mongo.database"));
    }

    @Override
    protected void stop() {
        if (client != null)
            client.close();
    }

    /**
     * Gets the collection from a {@link MongoRepository} wrapper.
     * <p>
     * May be null if the is no database connected.
     *
     * @param table the table of the data
     * @return the {@link MongoCollection} of the table, or null if there is no connection.
     */
    public MongoCollection<Document> getCollection(MongoRepository table) {
        return database != null
                ? database.getCollection(table.getRepository())
                : null;
    }

}
